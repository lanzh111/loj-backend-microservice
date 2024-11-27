package com.lan.lojbackendclientservice.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lan.lojbackendcommonservice.dto.submitRecord.QueryRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.QuerySubmitRecordRequest;
import com.lan.lojbackendcommonservice.dto.submitRecord.SubmitCodeRequest;
import com.lan.lojbackendmodelservice.entity.SubmitRecord;
import com.lan.lojbackendmodelservice.entity.User;
import com.lan.lojbackendmodelservice.vo.SubmitQuestionRecordVo;
import com.lan.lojbackendmodelservice.vo.SubmitRecordVo;

/**
* @author pytho
* @description 针对表【submit_record(提交记录表)】的数据库操作Service
* @createDate 2024-10-19 20:55:13
*/
public interface SubmitRecordService extends IService<SubmitRecord> {

    LambdaQueryWrapper<SubmitRecord> getQueryWrapper(QuerySubmitRecordRequest querySubmitRecordRequest);

    Page<SubmitRecordVo> getSummitRecordVo(Page<SubmitRecord> submitRecordPage);

     SubmitCodeRequest valid(SubmitCodeRequest submitCodeRequest) ;

    SubmitRecord getSummitRecord(SubmitCodeRequest submitCodeRequest,long userId);

    Page<SubmitQuestionRecordVo> listSubmitRecordByPage(QueryRecordRequest queryRecordRequest, User loginUser);
}
