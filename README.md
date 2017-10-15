# Websocket Client plugin

### Prerequisites

Servoy 7+, Maven and Java (jdk) 1.7+ installed

### Building and deployment

Compile the source and build the zip file:

```
mvn clean package
```

Unzip the resulting zipfile in your Servoy plugins directory:

```
cd /path/to/servoy/application_server/plugins
unzip /path/to/this/plugin/servoy-wsclient-plugin/target/servoy-wsclient-plugin*.zip
```

(Re)start Servoy Developer.

## Solution example code

```
var wsclient = null;

function ws_echo() {
    wsclient = plugins.wsclient.createClient();
    wsclient.setOnOpen(ws_onopen);
    wsclient.setOnMessage(ws_onmessage);
    wsclient.connect('ws://echo.websocket.org');
}

function ws_onopen() {
    application.output('Opened');
    wsclient.sendText('Hello');
}

function ws_onmessage(event) {
    application.output('Incoming message: ' + event.data)
}
```

## Built With

* [ Tyrus ](https://github.com/tyrus-project/tyrus) - The open source JSR 356 - Java API for WebSocket reference implementation

## Authors

* **Rob Gansevles** - *Initial work*

See also the list of [contributors](https://github.com/rgansevles/servoy-wsclient-plugin/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the LICENSE file for details

