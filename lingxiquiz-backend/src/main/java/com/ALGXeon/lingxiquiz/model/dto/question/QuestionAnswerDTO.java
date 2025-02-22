package com.ALGXeon.lingxiquiz.model.dto.question;

import lombok.Data;

import java.util.List;

/**
 * 题目答案封装类（用于 AI 评分）
 *
 * @author <a href="https://github.com/ALGXeon">ALGXeon</a>
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 题目选项列表
     */
    private List<QuestionContentDTO.Option> options;

    /**
     * 用户答案
     */
    private String userAnswer;
}
