package io.github.xucancould.module.message;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * created by Xu on 2024/3/21 17:43.
 * websocket 接口
 */
public interface WsHandler {

    /**
     * 处理 websocket 连接
     * @param session
     */
    void handleOpen(WebSocketSession session);

    /**
     * 关闭连接
     * @param session
     */
    void handleClose(WebSocketSession session);

    /**
     * 处理消息
     * @param session 会话
     * @param message 接收的消息
     */
    void handleMessage(WebSocketSession session, String message);

    /**
     * 发送消息
     * @param session
     * @param message
     */
    void sendMessage(WebSocketSession session, String message) throws IOException;

    /**
     * 发送消息
     * @param userId 用户id
     * @param message 消息
     * @throws IOException
     */
    void sendMessage(Integer userId, TextMessage message) throws IOException;

    /**
     * 发送消息
     * @param session ws会话
     * @param message 文本消息
     * @throws IOException
     */
    void sendMessage(WebSocketSession session, TextMessage message) throws IOException;


    /**
     * 对某个分组广播
     *
     * @param groupId 分组id
     * @param message 文本消息
     * @throws IOException 异常
     */
    void broadCastToGroup(Integer groupId, TextMessage message) throws IOException;


    /**
     * 处理会话异常
     *
     * @param session 会话
     * @param error   异常
     */
    void handleError(WebSocketSession session, Throwable error);

}
