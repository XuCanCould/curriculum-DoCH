package io.github.xucancould.module.message;

import io.github.xucancould.service.GroupService;
import io.github.xucancould.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by Xu on 2024/3/21 17:44.
 */
@Slf4j
public class WsHandlerImpl implements WsHandler {

    private final AtomicInteger connectionCount = new AtomicInteger(0);

    private CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private GroupService groupService;


    @Override
    public void handleOpen(WebSocketSession session) {
        sessions.add(session);
        int cnt = connectionCount.incrementAndGet();
        log.info("a new connection opened，current online count：{}", cnt);
    }

    @Override
    public void handleClose(WebSocketSession session) {
        sessions.remove(session);
        int cnt = connectionCount.decrementAndGet();
        log.info("a connection closed，current online count：{}", cnt);
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        this.sendMessage(session, message);
    }

    /**
     * 发送消息给指定用户
     * 要求：1、session未关闭  2、用户已登录
     * @param userId 用户id
     * @param message 消息
     * @throws IOException
     */
    public void sendMessage(Integer userId, TextMessage message) throws IOException {
        Optional<WebSocketSession> userSession = sessions.stream().filter(session -> {
            if (session.isOpen()) {
                return false;
            }
            Map<String, Object> attributes = session.getAttributes();
            if (!attributes.containsKey(MessageConstant.USER_KEY)) {
                return false;
            }
            UserDO user = (UserDO) attributes.get(MessageConstant.USER_KEY);
            return user.getId().equals(userId);
        }).findFirst();
        if (userSession.isPresent()) {  // 判断是否存在ws会话
            userSession.get().sendMessage(message);
        }
    }

    /**
     * 向组群广播。当前会话用户ID属于群组且会话开启，则向该会话发送消息。
     * @param groupId 分组id，根据分组id得到全部的用户id
     * @param message 文本消息
     * @throws IOException
     */
    @Override
    public void broadCastToGroup(Integer groupId, TextMessage message) throws IOException {
        List<Integer> userIds = groupService.getGroupUserIds(groupId);
        for(WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            Map<String, Object> attributes = session.getAttributes();
            if(!attributes.containsKey(MessageConstant.USER_KEY)) {
                continue;
            }
            UserDO user = (UserDO) attributes.get(MessageConstant.USER_KEY);
            boolean anyMatch = userIds.stream().anyMatch(id -> id.equals(user.getId()));
            if (!anyMatch) {
                continue;
            }
            session.sendMessage(message);
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error: {}, session id {}", error.getMessage(), session.getId());
        log.error("", error);
    }

}
