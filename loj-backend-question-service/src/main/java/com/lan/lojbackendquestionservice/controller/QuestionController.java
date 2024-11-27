package com.lan.lojbackendquestionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lan.lojbackendclientservice.service.QuestionService;
import com.lan.lojbackendclientservice.service.SubmitRecordService;
import com.lan.lojbackendclientservice.service.inner.UserServiceInner;
import com.lan.lojbackendcommonservice.common.BaseResponse;
import com.lan.lojbackendcommonservice.common.DeleteRequest;
import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.common.ResultUtils;
import com.lan.lojbackendcommonservice.constant.JudgeConstants;
import com.lan.lojbackendcommonservice.constant.UserConstant;
import com.lan.lojbackendcommonservice.dto.enums.CodeLanguageEnum;
import com.lan.lojbackendcommonservice.dto.enums.TaskStatusEnum;
import com.lan.lojbackendcommonservice.dto.question.QuestionAddRequest;
import com.lan.lojbackendcommonservice.dto.question.QuestionQueryRequest;
import com.lan.lojbackendcommonservice.dto.question.QuestionUpdateRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.QueryRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.QuerySubmitRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.SubmitCodeRequest;
import com.lan.lojbackendcommonservice.exception.BusinessException;
import com.lan.lojbackendcommonservice.exception.ThrowUtils;
import com.lan.lojbackendmodelservice.entity.Question;
import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import com.lan.lojbackendmodelservice.entity.User;
import com.lan.lojbackendmodelservice.vo.*;
import com.lan.lojbackendquestionservice.amqp.JudgeProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 题目相关
 *
 */
@RestController
@RequestMapping("/")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserServiceInner userService;


    @Resource
    private SubmitRecordService submitRecordService;

    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        String tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(tags);
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase.size()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"判题用例不为空");
        }
        //judgeCase转json
        String judgeCaseStr = JudgeCase.listToStr(judgeCase);
        question.setJudgeCase(judgeCaseStr);
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();


        if (judgeConfig==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"判题配置不为空");
        }
        //judgeConfig转json
        String judgeConfigStr = JudgeConfig.objToStr(judgeConfig);
        question.setJudgeConfig(judgeConfigStr);

        questionService.validQuestion(question, true);
        User loginUser = userService.getLoginUser();
        question.setUserId(loginUser.getId());
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser();
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute(UserConstant.USER_LOGIN_STATE);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        String tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(tags);
        }
        //判题用例转json
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase!=null){
            question.setJudgeCase(JudgeCase.listToStr(judgeCase));
        }
        //判题配置转json
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig!=null){
            question.setJudgeConfig(JudgeConfig.objToStr(judgeConfig));
        }

        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/origin")
    public BaseResponse<UpdateQuestionVO> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UpdateQuestionVO updateQuestionVO = new UpdateQuestionVO();
        BeanUtils.copyProperties(question,updateQuestionVO);
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        List<JudgeCase> judgeCases = JudgeCase.strToList(judgeCase);
        JudgeConfig config = JudgeConfig.strToObj(judgeConfig);
        updateQuestionVO.setJudgeCase(judgeCases);
        updateQuestionVO.setJudgeConfig(config);
        return ResultUtils.success(updateQuestionVO);
    }
    /**
     * 分页获取列表（仅管理员）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest,true));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest,false));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser();
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest,false));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    // endregion


    @Resource
    private JudgeProducer judgeProducer;
    /**
     * 代码提交
     * @param submitCodeRequest
     * @param request
     * @return
     */
    @PostMapping("/code/submit")
    public BaseResponse<Long> doSubmit(@RequestBody SubmitCodeRequest submitCodeRequest,
                                       HttpServletRequest request) {
        Long questionId = submitCodeRequest.getQuestionId();
        if (submitCodeRequest == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //校验编程语言
        CodeLanguageEnum codeLanguageEnum = CodeLanguageEnum.getEnumByValue(submitCodeRequest.getLanguage());
        if (codeLanguageEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言不合法");
        }

        // 检查是否登录
         User  loginUser = userService.getLoginUser();
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //题目是否存在

        Question question = questionService.getById(questionId);
        if (question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目不存在");
        }

        //保存提交代码
        SubmitRecord submitRecord = new SubmitRecord();
        submitRecord.setTaskStatus(TaskStatusEnum.PENDING.getValue());
        submitRecord.setTaskInfo(TaskStatusEnum.PENDING.getText());
        submitRecord.setUserId(loginUser.getId());
        BeanUtils.copyProperties(submitCodeRequest,submitRecord);
        submitRecordService.save(submitRecord);

        //异步调用判题服务


        //解决openFeign异步调用上下文丢失
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Long id = submitRecord.getId();
        judgeProducer.send(String.valueOf(id), JudgeConstants.EXCHANGE_NAME,JudgeConstants.ROUTING_KEY);

//        CompletableFuture.runAsync(()->{
//            RequestContextHolder.setRequestAttributes(requestAttributes);
//            judgeService.doJudge(submitRecord.getId());
//        }).whenComplete((result, exception) -> {
//            if (exception!= null) {
//                System.err.println("异步调用出现异常: " + exception.getMessage());
//            }
//        });;

        return ResultUtils.success(submitRecord.getId());
    }

    /**
     * 分页搜索（管理员）
     * @return
     */
    @PostMapping("/code/record/page")
    public BaseResponse<Page<SubmitRecord>> getSubmitRecordByPage(@RequestBody QuerySubmitRecordRequest querySubmitRecordRequest){
        long current = querySubmitRecordRequest.getCurrent();
        long size = querySubmitRecordRequest.getPageSize();
        Page<SubmitRecord> submitRecordPage = submitRecordService.page(new Page<>(current, size),
                submitRecordService.getQueryWrapper(querySubmitRecordRequest));
        return ResultUtils.success(submitRecordPage);

    }

    /**
     * 分页搜索(用户)
     * @return
     */
    @PostMapping("/code/record/page/vo")
    public BaseResponse<Page<SubmitRecordVo>> getSubmitRecordVoByPage(@RequestBody QuerySubmitRecordRequest querySubmitRecordRequest){
        if (querySubmitRecordRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = querySubmitRecordRequest.getCurrent();
        long size = querySubmitRecordRequest.getPageSize();
        Page<SubmitRecord> submitRecordPage = submitRecordService.page(new Page<>(current, size),
                submitRecordService.getQueryWrapper(querySubmitRecordRequest));
        return ResultUtils.success(submitRecordService.getSummitRecordVo(submitRecordPage));

    }

    /**
     * 题目提交记录查询
     */
    @PostMapping("/code/record/query")
    public BaseResponse<Page<SubmitQuestionRecordVo>> querySubmitRecord(@RequestBody QueryRecordRequest queryRecordRequest, HttpServletRequest request) {
        if(queryRecordRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断是否登录
        User loginUser = userService.getLoginUser();
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //题目是否存在
        Long questionId = queryRecordRequest.getQuestionId();
        if (questionId==null || questionId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目不存在");
        }
        Question question = questionService.getById(questionId);
        if (question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目不存在");
        }

        //查询
        Page<SubmitQuestionRecordVo> questionRecordVoPage = submitRecordService.listSubmitRecordByPage(queryRecordRequest, loginUser);


        return  ResultUtils.success(questionRecordVoPage);
    }


}
