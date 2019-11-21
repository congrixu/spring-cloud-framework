package com.rxv5.workflow.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.service.ProdessInstanceService;
import com.rxv5.workflow.vo.WorkflowProcesssInstanceVo;

@Controller
@RequestMapping("/workflow/pi")
public class ProdessInstanceController extends BaseController {

    @Autowired
    private ProdessInstanceService service;

    @RequestMapping(value = "/query")
    public String query(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, Model model) {
        WorkflowProcesssInstanceVo search = getBean("search", WorkflowProcesssInstanceVo.class);
        PageHelper.startPage(pageNum, pageSize);
        List<WorkflowProcesssInstanceVo> list = service.find(search);
        PageInfo<WorkflowProcesssInstanceVo> page = new PageInfo<WorkflowProcesssInstanceVo>(list);
        model.addAttribute("search", search);
        model.addAttribute("page", page);
        return "/workflow/pi/list";
    }

    public static void main(String[] args) {
        List<WorkflowProcesssInstanceVo> tes = new ArrayList<WorkflowProcesssInstanceVo>();
        WorkflowProcesssInstanceVo a1 = new WorkflowProcesssInstanceVo();
        WorkflowProcesssInstanceVo a2 = new WorkflowProcesssInstanceVo();
        WorkflowProcesssInstanceVo a3 = new WorkflowProcesssInstanceVo();
        tes.add(a1);
        tes.add(a2);
        tes.add(a3);
        int i = 0;
        // tes.stream().forEach(t -> t.setApplUserId((++i) + ""));
        tes.stream().forEach(t -> {
            System.out.println(t.getApplUserId());
        });

    }
}
