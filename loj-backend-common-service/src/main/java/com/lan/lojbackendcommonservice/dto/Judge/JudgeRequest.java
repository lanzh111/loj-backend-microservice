package com.lan.lojbackendcommonservice.dto.Judge;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题请求
 */
@Data
public class JudgeRequest implements Serializable {
    //提交题目记录id
    private Long id;
}
