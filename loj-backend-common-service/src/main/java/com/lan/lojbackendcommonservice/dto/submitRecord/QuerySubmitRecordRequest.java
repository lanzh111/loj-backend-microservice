package com.lan.lojbackendcommonservice.dto.submitRecord;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lan.lojbackendcommonservice.common.PageRequest;
import lombok.Data;

import java.io.Serializable;


/**
 * 题目提交记录查询请求
 */
@Data
public class QuerySubmitRecordRequest  extends PageRequest implements Serializable{

    /**
     * id
     */
    private Long id;

    /**
     * 代码
     */
    private String code;

    /**
     * 编程语言 枚举值
     */
    private Integer language;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;

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
