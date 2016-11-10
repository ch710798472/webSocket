package com.ch.util;

import com.alibaba.fastjson.JSON;
import com.ch.domain.Message;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by banmo.ch on 16/11/10.
 * Email: banmo.ch@alibaba-inc.com
 */
public class ServerEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message object) throws EncodeException {
        return JSON.toJSONString(object);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
//        System.out.println("ServerEncoder -init method called");
    }

    @Override
    public void destroy() {
//        System.out.println("ServerEncoder - destroy method called");
    }
}
