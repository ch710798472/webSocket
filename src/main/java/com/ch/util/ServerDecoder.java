package com.ch.util;

import com.alibaba.fastjson.JSON;
import com.ch.domain.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * Created by banmo.ch on 16/11/10.
 * Email: banmo.ch@alibaba-inc.com
 */
public class ServerDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(String message) throws DecodeException {
        try {
            return JSON.parseObject(message, Message.class);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean willDecode(String message) {
        try {
            // Check if incoming message is valid JSON
            JSON.parseObject(message,Message.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig ec) {
//        System.out.println("ServerDecoder -init method called");
    }

    @Override
    public void destroy() {
//        System.out.println("ServerDecoder - destroy method called");
    }

}
