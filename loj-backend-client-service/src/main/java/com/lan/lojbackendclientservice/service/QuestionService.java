package com.lan.lojbackendclientservice.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lan.lojbackendcommonservice.dto.question.QuestionQueryRequest;
import com.lan.lojbackendmodelservice.entity.Question;
import com.lan.lojbackendmodelservice.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;


/**
* @author pytho
* @description 针对表【question(题目表)】的数据库操作Service
* @createDate 2024-10-18 20:15:00
*/
public interface QuestionService extends IService<Question> {
    

    void validQuestion(Question question, boolean b);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest, boolean isAdmin);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
