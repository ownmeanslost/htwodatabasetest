var message = {
    websocket: null,
    init: function (url, code) {
        // 初始化websocket连接
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://" + url + "/websocket/socketServer.do?ACCI_CODE=" + code);
        } else if ('MozWebSocket' in window) {
            websocket = new MozWebSocket("ws://" + url + "/websocket/socketServer.do?ACCI_CODE=" + code);
        } else {
            websocket = new SockJS("http://" + url + "/sockjs/socketServer.do?ACCI_CODE=" + code);
        }
        //websocket.onopen = onOpen;
        websocket.onmessage = message.onMessage;
        //websocket.onerror = onError;
        //websocket.onclose = onClose;
    },
    onMessage: function (evt) {
        alert(evt.data);
    }
}
