package io.github.xucancould.module.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

/**
 * created by Xu on 2024/3/21 19:06.
 */
public class MessageWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WsHandler wsHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        wsHandler.handleOpen(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            wsHandler.handleMessage(session, textMessage.getPayload());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        wsHandler.handleError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        wsHandler.handleClose(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
