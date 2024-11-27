package com.lan.lojbackendjudgeservice.codeSandBox.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 执行响应参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteResponse implements Serializable {
    private List<String> outputList;
    private String executeInfo;
    private Integer executeStatus;
    private Metrics metrics;
}
