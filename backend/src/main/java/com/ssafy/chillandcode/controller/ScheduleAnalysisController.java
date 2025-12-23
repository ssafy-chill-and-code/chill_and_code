package com.ssafy.chillandcode.controller;

import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleSummaryResponse;
import com.ssafy.chillandcode.model.service.ScheduleAnalysisService;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleAnalysisController {

	@Autowired
	private ScheduleAnalysisService analysisService;

	@GetMapping("/summary")
	public ApiResponse<ScheduleSummaryResponse> getSummary(
			@RequestParam YearMonth month,
			@AuthenticationPrincipal Long userId
	) {
		return ApiResponse.success(
				analysisService.analyzeMonth(userId, month)
		);
	}
}
