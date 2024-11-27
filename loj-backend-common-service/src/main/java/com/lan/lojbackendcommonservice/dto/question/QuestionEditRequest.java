package com.lan.lojbackendcommonservice.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class QuestionEditRequest {

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
     * 判题用例（json 数组）
     */
    private String judgeCase;

    /**
     * 判题配置（json 数组）
     */
    private String judgeConfig;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 提交总数
     */
    private Integer totalNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 创建用户 id
     */
    private Long userId;

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
