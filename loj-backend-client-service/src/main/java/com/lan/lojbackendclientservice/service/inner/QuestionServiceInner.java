package com.lan.lojbackendclientservice.service.inner;


import com.lan.lojbackendmodelservice.entity.Question;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
* @author pytho
* @description 针对表【question(题目表)】的数据库操作Service
* @createDate 2024-10-18 20:15:00
*/
@RestController
@FeignClient(name = "loj-backend-question-service")
public interface QuestionServiceInner{

    @GetMapping("/api/question/inner/get")
    Question getById(@RequestParam("id") Long id);
}
