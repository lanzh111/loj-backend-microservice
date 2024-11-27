package com.lan.lojbackendmodelservice.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 返回脱敏数据
 */
@Data
public class UpdateQuestionVO implements Serializable {


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
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json 数组）
     */
    private JudgeConfig judgeConfig;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 测试代码
     *
     */
    private String testCode;





    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
