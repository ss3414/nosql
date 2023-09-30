package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class IndexController {

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @RequestMapping("/session")
    public Map session(HttpSession session) {
        if (session.getAttribute("sessionKey") == null) {
            session.setAttribute("sessionKey", "sessionValue");
        } else {
            System.out.println(session.getAttribute("sessionKey"));
        }
        Map map = new LinkedHashMap();
        map.put("result", "session");
        return map;
    }

}
