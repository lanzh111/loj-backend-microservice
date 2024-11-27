package com.lan.lojbackendmodelservice.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;


@Data
public class SubmitQuestionRecordVo {


    /**
     * id
     */
    private Long id;


    /**
     * 编程语言 枚举值
     */
    private Integer language;


    /**
     * 结果状态
     */
    private String resultStates;

    /**
     * 结果信息
     */
    private String resultInfo;

    /**
     *
     * 执行时间
     */
    private Long time;

    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 创建时间
     */
    private Date createTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
