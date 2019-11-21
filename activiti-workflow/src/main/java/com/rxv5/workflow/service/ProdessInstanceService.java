package com.rxv5.workflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rxv5.workflow.constant.WorkflowConstants;
import com.rxv5.workflow.dao.ProdessInstanceMapper;
import com.rxv5.workflow.vo.WorkflowProcesssInstanceVo;

@Service
public class ProdessInstanceService {
    @Autowired
    private ProdessInstanceMapper maper;

    public List<WorkflowProcesssInstanceVo> find(WorkflowProcesssInstanceVo search) {
        List<WorkflowProcesssInstanceVo> list = maper.select(search);
        if (list != null && list.size() > 0) {
            for (WorkflowProcesssInstanceVo instance : list) {
                instance.setStateStr(WorkflowConstants.WorkflowState.byId(instance.getState()).getText());
                instance.setIsSubProcesStr(WorkflowConstants.YesOrNo.byId(instance.getIsSubProcess()).getText());
            }
        }
        return list;
    }
}
