package com.ALGXeon.lingxiquiz.scoring;

import com.ALGXeon.lingxiquiz.model.entity.App;
import com.ALGXeon.lingxiquiz.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author <a href="https://github.com/ALGXeon">ALGXeon</a>

 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}