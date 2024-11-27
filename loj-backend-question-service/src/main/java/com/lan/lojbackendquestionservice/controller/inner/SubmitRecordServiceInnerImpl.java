package com.lan.lojbackendquestionservice.controller.inner;

import com.lan.lojbackendclientservice.service.SubmitRecordService;
import com.lan.lojbackendclientservice.service.inner.SubmitRecordServiceInner;
import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 * submitRecord内部接口调用的实现
*/
@RestController
@RequestMapping("/inner")
public class SubmitRecordServiceInnerImpl implements SubmitRecordServiceInner {


    @Resource
    private SubmitRecordService submitRecordService;


    @Override
    @GetMapping("/submit/record/get")
    public SubmitRecord getById(@RequestParam("id")Long id) {
        return submitRecordService.getById(id);
    }

    @Override
    @PostMapping("/submit/record/update")
    public boolean updateById(SubmitRecord submitRecord) {
        return submitRecordService.updateById(submitRecord);
    }
}




