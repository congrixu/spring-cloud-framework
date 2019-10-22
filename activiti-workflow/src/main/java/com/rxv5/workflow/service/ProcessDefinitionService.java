package com.rxv5.workflow.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rxv5.workflow.dao.PDConfigGroupMapper;
import com.rxv5.workflow.dao.PDConfigUserMapper;
import com.rxv5.workflow.vo.ActivitiVo;
import com.rxv5.workflow.vo.PDGroupConfigVo;
import com.rxv5.workflow.vo.PDUserConfigVo;
import com.rxv5.workflow.vo.PDUserGroupConfigVo;

@Service
public class ProcessDefinitionService extends WorkflowService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PDConfigUserMapper configUserMapper;

    @Autowired
    private PDConfigGroupMapper configGroupMapper;

    public boolean deploy(MultipartFile file) {
        boolean result = false;
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        ZipInputStream zipInputStream = null;
        try {
            if ("ZIP".equals(fileType) || "BAR".equals(fileType)) {
                zipInputStream = new ZipInputStream(file.getInputStream());
                // 部署流程定义文件
                getRepositoryService().createDeployment().addZipInputStream(zipInputStream).deploy();
                result = true;
            } else if ("BPMN".equals(fileType)) {
                getRepositoryService().createDeployment().addInputStream(fileName, file.getInputStream()).deploy();
                result = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (zipInputStream != null)
                    zipInputStream.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 查询流程定义所有节点信息
     * 
     * @param processDefinitionId
     * @return
     */
    public List<ActivitiVo> findProcessDefinitionBPMN(String processDefinitionId) {
        List<ActivitiVo> result = new ArrayList<ActivitiVo>();
        BpmnModel model = getRepositoryService().getBpmnModel(processDefinitionId);

        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                String className = e.getClass().toString();
                String type = className.substring(className.lastIndexOf(".") + 1);

                ActivitiVo vo = new ActivitiVo();
                vo.setId(e.getId());
                vo.setName(e.getName());
                vo.setType(type);
                if ("UserTask".equals(type)) {
                    Map<String, String> uc = getUserInfo(processDefinitionId, e.getId());
                    vo.setUserIds(uc.get("userIds"));
                    vo.setUserNames(uc.get("userNames"));

                    Map<String, String> gc = getGroupInfo(processDefinitionId, e.getId());
                    vo.setGroupIds(gc.get("groupIds"));
                    vo.setGroupNames(gc.get("groupNames"));
                }
                result.add(vo);
            }
        }
        return result;
    }

    private Map<String, String> getUserInfo(String processDefinitionId, String bpmnId) {
        Map<String, String> result = new HashMap<String, String>();
        List<PDUserConfigVo> list = configUserMapper.select(processDefinitionId, bpmnId);
        if (list != null && list.size() > 0) {
            StringBuffer ids = new StringBuffer();
            StringBuffer names = new StringBuffer();
            for (PDUserConfigVo uc : list) {
                ids.append(uc.getUserId()).append(",");
                names.append(uc.getUserName()).append(",");
            }
            result.put("userIds", ids.toString());
            result.put("userNames", names.toString());
        }
        return result;
    }

    private Map<String, String> getGroupInfo(String processDefinitionId, String bpmnId) {
        Map<String, String> result = new HashMap<String, String>();
        List<PDGroupConfigVo> list = configGroupMapper.select(processDefinitionId, bpmnId);
        if (list != null && list.size() > 0) {
            StringBuffer ids = new StringBuffer();
            StringBuffer names = new StringBuffer();
            for (PDGroupConfigVo uc : list) {
                ids.append(uc.getGroupId()).append(",");
                names.append(uc.getGroupName()).append(",");
            }
            result.put("groupIds", ids.toString());
            result.put("groupNames", names.toString());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBpmnConfigUserGroup(String processDefinitionId, List<PDUserGroupConfigVo> userGroupConfig)
            throws Exception {

        configGroupMapper.deleteTaskGroupConfigByProcessDefinitionId(processDefinitionId);
        configUserMapper.deleteTaskUserConfigByProcessDefinitionId(processDefinitionId);
        for (PDUserGroupConfigVo config : userGroupConfig) {
            String bpmnId = config.getBpmnId();
            String userIds = config.getUserIds();
            String userNams = config.getUserNames();
            String groupIds = config.getGroupIds();
            String groupNames = config.getGroupNames();

            if (groupIds != null && groupIds.trim().length() > 0) {
                String[] groupIdArray = userIds.split(",");
                String[] groupNameArray = groupNames.split(",");
                for (int i = 0; i < groupIdArray.length; i++) {
                    PDGroupConfigVo groupConfig = new PDGroupConfigVo();
                    groupConfig.setBpmnId(bpmnId);
                    groupConfig.setProcessDefinitionId(processDefinitionId);
                    groupConfig.setGroupId(groupIdArray[i]);
                    groupConfig.setGroupName(groupNameArray[i]);
                    configGroupMapper.save(groupConfig);
                }

            }

            if (userIds != null && userIds.trim().length() > 0) {
                String[] userIdArray = userIds.split(",");
                String[] userNameArray = userNams.split(",");
                for (int i = 0; i < userIdArray.length; i++) {
                    PDUserConfigVo userConfig = new PDUserConfigVo();
                    userConfig.setBpmnId(bpmnId);
                    userConfig.setProcessDefinitionId(processDefinitionId);
                    userConfig.setUserId(userIdArray[i]);
                    userConfig.setUserName(userNameArray[i]);
                    configUserMapper.save(userConfig);
                }
            }
        }
    }
}
