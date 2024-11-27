package com.lan.lojbackendjudgeservice.codeSandBox.Imp;


import com.lan.lojbackendcommonservice.dto.enums.ExecuteStatusEnum;
import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteRequest;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
import com.lan.lojbackendjudgeservice.codeSandBox.model.Metrics;

import java.util.List;

/**
 * 实例代码沙箱
 * 最简单的代码沙箱
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteResponse ExecuteCode(ExecuteRequest executeRequest) {

        List<String> inputList = executeRequest.getInputList();
        ExecuteResponse executeResponse = new ExecuteResponse();
        executeResponse.setOutputList(inputList);
        executeResponse.setExecuteInfo(ExecuteStatusEnum.FINISH.getText());
        executeResponse.setExecuteStatus(ExecuteStatusEnum.FINISH.getValue());
        executeResponse.setMetrics(Metrics.builder().memory(1000L).time(100L).build());
        return executeResponse;
    }
}
