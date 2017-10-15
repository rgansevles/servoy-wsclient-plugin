/*
MIT License

Copyright (c) 2017 Rob Gansevles

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package nl.rgansevles.servoy.wsclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSFunction;

import com.servoy.j2db.documentation.ServoyDocumented;
import com.servoy.j2db.plugins.IClientPluginAccess;
import com.servoy.j2db.scripting.FunctionDefinition;
import com.servoy.j2db.scripting.JSEvent;

@ServoyDocumented
public class WebsocketClient {

	private IClientPluginAccess clientPluginAccess;
	private FunctionDefinition onOpenMethod;
	private FunctionDefinition onMessageMethod;
	private WebsocketClientEndpoint endpoint;

	public WebsocketClient(IClientPluginAccess clientPluginAccess) {
		this.clientPluginAccess = clientPluginAccess;
	}

	@JSFunction
	public void setOnOpen(Function onOpenMethod) {
		this.onOpenMethod = new FunctionDefinition(onOpenMethod);
	}

	@JSFunction
	public void setOnMessage(Function onMessageMethod) {
		this.onMessageMethod = new FunctionDefinition(onMessageMethod);
	}

	@JSFunction
	public void connect(String uri) throws IOException, DeploymentException, URISyntaxException {
		ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

		ClientManager client = ClientManager.createClient();
		endpoint = new WebsocketClientEndpoint();
		client.connectToServer(endpoint, cec, new URI(uri));
	}

	@JSFunction
	public void sendText(String text) throws IOException {
		if (endpoint == null) {
			throw new IOException("not connected");
		}
		endpoint.sendText(text);
	}

	private class WebsocketClientEndpoint extends Endpoint {

		private Session session;

		@Override
		public void onOpen(Session session, EndpointConfig config) {

			this.session = session;
			session.addMessageHandler(new MessageHandler.Whole<String>() {
				@Override
				public void onMessage(String message) {
					callFunction(onMessageMethod, "message", message);
				}
			});

			callFunction(onOpenMethod, "open", null);
		}

		void sendText(String text) throws IOException {
			if (session == null) {
				throw new IOException("session not open");
			}
			session.getBasicRemote().sendText(text);
		}

		private void callFunction(FunctionDefinition function, String type, Object data) {
			if (function != null) {
				JSEvent event = new JSEvent();
				event.setType(type);
				event.setSource(this);
				event.setData(data);
				function.executeAsync(clientPluginAccess, new Object[] { event });
			}
		}
	}

}
