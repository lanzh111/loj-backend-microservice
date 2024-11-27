package com.lan.lojbackendcommonservice.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lan.lojbackendmodelservice.vo.JudgeCase;
import com.lan.lojbackendmodelservice.vo.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 题目更新请求
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
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
     */
    private String testCode;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
