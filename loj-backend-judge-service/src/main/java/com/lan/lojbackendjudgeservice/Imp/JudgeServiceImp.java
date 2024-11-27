package com.lan.lojbackendjudgeservice.Imp;


import com.lan.lojbackendclientservice.service.JudgeService;
import com.lan.lojbackendclientservice.service.inner.QuestionServiceInner;
import com.lan.lojbackendclientservice.service.inner.SubmitRecordServiceInner;
import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.dto.Judge.JudgeResponse;
import com.lan.lojbackendcommonservice.dto.enums.CodeLanguageEnum;
import com.lan.lojbackendcommonservice.dto.enums.ResultStatusEnum;
import com.lan.lojbackendcommonservice.dto.enums.TaskStatusEnum;
import com.lan.lojbackendcommonservice.exception.BusinessException;
import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteRequest;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
import com.lan.lojbackendjudgeservice.codeSandBox.model.Metrics;
import com.lan.lojbackendjudgeservice.factory.CodeSandBoxFactory;
import com.lan.lojbackendmodelservice.entity.Question;
import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import com.lan.lojbackendmodelservice.vo.ExecInfo;
import com.lan.lojbackendmodelservice.vo.JudgeCase;
import com.lan.lojbackendmodelservice.vo.JudgeConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class JudgeServiceImp implements JudgeService {

    @Resource
    private SubmitRecordServiceInner submitRecordService;

    @Resource
    private QuestionServiceInner questionService;


    @Value("${codeSandBox.type}")
    private String type;


    public void setType(String type) {
        this.type = type;
    }

    @Override
    public JudgeResponse doJudge(long id) {
        // 1）输入提交题目代码编号，查询题目相关的信息（输入和输出用例、代码、编程语言等信息）。
        SubmitRecord submitRecord = submitRecordService.getById(id);
        if (submitRecord==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目提交代码不存在");
        }
        Long questionId = submitRecord.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目不存在");
        }
        // 2）检查判题状态是否为 ”等待中“
        Integer taskStatus = submitRecord.getTaskStatus();
        if (!taskStatus.equals(TaskStatusEnum.PENDING.getValue())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"判题状态不合法,不能重复提交");
        }
        // 3）修改判题状态为 "判题中"
        SubmitRecord newSubmitRecord = new SubmitRecord();
        BeanUtils.copyProperties(submitRecord,newSubmitRecord);
        newSubmitRecord.setTaskStatus(TaskStatusEnum.JUDGING.getValue());
        newSubmitRecord.setTaskInfo(TaskStatusEnum.JUDGING.getText());
        boolean update = submitRecordService.updateById(newSubmitRecord);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        // 4）调用代码沙箱服务，返回执行结果。

        //补充代码沙箱请求参数
        Integer language = submitRecord.getLanguage();
        CodeLanguageEnum languageEnum = CodeLanguageEnum.getEnumByValue(language);
        String languageText = languageEnum.getText();
        String code = submitRecord.getCode();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JudgeCase.strToList(judgeCase);
        List<String> inputs =new ArrayList<>();
        for (JudgeCase judgeCaseItem : judgeCases) {
            String input = judgeCaseItem.getInput();
            inputs.add(input);
        }
        ExecuteRequest executeRequest = new ExecuteRequest();
        executeRequest.setCode(code);
        executeRequest.setTestCode(question.getTestCode());
        executeRequest.setLanguage(languageText);
        executeRequest.setInputList(inputs);
        //创建一个代码沙箱实例
        CodeSandBox codeSandBox = CodeSandBoxFactory.getInstance(type);
        //执行代码沙箱
        ExecuteResponse executeResponse = codeSandBox.ExecuteCode(executeRequest);

        JudgeResponse judgeResponse = new JudgeResponse();
        judgeResponse.setTaskId(submitRecord.getId());



        SubmitRecord finishSubmitRecord = new SubmitRecord();
        //5）对比预期结果和输出是否符合要求。
        ArrayList<String> expectOutputList = new ArrayList<>();
        List<String> resultOutputList = executeResponse.getOutputList();
        for (JudgeCase judgeCaseItem : judgeCases) {
            String output = judgeCaseItem.getOutput();
            expectOutputList.add(output);
        }
        //a.对比结果输出用例和预期输出数量是否相同。
        if (resultOutputList.size()!=expectOutputList.size()){
            // TODO: 2024/10/30  判题结果改为wrong answer
            String wrongAnswerValue = ResultStatusEnum.WRONG_ANSWER.getValue();
            setTaskStatus(newSubmitRecord,executeResponse,finishSubmitRecord,wrongAnswerValue,"");

            judgeResponse.setTaskStatus(TaskStatusEnum.JUDGE_FAILED.getValue());
            judgeResponse.setTaskInfo(TaskStatusEnum.JUDGE_FAILED.getText());
           return judgeResponse;
        }

        //b.对比结果的每一项输出用例与预期的输出用例是否一样。
        for (int i = 0; i < resultOutputList.size(); i++) {
            if (!expectOutputList.get(i).equals(resultOutputList.get(i))){
                // TODO: 2024/10/30  判题结果改为wrong answer
                String wrongAnswerValue = ResultStatusEnum.WRONG_ANSWER.getValue();
                setTaskStatus(newSubmitRecord,executeResponse,finishSubmitRecord,wrongAnswerValue,"");

                judgeResponse.setTaskStatus(TaskStatusEnum.JUDGE_FAILED.getValue());
                judgeResponse.setTaskInfo(TaskStatusEnum.JUDGE_FAILED.getText());
                return judgeResponse;
            }
        }
        //c.最后检查性能指标是否符合题目的要求。
        JudgeConfig judgeConfig = JudgeConfig.strToObj(question.getJudgeConfig());
        Metrics metrics = executeResponse.getMetrics();
        //获取实际执行的时间
        Long realExecTime = metrics.getTime();
        //获得题目要求的时间限制
        Integer needTimeLimit = judgeConfig.getTimeLimit();

        if (realExecTime>needTimeLimit){
            // TODO: 2024/10/30  判题结果改为 time limit exceeded
            String time_limit_exceededValue = ResultStatusEnum.TIME_LIMIT_EXCEEDED.getValue();
            setTaskStatus(newSubmitRecord,executeResponse,finishSubmitRecord,
                    time_limit_exceededValue,ResultStatusEnum.TIME_LIMIT_EXCEEDED.getText());

            judgeResponse.setTaskStatus(TaskStatusEnum.JUDGE_FAILED.getValue());
            judgeResponse.setTaskInfo(TaskStatusEnum.JUDGE_FAILED.getText());
            return judgeResponse;
        }
        Long realMemory = metrics.getMemory();
//        //获得题目要求的内存限制
        Integer needMemoryLimit = judgeConfig.getMemoryLimit();
        if (realMemory>needMemoryLimit){
            // TODO: 2024/10/30  判题结果改为 memory limit exceeded
            String memoryLimitExceededValue = ResultStatusEnum.MEMORY_LIMIT_EXCEEDED.getValue();
            setTaskStatus(newSubmitRecord,executeResponse,finishSubmitRecord,
                    memoryLimitExceededValue,ResultStatusEnum.MEMORY_LIMIT_EXCEEDED.getText());

            judgeResponse.setTaskStatus(TaskStatusEnum.JUDGE_FAILED.getValue());
            judgeResponse.setTaskInfo(TaskStatusEnum.JUDGE_FAILED.getText());
            return judgeResponse;
        }
        //获取程序实际占用的内存

        // 6）最后将判题状态修改为 "完成"

        BeanUtils.copyProperties(newSubmitRecord,finishSubmitRecord);
        String executeInfo = executeResponse.getExecuteInfo();
        ExecInfo execInfo = new ExecInfo();
        execInfo.setTime(realExecTime);
        execInfo.setMemory(realMemory);
        execInfo.setMessage(executeInfo);
        finishSubmitRecord.setTaskInfo(TaskStatusEnum.COMPLETED.getText());
        finishSubmitRecord.setTaskStatus(TaskStatusEnum.COMPLETED.getValue());
        finishSubmitRecord.setResultStates(ResultStatusEnum.ACCEPTED.getValue());
        finishSubmitRecord.setExecInfo(ExecInfo.objToStr(execInfo));
        update = submitRecordService.updateById(finishSubmitRecord);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }


        judgeResponse.setTaskStatus(TaskStatusEnum.COMPLETED.getValue());
        judgeResponse.setTaskInfo(TaskStatusEnum.PENDING.getText());
        return judgeResponse;
    }

    private void setTaskStatus(SubmitRecord newSubmitRecord,
                               ExecuteResponse executeResponse,
                               SubmitRecord finishSubmitRecord,
                               String resultStatus,String resultInfo) {

        BeanUtils.copyProperties(newSubmitRecord,finishSubmitRecord);
        String executeInfo = executeResponse.getExecuteInfo();
        ExecInfo execInfo = new ExecInfo();
        execInfo.setMessage(executeInfo);
        finishSubmitRecord.setTaskInfo(TaskStatusEnum.JUDGE_FAILED.getText());
        finishSubmitRecord.setTaskStatus(TaskStatusEnum.JUDGE_FAILED.getValue());
        finishSubmitRecord.setResultStates(resultStatus);
        finishSubmitRecord.setResultInfo(resultInfo);
        finishSubmitRecord.setExecInfo(ExecInfo.objToStr(execInfo));
        boolean  update = submitRecordService.updateById(finishSubmitRecord);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
