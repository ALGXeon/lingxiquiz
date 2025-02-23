package com.ALGXeon.lingxiquiz.service;

import com.ALGXeon.lingxiquiz.model.entity.AiUsage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dell
* @description 针对表【ai_usage】的数据库操作Service
* @createDate 2025-02-22 21:52:58
*/
public interface AiUsageService extends IService<AiUsage> {
    public abstract AiUsage getAiUsage(Long userId);

    public abstract boolean tryGetAndUse(Long userId);

    public abstract Integer getAvailableUses(Long userId);

}
