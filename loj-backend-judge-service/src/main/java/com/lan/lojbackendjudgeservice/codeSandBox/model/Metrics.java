package com.lan.lojbackendjudgeservice.codeSandBox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 执行程序的性能指标
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metrics implements Serializable {
    private Long time;
    private Long memory;
}
