package com.rxv5.workflow.service;

import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProcessDefinitionService extends WorkflowService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public boolean deploy(MultipartFile file) {
        boolean result = false;
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1)
                .toUpperCase();
        ZipInputStream zipInputStream = null;
        try {
            if ("ZIP".equals(fileType) || "BAR".equals(fileType)) {
                zipInputStream = new ZipInputStream(file.getInputStream());
                // 部署流程定义文件
                getRepositoryService().createDeployment()
                        .addZipInputStream(zipInputStream).deploy();
                result = true;
            } else if ("BPMN".equals(fileType)) {
                getRepositoryService().createDeployment()
                        .addInputStream(fileName, file.getInputStream())
                        .deploy();
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
}
