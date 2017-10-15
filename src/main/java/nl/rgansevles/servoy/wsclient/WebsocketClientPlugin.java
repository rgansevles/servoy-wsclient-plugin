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

import java.beans.PropertyChangeEvent;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.servoy.j2db.plugins.IClientPlugin;
import com.servoy.j2db.plugins.IClientPluginAccess;
import com.servoy.j2db.plugins.PluginException;
import com.servoy.j2db.preference.PreferencePanel;
import com.servoy.j2db.scripting.IScriptable;

public class WebsocketClientPlugin implements IClientPlugin {

	public static final String PLUGIN_NAME = "wsclient";

	private IClientPluginAccess clientPluginAccess;

	private WebsocketClientProvider websocketClientProvider;

	@Override
	public void load() throws PluginException {
	}

	@Override
	public void initialize(IClientPluginAccess clientPluginAccess) throws PluginException {
		this.clientPluginAccess = clientPluginAccess;
	}

	@Override
	public void unload() throws PluginException {
		clientPluginAccess = null;
		websocketClientProvider = null;
	}

	@Override
	public Properties getProperties() {
		Properties props = new Properties();
		props.put(DISPLAY_NAME, "WebsocketCient Plugin"); //$NON-NLS-1$
		return props;
	}

	public PreferencePanel[] getPreferencePanels() {
		return null;
	}

	@Override
	public String getName() {
		return PLUGIN_NAME;
	}

	@Override
	public IScriptable getScriptObject() {
		if (websocketClientProvider == null) {
			websocketClientProvider = new WebsocketClientProvider(clientPluginAccess);
		}
		return websocketClientProvider;
	}

	@Override
	public Icon getImage() {
		return new ImageIcon(getClass().getResource("wsclient.png"));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

}
