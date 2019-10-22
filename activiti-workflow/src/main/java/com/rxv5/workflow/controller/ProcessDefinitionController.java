package com.rxv5.workflow.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rxv5.workflow.dao.ProcessDefinitionMapper;
import com.rxv5.workflow.service.ProcessDefinitionService;
import com.rxv5.workflow.util.FastjsonUtil;
import com.rxv5.workflow.vo.ActivitiVo;
import com.rxv5.workflow.vo.JsonResult;
import com.rxv5.workflow.vo.PDUserGroupConfigVo;
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
    public String find(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, Model model) {
        ProcessDefinitionVo search = getBean("search", ProcessDefinitionVo.class);
        PageHelper.startPage(pageNum, pageSize);
        List<ProcessDefinitionVo> list = mapper.select(search);
        PageInfo<ProcessDefinitionVo> page = new PageInfo<ProcessDefinitionVo>(list);
        model.addAttribute("search", search);
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
    @ResponseBody // 不加些标注getOutputStream()会报错
    @GetMapping("/load-rsource")
    public void loadResource(@RequestParam String deploymentId, @RequestParam String resourceName) throws Exception {
        try {
            InputStream resourceAsStream = processDefinitionService.getRepositoryService()
                    .getResourceAsStream(deploymentId, resourceName);
            byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(byteArray, 0, byteArray.length);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 生成流成图片 （运行中流程图也可以通过ProcessDiagramGenerator实现 有待研究） TODO
     */
    @ResponseBody // 不加些标注getOutputStream()会报错
    @GetMapping("/load-rsource-img")
    public void loadResourceImg(@RequestParam String processDefinitionId) {
        try {
            BpmnModel model = processDefinitionService.getRepositoryService().getBpmnModel(processDefinitionId);

            ProcessDiagramGenerator ge = new DefaultProcessDiagramGenerator();
            InputStream resource = ge.generateDiagram(model, "宋体", "宋体", "宋体");
            byte[] byteArray = IOUtils.toByteArray(resource);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(byteArray, 0, byteArray.length);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 配置流程定义页面
     * 
     * @param processDefinitionId
     * @return
     */
    @RequestMapping("/to-config")
    public String config(@RequestParam String processDefinitionId, Model model) {
        model.addAttribute("processDefinitionId", processDefinitionId);
        return "/workflow/pd/config";
    }

    /**
     * 查询流程定义各节点信息
     * 
     * @param processDefinitionId
     * @return
     */
    @RequestMapping("/find-bpmn")
    public String findProcessDefinitionBPMN(@RequestParam String processDefinitionId, Model model) {
        List<ActivitiVo> list = processDefinitionService.findProcessDefinitionBPMN(processDefinitionId);
        model.addAttribute("bpmns", list);
        return "/workflow/pd/bpmn";
    }

    /**
     * 保存用户任务的人员\人员组配置信息
     * 
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/save-config-usergroup")
    public boolean saveDefinitionActivitisConfigUser(@RequestParam String processDefinitionId,
            @RequestParam String ugConfigJson) throws Exception {
        boolean bool = true;
        try {
            List<PDUserGroupConfigVo> userGroupConfig = FastjsonUtil.json2ObjList(ugConfigJson,
                    PDUserGroupConfigVo.class);
            processDefinitionService.saveBpmnConfigUserGroup(processDefinitionId, userGroupConfig);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            bool = false;
        }
        return bool;
    }

}
