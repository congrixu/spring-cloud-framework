package com.rxv5.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.dao.GroupMapper;
import com.rxv5.workflow.entity.Group;

@Controller
@RequestMapping("/workflow/group")
public class GroupControler extends BaseController {

    @Autowired
    private GroupMapper mapper;

    @RequestMapping("/to-choose")
    public String toChoose() {
        return "/workflow/group/choose";
    }

    /**
     * 用户选择
     * 
     * @return
     */
    @RequestMapping("/choose")
    public String choose(@RequestParam(required = false) String groupId,
            @RequestParam(required = false) String groupName,
            @RequestParam(name = "mutl", defaultValue = "true") boolean mutl,
            @RequestParam(name = "pageNum", defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<Group> groups = mapper.select(groupId, groupName);
        PageInfo<Group> page = new PageInfo<Group>(groups);
        model.addAttribute("mutl", mutl);
        model.addAttribute("groups", page);
        model.addAttribute("groupId", groupId);
        model.addAttribute("groupName", groupName);
        return "/workflow/group/choose-list";
    }

}
