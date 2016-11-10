package com.ch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by banmo.ch on 16/11/8.
 * Email: banmo.ch@alibaba-inc.com
 */
@RestController
@RequestMapping("/my")
public class MyController {
    Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/hi")
    public ModelAndView index(Map<String, Object> model) {
        ModelAndView modelAndView = new ModelAndView("index");
        model.put("msg","hello");
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("message","world");
        map.put("nickname","陈");
        map.put("windowTitle","小窝聊天室");
        map.put("yourNameSpace","1");
        modelAndView.addAllObjects(map);
        return modelAndView;
    }
}
