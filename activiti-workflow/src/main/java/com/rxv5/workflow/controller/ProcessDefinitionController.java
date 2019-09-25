package com.rxv5.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.dao.ProcessDefinitionMapper;
import com.rxv5.workflow.vo.ProcessDefinitionVo;

/**
 * 流程定义
 * @author rxv5
 */
@Controller
@RequestMapping("/workflow/pd")
public class ProcessDefinitionController {
	
	@Autowired
	private ProcessDefinitionMapper mapper;

	@RequestMapping(value="/find")
	public String find(@RequestBody(required = false) ProcessDefinitionVo search, @RequestParam(name="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(name="pageSize",defaultValue = "10") Integer pageSize, Model model){
		PageHelper.startPage(pageNum, pageSize);
		List<ProcessDefinitionVo> list = mapper.select(search);
		PageInfo<ProcessDefinitionVo> page = new PageInfo<ProcessDefinitionVo>(list);
		model.addAttribute("page", page);
		return "/workflow/pd/list";
	}
}
