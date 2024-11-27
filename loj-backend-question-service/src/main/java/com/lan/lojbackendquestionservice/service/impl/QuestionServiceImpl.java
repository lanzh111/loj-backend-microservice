package com.lan.lojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lan.lojbackendclientservice.mapper.QuestionMapper;
import com.lan.lojbackendclientservice.service.QuestionService;
import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.constant.QuestionConstant;
import com.lan.lojbackendcommonservice.constant.UserConstant;
import com.lan.lojbackendcommonservice.dto.question.QuestionQueryRequest;
import com.lan.lojbackendcommonservice.exception.BusinessException;
import com.lan.lojbackendmodelservice.entity.Question;
import com.lan.lojbackendmodelservice.vo.QuestionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author pytho
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2024-10-18 20:15:00
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Override
    public void validQuestion(Question question, boolean b) {

        if (question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //更新校验
        if (!b){
            String tags = question.getTags();
            if (StringUtils.isNotBlank(tags) &&
                    tags.length()> QuestionConstant.TAGS_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"tags不合法");
            }
        }
        String name = question.getName();
        if (StringUtils.isBlank(name) ||
                name.length()>QuestionConstant.NAME_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"name不合法");
        }
        String content = question.getContent();
        if (StringUtils.isBlank(content) ||
                content.length()>QuestionConstant.CONTENT_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"content不合法");
        }
        String judgeCase = question.getJudgeCase();
        if (StringUtils.isBlank(judgeCase) ||
                judgeCase.length()>QuestionConstant.JUDGE_CASE_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"judgeCase不合法");
        }
        String judgeConfig = question.getJudgeConfig();
        if (StringUtils.isBlank(judgeConfig) ||
                judgeConfig.length()>QuestionConstant.JUDGE_CONFIG_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"judgeConfig不合法");
        }
        String answer = question.getAnswer();
        if (StringUtils.isBlank(answer) ||
                answer.length()>QuestionConstant.ANSWER_MAX_LEN){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"answer不合法");
        }

    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {


        //脱敏数据
        return Question.toVo(question);
    }


    @Override
    public LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest, boolean isAdmin) {
        if (questionQueryRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //动态查询
        LambdaQueryWrapper<Question> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        Long id = questionQueryRequest.getId();
        if (id!=null){
            lambdaQueryWrapper.eq(Question::getId,id);
        }
        String name = questionQueryRequest.getName();
        if (StringUtils.isNotBlank(name)){
            if (name.length()>QuestionConstant.NAME_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"name不合法");
            }
            lambdaQueryWrapper.like(Question::getName,name);
        }
        String content = questionQueryRequest.getContent();
        if (StringUtils.isNotBlank(content)){
            if (content.length()>QuestionConstant.CONTENT_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"content不合法");
            }
            lambdaQueryWrapper.like(Question::getContent,content);
        }
        String tags = questionQueryRequest.getTags();
        if (StringUtils.isNotBlank(tags)){
            if (tags.length()>QuestionConstant.TAGS_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"tags不合法");
            }
            lambdaQueryWrapper.like(Question::getTags,tags);
        }
        String answer = questionQueryRequest.getAnswer();
        if (StringUtils.isNotBlank(answer)){
            if (answer.length()>QuestionConstant.ANSWER_MAX_LEN){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"answer不合法");
            }
            lambdaQueryWrapper.like(Question::getAnswer,answer);
        }
        String searchText = questionQueryRequest.getSearchText();
        if (StringUtils.isNotBlank(searchText)) {
            if (searchText.length() > QuestionConstant.SEARCH_TEXT_MAX_LEN) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "searchText不合法");
            }
            if (isAdmin) {
                lambdaQueryWrapper.like(Question::getContent, searchText).or().
                        like(Question::getAnswer, searchText).or().
                        like(Question::getTags, searchText).or().
                        like(Question::getName, searchText);
            } else {
                lambdaQueryWrapper.like(Question::getContent, searchText).or().
                        like(Question::getId, searchText).or().
                        like(Question::getContent, searchText).or().
                        like(Question::getName, searchText);
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {

        //校验
        if (questionPage==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //数据脱敏
        List<Question> records = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>();

        List<QuestionVO> questionVOS = records.stream().map(Question::toVo).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOS);
        questionVOPage.setTotal(questionPage.getTotal());
        questionVOPage.setSize(questionPage.getSize());
        questionVOPage.setCurrent(questionPage.getCurrent());

        return questionVOPage;
    }

    private void isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUser==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

}




