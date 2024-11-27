package com.lan.lojbackendclientservice.service;


import com.lan.lojbackendcommonservice.dto.Judge.JudgeResponse;

/**
 * 判题服务接口
 */
public interface JudgeService {
    /**
     * 判题接口
     * @param id
     * @return
     */
    JudgeResponse doJudge(long id);
}
