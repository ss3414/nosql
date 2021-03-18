package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mapper.UserMapper;
import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    /* fixme 失败 */
    @ResponseBody
    @RequestMapping("/elasticsearch")
    public Map<String, Object> elasticsearch() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<User> userList = userMapper.selectList(new QueryWrapper<>());
        userRepository.saveAll(userList);
        Pageable pageable = PageRequest.of(0, 10);
        List<User> documentList = userRepository.findAll(pageable).toList();
        map.put("documentList", documentList);
        return map;
    }

}
