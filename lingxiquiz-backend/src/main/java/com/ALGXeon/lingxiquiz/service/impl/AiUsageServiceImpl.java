package com.ALGXeon.lingxiquiz.service.impl;

import com.ALGXeon.lingxiquiz.common.ErrorCode;
import com.ALGXeon.lingxiquiz.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ALGXeon.lingxiquiz.model.entity.AiUsage;
import com.ALGXeon.lingxiquiz.service.AiUsageService;
import com.ALGXeon.lingxiquiz.mapper.AiUsageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

/**
* @author dell
* @description 针对表【ai_usage】的数据库操作Service实现
* @createDate 2025-02-22 21:52:58
*/
@Service
public class AiUsageServiceImpl extends ServiceImpl<AiUsageMapper, AiUsage>
    implements AiUsageService{

    private final Integer dailyAvailableUses = 20;


    /**
     * 获取或初始化用户的AI使用记录
     *
     * @param userId 用户ID
     * @return AI使用记录
     */
    public AiUsage getAiUsage(Long userId) {
        Optional<AiUsage> optionalAiUsage = Optional.ofNullable(this.getById(userId));
        if (optionalAiUsage.isPresent()) {
            return optionalAiUsage.get();
        } else {
            AiUsage newAiUsage = new AiUsage();
            newAiUsage.setUserId(userId);
            newAiUsage.setAvailableUses(10); // 默认10次
            newAiUsage.setLastUsedDate(convertLocalDateToDate(LocalDate.now()));
            this.save(newAiUsage);
            return newAiUsage;
        }
    }

    /**
     * 尝试使用一次并执行操作
     *
     * @param userId 用户ID
     * @return 是否成功获取权限并执行操作
     */
    public boolean tryGetAndUse(Long userId) {
        AiUsage aiUsage = getAiUsage(userId);
        Date today = convertLocalDateToDate(LocalDate.now());

        if (!today.equals(aiUsage.getLastUsedDate())) {
            // 如果日期变化了，重置计数
            aiUsage.setAvailableUses(dailyAvailableUses);
            aiUsage.setLastUsedDate(today);
            aiUsage.setAvailableUses(aiUsage.getAvailableUses() - 1);
            this.updateById(aiUsage);
            return true;
        }

        if (aiUsage.getAvailableUses() > 0) {
            aiUsage.setAvailableUses(aiUsage.getAvailableUses() - 1);
            this.updateById(aiUsage);
            return true;
        } else {
            return false;
        }
    }



    /**
     * 将 LocalDate 转换为 Date
     *
     * @param localDate LocalDate 对象
     * @return Date 对象
     */
    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

   public Integer getAvailableUses(Long userId){
       AiUsage aiUsage = getAiUsage(userId);
       Date today = convertLocalDateToDate(LocalDate.now());

       if (!today.equals(aiUsage.getLastUsedDate())) {
           // 如果日期变化了，重置计数
           aiUsage.setAvailableUses(dailyAvailableUses);
           aiUsage.setLastUsedDate(today);
           this.updateById(aiUsage);
       }
        return this.getById(userId).getAvailableUses();
    }
}




