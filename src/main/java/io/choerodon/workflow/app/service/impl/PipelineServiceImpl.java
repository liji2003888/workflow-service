package io.choerodon.workflow.app.service.impl;

import com.google.gson.Gson;

import io.choerodon.asgard.saga.annotation.Saga;
import io.choerodon.asgard.saga.producer.StartSagaBuilder;
import io.choerodon.asgard.saga.producer.TransactionalProducer;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.workflow.api.vo.DevopsPipelineVO;
import io.choerodon.workflow.app.service.PipelineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Sheep on 2019/5/16.
 */

@Service
public class PipelineServiceImpl implements PipelineService {

    private Gson gson = new Gson();

    @Autowired
    TransactionalProducer producer;

    @Override
    @Saga(code = "workflow-create-pipeline",
            description = "启动cd pipeline", inputSchema = "{}")
    public void beginDevopsPipelineSaga(DevopsPipelineVO devopsPipelineVO) {
        producer.apply(
                StartSagaBuilder
                        .newBuilder()
                        .withLevel(ResourceLevel.SITE)
                        .withRefType("workflow")
                        .withSagaCode("workflow-create-pipeline"),
                builder -> builder
                        .withPayloadAndSerialize(devopsPipelineVO)
                        .withRefId("1"));
    }

}
