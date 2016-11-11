package com.ch.websocket;

import com.ch.domain.Message;
import com.ch.entity.User;
import com.ch.util.ServerDecoder;
import com.ch.util.ServerEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by banmo.ch on 16/11/9.
 * Email: banmo.ch@alibaba-inc.com
 */
@ServerEndpoint(value = "/websocket", encoders = {ServerEncoder.class}, decoders = {ServerDecoder.class})
@Component
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
    private static ConcurrentHashMap<Long, User> user = new ConcurrentHashMap<Long, User>();

    private Session session;

    @RequestMapping("/chat")
    public ModelAndView websocket() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count_num", getOnlineCount());
        ModelAndView modelAndView = new ModelAndView("websocket");
        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        System.out.println("有新链接加入!当前在线人数为" + getOnlineCount() + ", 你的ID是：" + session.getId());
        //推送当前在线人数
        Message message = new Message("open", getOnlineCount() + "", Long.valueOf(session.getId()));
        for (WebSocketController item : webSocketSet) {
            item.sendObjectMessage(message);
        }
        Message userId = new Message("userId", getOnlineCount() + "", Long.valueOf(session.getId()));
        this.sendObjectMessage(userId);
    }

    @OnClose
    public void onClose() throws IOException, EncodeException {
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一链接关闭,ID=" + session.getId() + "!当前在线人数为" + getOnlineCount());
        //推送当前在线人数
        User closeUser = user.get(Long.valueOf(session.getId()));
        Message message = new Message("close", getOnlineCount() + "", Long.valueOf(session.getId()), closeUser.getName());
        for (WebSocketController item : webSocketSet) {
            item.sendObjectMessage(message);
        }
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        System.out.println("来自客户端ID=" + session.getId() + "的消息:" + message.toString());

        if (message.getType().equals("userName")) {
            User fronUser = new User(Long.valueOf(session.getId()), message.getUserName());
            user.put(Long.valueOf(session.getId()), fronUser);
            Message userMsg = new Message("userName", getOnlineCount() + "", Long.valueOf(session.getId()), message.getUserName());
            //群发用户名
            for (WebSocketController item : webSocketSet) {
                item.sendObjectMessage(userMsg);
            }
        }

        if (message.getType().equals("msg")) {
            // 群发消息
            message.setUserId(Long.valueOf(session.getId()));
            for (WebSocketController item : webSocketSet) {
//                item.sendMessage(message);
                item.sendObjectMessage(message);
            }
        }

    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendObjectMessage(Object object) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(object);
    }

    public static synchronized int getOnlineCount() {
        return WebSocketController.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketController.onlineCount--;
    }

}
