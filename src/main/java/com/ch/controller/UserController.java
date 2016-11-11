package com.ch.controller;

import com.ch.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by banmo.ch on 16/11/8.
 * Email: banmo.ch@alibaba-inc.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    public User view(@PathVariable("id") Long id) {
        User user = new User(id,"zhang");
        return user;
    }

    //public static void main(String[] args) {
    //    SpringApplication.run(UserController.class);
    //}

}
