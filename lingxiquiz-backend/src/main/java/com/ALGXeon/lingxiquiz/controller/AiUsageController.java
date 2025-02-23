package com.ALGXeon.lingxiquiz.controller;

import com.ALGXeon.lingxiquiz.common.BaseResponse;
import com.ALGXeon.lingxiquiz.common.ResultUtils;
import com.ALGXeon.lingxiquiz.service.AiUsageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aiUsage")
@Slf4j
public class AiUsageController {

    @Resource
    private AiUsageService aiUsageService;

    @GetMapping("/get")
    public BaseResponse<Integer> getAvailableUses(Long userId, HttpServletRequest request){
        Integer remainingUses = aiUsageService.getAvailableUses(userId);
        return ResultUtils.success(remainingUses);
    }
}
