package com.lan.lojbackendmodelservice.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 提交记录脱敏
 */
@Data
public class SubmitRecordVo implements Serializable {
    /**
     * id
     */
    private Long id;


    /**
     * 编程语言 枚举值
     */
    private Integer language;

    /**
     * 执行信息json
     * {
     *     time : 执行时间
     *     memory: 消耗内存
     *     message : 执行信息
     * }
     */
    private String execInfo;

    /**
     * 结果状态
     */
    private String resultStates;


    /**
     * 任务状态 排队中 0、判断中 1、判题失败 2、判题成功 3
     */
    private Integer taskStatus;

    /**
     * 任务信息
     */
    private String taskInfo;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
