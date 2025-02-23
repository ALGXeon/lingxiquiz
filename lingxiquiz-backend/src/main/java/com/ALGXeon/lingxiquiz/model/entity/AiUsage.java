package com.ALGXeon.lingxiquiz.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName ai_usage
 */
@TableName(value ="ai_usage")
@Data
public class AiUsage {
    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 当前可用次数
     */
    private Integer availableUses;

    /**
     * 上次使用日期
     */
    private Date lastUsedDate;

    /**
     * 是否删除
     */
    private Integer isDelete;
}