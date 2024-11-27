package com.lan.lojbackendclientservice.service.inner;


import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
* @author pytho
* @description 针对表【submit_record(提交记录表)】的数据库操作Service
* @createDate 2024-10-19 20:55:13
*/
@RestController
@FeignClient(name = "loj-backend-question-service")
public interface SubmitRecordServiceInner{

    @GetMapping("/api/question/inner/submit/record/get")
    SubmitRecord getById(@RequestParam("id") Long id);


    @PostMapping("/api/question/inner/submit/record/update")
    boolean updateById(@RequestBody SubmitRecord submitRecord);
}
