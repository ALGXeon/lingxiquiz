package com.ALGXeon.lingxiquiz.scoring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {

    /**
     * 应用类型（0-得分类，1-测评类）
     * @return
     */
    int appType();

    /**
     * 评分策略（0-自定义，1-AI）
     * @return
     */
    int scoringStrategy();
}