package com.rxv5.workflow.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.dao.ProcessDefinitionMapper;
import com.rxv5.workflow.service.ProcessDefinitionService;
import com.rxv5.workflow.vo.JsonResult;
import com.rxv5.workflow.vo.ProcessDefinitionVo;

/**
 * 流程定义
 * 
 * @author rxv5
 */
@Controller
@RequestMapping("/workflow/pd")
public class ProcessDefinitionController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProcessDefinitionMapper mapper;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @RequestMapping(value = "/find")
    public String find(
            @RequestBody(required = false) ProcessDefinitionVo search,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            Model model) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProcessDefinitionVo> list = mapper.select(search);
        PageInfo<ProcessDefinitionVo> page = new PageInfo<ProcessDefinitionVo>(
                list);
        model.addAttribute("page", page);
        return "/workflow/pd/list";
    }

    @GetMapping("/uplod")
    public String toUpload() {
        return "/workflow/pd/upload";
    }

    @ResponseBody
    @PostMapping("/deploy")
    public JsonResult<Object> deploy(@RequestParam("file") MultipartFile file) {
        JsonResult<Object> result = new JsonResult<Object>();
        if (file.isEmpty()) {
            result.setSuccess(false);
            result.setMessage("上传失败，请选择文件");
            return result;
        }
        boolean bool = processDefinitionService.deploy(file);
        result.setSuccess(bool);
        return result;
    }

}
