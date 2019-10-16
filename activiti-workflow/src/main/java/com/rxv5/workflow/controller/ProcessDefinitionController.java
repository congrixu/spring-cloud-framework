package com.rxv5.workflow.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.IOUtils;
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
public class ProcessDefinitionController extends BaseController {

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

    /**
     * 查询XML或者流程图
     * 
     * @param deploymentId
     * @param resourceName
     *            xml名称(act_re_procdef.RESOURCE_NAME_)
     *            或者图片名称(act_re_procdef.DGRM_RESOURCE_NAME_)
     * @throws Exception
     */
    @GetMapping("/load-rsource")
    public void loadResource(@RequestParam String deploymentId,
            @RequestParam String resourceName) throws Exception {
        try {
            InputStream resourceAsStream = processDefinitionService
                    .getRepositoryService()
                    .getResourceAsStream(deploymentId, resourceName);
            byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
            ServletOutputStream servletOutputStream = response
                    .getOutputStream();
            servletOutputStream.write(byteArray, 0, byteArray.length);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
