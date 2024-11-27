package com.lan.lojbackendmodelservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lan.lojbackendmodelservice.vo.QuestionVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目表
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 测试代码
     */
    private String testCode;

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

    /**
     * 转Vo
     * @return
     */
    public static QuestionVO toVo(Question question){
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question,questionVO);
        return questionVO;
    }


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}