package com.lan.lojbackendcommonservice.dto.Judge;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题响应结果
 */
@Data
public class JudgeResponse implements Serializable {
    private Long taskId;
    private Integer taskStatus;
    private String taskInfo;
}
