package com.ALGXeon.lingxiquiz.scoring;

import cn.hutool.json.JSONUtil;
import com.ALGXeon.lingxiquiz.manager.AiManager;
import com.ALGXeon.lingxiquiz.model.dto.question.QuestionAnswerDTO;
import com.ALGXeon.lingxiquiz.model.dto.question.QuestionContentDTO;
import com.ALGXeon.lingxiquiz.model.entity.App;
import com.ALGXeon.lingxiquiz.model.entity.Question;
import com.ALGXeon.lingxiquiz.model.entity.UserAnswer;
import com.ALGXeon.lingxiquiz.model.vo.QuestionVO;
import com.ALGXeon.lingxiquiz.service.QuestionService;
import com.ALGXeon.lingxiquiz.tools.OpenAIApiUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.json.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 测评类应用评分策略
 *
 * @author <a href="https://github.com/ALGXeon">ALGXeon</a>
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private AiManager aiManager;

    /**
     * AI 评分系统消息模板
     */
    private static final String AI_TEST_SCORING_SYSTEM_MESSAGE_TEMPLATE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称：%s\n" +
            "【【【应用描述：%s】】】,\n" +
            "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
            "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
            "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
            "```\n" +
            "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
            "```\n" +
            "3. 返回格式必须为 JSON 对象";

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();

        // 1. 根据 id 查询到题目
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 2. 封装 Prompt
        String promptMessage = getAiTestScoringPromptMessage(app, questionContent, choices);

        // AI 生成
        String result = aiManager.doMyRequest(promptMessage);
        System.out.println(result);

        // 使用工具类解析AI返回的结果
        OpenAIApiUtils.Tuple<String, JSONObject> parsedResult = OpenAIApiUtils.tryParseJsonObject(result);
        JSONObject cleanedJson = parsedResult.item2;

        // 提取并转换为UserAnswer对象
        UserAnswer userAnswer = new UserAnswer();

        if (cleanedJson.has("message") && !cleanedJson.isNull("message")) {
            JSONObject messageJson = cleanedJson.getJSONObject("message");
            if (messageJson.has("content") && !messageJson.isNull("content")) {
                String content = messageJson.getString("content");

                // 解析内容中的JSON部分
                JSONObject resultJson = new org.json.JSONObject(content);

                if (resultJson.has("resultName") && !resultJson.isNull("resultName") &&
                        resultJson.has("resultDesc") && !resultJson.isNull("resultDesc")) {
                    userAnswer.setResultName(resultJson.getString("resultName"));
                    userAnswer.setResultDesc(resultJson.getString("resultDesc"));
                }
            }
        }

        // 填充答案对象的其他属性
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));

        return userAnswer;
    }

    /**
     * AI 评分提示消息封装
     *
     * @param app
     * @param questionContentDTOList
     * @param choices
     * @return
     */
    private String getAiTestScoringPromptMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        // 生成系统消息部分
        String systemMessage = String.format(AI_TEST_SCORING_SYSTEM_MESSAGE_TEMPLATE,
                app.getAppName(),
                app.getAppDesc());

        // 生成用户消息部分
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        String userMessage = JSONUtil.toJsonStr(questionAnswerDTOList);

        // 合并系统消息和用户消息
        return systemMessage + "\n\n" + userMessage;
    }
}