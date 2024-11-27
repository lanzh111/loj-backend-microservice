package com.lan.lojbackendcommonservice.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lan.lojbackendcommonservice.common.PageRequest;
import lombok.Data;

import java.io.Serializable;


/**
 * 题目查询请求
 */
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {
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
     * 题目答案
     */
    private String answer;

    /**
     * 搜索内容
     */

    private String searchText;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
