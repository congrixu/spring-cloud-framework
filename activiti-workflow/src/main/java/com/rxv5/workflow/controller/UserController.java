package com.rxv5.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.dao.UserMapper;
import com.rxv5.workflow.entity.User;

@Controller
@RequestMapping("/workflow/user")
public class UserController extends BaseController {

    @Autowired
    private UserMapper mapper;

    @RequestMapping("/to-choose")
    public String toChoose() {
        return "/workflow/user/choose";
    }

    /**
     * 用户选择
     * 
     * @return
     */
    @RequestMapping("/choose")
    public String choose(@RequestParam(required = false) String userId, @RequestParam(required = false) String userName,

    @RequestParam(name = "mutl", defaultValue = "true") boolean mutl,
            @RequestParam(name = "pageNum", defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = mapper.select(userId, userName);
        PageInfo<User> page = new PageInfo<User>(users);
        model.addAttribute("mutl", mutl);
        model.addAttribute("users", page);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        return "/workflow/user/choose-list";
    }

}
