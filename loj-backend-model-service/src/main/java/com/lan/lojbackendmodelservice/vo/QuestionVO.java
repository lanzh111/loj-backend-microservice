package com.lan.lojbackendmodelservice.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回脱敏数据
 */
@Data
public class QuestionVO implements Serializable {

    private Long id;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目标签（json 数组）
     */
    private String tags;

    /**
     * 提交总数
     */
    private Integer totalNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
