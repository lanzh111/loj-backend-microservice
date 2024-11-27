package com.lan.lojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lan.lojbackendclientservice.mapper.SubmitRecordMapper;
import com.lan.lojbackendclientservice.service.SubmitRecordService;
import com.lan.lojbackendclientservice.service.inner.QuestionServiceInner;
import com.lan.lojbackendclientservice.service.inner.UserServiceInner;
import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.constant.CodeSubmitConstant;
import com.lan.lojbackendcommonservice.dto.submitRecord.QueryRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.QuerySubmitRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.SubmitCodeRequest;
import com.lan.lojbackendcommonservice.dto.enums.CodeLanguageEnum;
import com.lan.lojbackendcommonservice.dto.enums.ResultStatusEnum;
import com.lan.lojbackendcommonservice.dto.enums.TaskStatusEnum;
import com.lan.lojbackendcommonservice.exception.BusinessException;
import com.lan.lojbackendmodelservice.entity.Question;
import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import com.lan.lojbackendmodelservice.entity.User;
import com.lan.lojbackendmodelservice.vo.ExecInfo;
import com.lan.lojbackendmodelservice.vo.SubmitQuestionRecordVo;
import com.lan.lojbackendmodelservice.vo.SubmitRecordVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author pytho
* @description 针对表【submit_record(提交记录表)】的数据库操作Service实现
* @createDate 2024-10-19 20:55:13
*/
@Service
public class SubmitRecordServiceImpl extends ServiceImpl<SubmitRecordMapper, SubmitRecord>
    implements SubmitRecordService {


    @Resource
    private QuestionServiceInner questionService;

    @Resource
    private UserServiceInner userService;


    @Override
    public LambdaQueryWrapper<SubmitRecord> getQueryWrapper(QuerySubmitRecordRequest querySubmitRecordRequest) {
        if (querySubmitRecordRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        //动态查询
        LambdaQueryWrapper<SubmitRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        Long id = querySubmitRecordRequest.getId();
        if (id!=null){
            if (id<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"id不合法");
            }
            lambdaQueryWrapper.eq(SubmitRecord::getId,id);
        }
        String code = querySubmitRecordRequest.getCode();
        if (StringUtils.isNotBlank(code)){
            if (code.length()> CodeSubmitConstant.CODE_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"code不合法");
            }
            lambdaQueryWrapper.like(SubmitRecord::getCode,code);
        }
        Integer language = querySubmitRecordRequest.getLanguage();
        if (language!=null){
            CodeLanguageEnum codeLanguageEnum = CodeLanguageEnum.getEnumByValue(language);
            if (codeLanguageEnum==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"language不合法");
            }
            lambdaQueryWrapper.eq(SubmitRecord::getLanguage,language);
        }

        Long questionId = querySubmitRecordRequest.getQuestionId();
        if (questionId!=null){
            if (questionId<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"questionId不合法");
            }
            Question question = questionService.getById(questionId);
            if (question==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目不存在");
            }
            lambdaQueryWrapper.eq(SubmitRecord::getQuestionId,questionId);
        }
        Long userId = querySubmitRecordRequest.getUserId();
        if (userId!=null){
            if (userId<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"userId不合法");
            }

            User user = userService.getById(userId);
            if (user==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
            }
            lambdaQueryWrapper.eq(SubmitRecord::getUserId,userId);
        }

        String resultStates = querySubmitRecordRequest.getResultStates();
        if (StringUtils.isNotBlank(resultStates)){
            ResultStatusEnum statusEnum = ResultStatusEnum.getEnumByValue(resultStates);
            if (statusEnum==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"resultStates不合法");
            }
            lambdaQueryWrapper.eq(SubmitRecord::getResultStates,resultStates);
        }

        Integer taskStatus = querySubmitRecordRequest.getTaskStatus();
        if (taskStatus!=null){
            TaskStatusEnum taskStatusEnum = TaskStatusEnum.getEnumByValue(taskStatus);
            if (taskStatusEnum==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"任务状态不合法");
            }

            lambdaQueryWrapper.eq(SubmitRecord::getTaskStatus,taskStatus);
        }
        String taskInfo = querySubmitRecordRequest.getTaskInfo();
        if (StringUtils.isNotBlank(taskInfo)){
            lambdaQueryWrapper.eq(SubmitRecord::getTaskInfo,taskInfo);
        }

        return lambdaQueryWrapper;
    }

    @Override
    public Page<SubmitRecordVo> getSummitRecordVo(Page<SubmitRecord> submitRecordPage) {
        //Page<SubmitRecord>转Page<SubmitRecordVo>
        Page<SubmitRecordVo> submitRecordVoPage = new Page<>();
        BeanUtils.copyProperties(submitRecordPage, submitRecordVoPage);
        List<SubmitRecord> records = submitRecordPage.getRecords();
        List<SubmitRecordVo> recordVos = records.stream().map(submitRecord -> {
            SubmitRecordVo submitRecordVo = new SubmitRecordVo();
            BeanUtils.copyProperties(submitRecord, submitRecordVo);
            return submitRecordVo;
        }).collect(Collectors.toList());
        submitRecordVoPage.setRecords(recordVos);

        return submitRecordVoPage;
    }

    @Override
    public SubmitCodeRequest valid(SubmitCodeRequest submitCodeRequest) {
        String code = submitCodeRequest.getCode();
        if (StringUtils.isBlank(code)||code.length()>CodeSubmitConstant.CODE_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"code不合法");
        }
        Integer language = submitCodeRequest.getLanguage();
        CodeLanguageEnum codeLanguageEnum = CodeLanguageEnum.getEnumByValue(language);
        if (codeLanguageEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"language不合法");
        }
        Long questionId = submitCodeRequest.getQuestionId();
        if (questionId==null || questionId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"questionId不合法");
        }
        Question question = questionService.getById(questionId);
        if (question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"questionId不存在");
        }

        return submitCodeRequest;
    }

    @Override
    public SubmitRecord getSummitRecord(SubmitCodeRequest submitCodeRequest,long userId) {
        SubmitCodeRequest codeRequest = valid(submitCodeRequest);
        SubmitRecord submitRecord = new SubmitRecord();
        submitRecord.setCode(codeRequest.getCode());
        submitRecord.setLanguage(codeRequest.getLanguage());
        submitRecord.setQuestionId(codeRequest.getQuestionId());
        submitRecord.setUserId(userId);
        submitRecord.setTaskStatus(TaskStatusEnum.PENDING.getValue());
        submitRecord.setTaskInfo(TaskStatusEnum.PENDING.getText());
        return null;
    }

    @Override
    public Page<SubmitQuestionRecordVo> listSubmitRecordByPage(QueryRecordRequest queryRecordRequest, User loginUser) {

        //构造查询条件
        Long questionId = queryRecordRequest.getQuestionId();
        int current = queryRecordRequest.getCurrent();
        int pageSize = queryRecordRequest.getPageSize();
        Integer language = queryRecordRequest.getLanguage();
        String resultStates = queryRecordRequest.getResultStates();
        CodeLanguageEnum codeLanguageEnum = CodeLanguageEnum.getEnumByValue(language);

        LambdaQueryWrapper<SubmitRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (codeLanguageEnum!=null){
            lambdaQueryWrapper.eq(SubmitRecord::getLanguage,language);
        }

        if (StringUtils.isNotBlank(resultStates)){
            lambdaQueryWrapper.eq(SubmitRecord::getResultStates,resultStates);
        }

        lambdaQueryWrapper.eq(SubmitRecord::getUserId, loginUser.getId());
        lambdaQueryWrapper.eq(SubmitRecord::getQuestionId, questionId);
        lambdaQueryWrapper.orderByDesc(SubmitRecord::getCreateTime);
        //根据查询条件查询
        Page<SubmitRecord> submitRecordPage = this.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        List<SubmitRecord> submitRecordList = submitRecordPage.getRecords();
        //处理返回结果
        Page<SubmitQuestionRecordVo> questionRecordVoPage=new Page<>();

        List<SubmitQuestionRecordVo> submitQuestionRecordVos = submitRecordList.stream().map((submitRecord) -> {
            SubmitQuestionRecordVo submitQuestionRecordVo = new SubmitQuestionRecordVo();
            BeanUtils.copyProperties(submitRecord, submitQuestionRecordVo);

            String execInfo = submitRecord.getExecInfo();
            if (StringUtils.isNotBlank(execInfo)){
                ExecInfo execInfoObj = ExecInfo.strToObj(execInfo);
                submitQuestionRecordVo.setTime(execInfoObj.getTime());
                submitQuestionRecordVo.setMemory(execInfoObj.getMemory());
            }
            return submitQuestionRecordVo;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(submitRecordPage,questionRecordVoPage);
        questionRecordVoPage.setRecords(submitQuestionRecordVos);

        return questionRecordVoPage;
    }

}




