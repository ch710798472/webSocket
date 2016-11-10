package com.ch.websocket;

import com.ch.domain.Message;
import com.ch.util.ServerDecoder;
import com.ch.util.ServerEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by banmo.ch on 16/11/9.
 * Email: banmo.ch@alibaba-inc.com
 */
@ServerEndpoint(value = "/websocket", encoders = {ServerEncoder.class}, decoders = {ServerDecoder.class})
@Component
@RestController
@RequestMapping("/websocket")
public class MyWebSocket {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    private Session session;

    @RequestMapping("/hi")
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
        Message message = new Message("onLineCount", getOnlineCount() + "");
        for (MyWebSocket item : webSocketSet) {
            item.sendObjectMessage(message);
        }
    }

    @OnClose
    public void onClose() throws IOException, EncodeException {
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一链接关闭,ID=" + session.getId() + "!当前在线人数为" + getOnlineCount());
        //推送当前在线人数
        Message message = new Message("onLineCount", getOnlineCount() + "");
        for (MyWebSocket item : webSocketSet) {
            item.sendObjectMessage(message);
        }
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        System.out.println("来自客户端ID=" + session.getId() + "的消息:" + message.toString());
        // 群发消息
        for (MyWebSocket item : webSocketSet) {
//            item.sendMessage(message);
            item.sendObjectMessage(message);
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendObjectMessage(Object object) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(object);
    }

    public static synchronized int getOnlineCount() {
        return MyWebSocket.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

}
