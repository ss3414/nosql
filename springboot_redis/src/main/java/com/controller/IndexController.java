package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class IndexController {

    @GetMapping("/")
    public Map index() {
        Map map = new HashMap();
        map.put("status", 1000);
        return map;
    }

    @RequestMapping("/session")
    public Map session(HttpSession session) {
        if (session.getAttribute("sessionKey") == null) {
            session.setAttribute("sessionKey", "sessionValue");
        } else {
            System.out.println(session.getAttribute("sessionKey"));
        }
        Map map = new HashMap();
        map.put("result", "session");
        return map;
    }

}
