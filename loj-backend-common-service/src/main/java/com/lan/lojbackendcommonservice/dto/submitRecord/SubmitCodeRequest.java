package com.lan.lojbackendcommonservice.dto.submitRecord;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;


/**
 * 代码提交
 */
@Data
public class SubmitCodeRequest implements Serializable {


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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
