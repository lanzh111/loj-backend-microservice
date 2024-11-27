package com.lan.lojbackendquestionservice.controller.inner;

import com.lan.lojbackendclientservice.service.QuestionService;
import com.lan.lojbackendclientservice.service.inner.QuestionServiceInner;
import com.lan.lojbackendmodelservice.entity.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * question内部接口实现
*/
@RestController
@RequestMapping("/inner")
public class QuestionServiceInnerImpl  implements QuestionServiceInner {

    @Resource
    private QuestionService questionService;

    @Override
    @GetMapping("/get")
    public Question getById(@RequestParam("id")Long id) {
        return questionService.getById(id);
    }
}




