package com.lan.lojbackendmodelservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交记录表
 * @TableName submit_record
 */
@TableName(value ="submit_record")
@Data
public class SubmitRecord implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 结果信息
     */
    private String resultInfo;



    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 任务状态 排队中 0、判断中 1、判题失败 2、判题成功 3
     */
    private Integer taskStatus;

    /**
     * 任务信息
     */
    private String taskInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}