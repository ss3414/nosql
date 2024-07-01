package com.demo;

import com.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class ElasticSearch {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearch.class, args);
    }

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("/index");
    }

    @Autowired
    private ProductMapper productMapper;

    @RequestMapping("/test")
    public Map test() {
        return new LinkedHashMap();
    }

}
