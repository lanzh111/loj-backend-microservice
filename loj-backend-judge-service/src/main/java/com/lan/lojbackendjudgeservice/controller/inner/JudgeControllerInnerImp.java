package com.lan.lojbackendjudgeservice.controller.inner;


import com.lan.lojbackendclientservice.service.JudgeService;
import com.lan.lojbackendclientservice.service.inner.JudgeServiceInner;
import com.lan.lojbackendcommonservice.dto.Judge.JudgeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 判题服务内部调用实现
 */
@RestController
@RequestMapping("/inner")
public class JudgeControllerInnerImp implements JudgeServiceInner {


    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/judge")
    public JudgeResponse doJudge(long id) {
        return judgeService.doJudge(id);
    }
}
