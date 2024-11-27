package com.lan.lojbackendclientservice.service.inner;


import com.lan.lojbackendcommonservice.dto.Judge.JudgeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 判题服务接口
 */
@RestController
@FeignClient(name = "loj-backend-judge-service")
public interface JudgeServiceInner {
    /**
     * 判题接口
     * @param id
     * @return
     */
    @PostMapping("/api/judge/inner/judge")
    JudgeResponse doJudge(long id);
}
