package com.ssafy.chillandcode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	// 일정 생성
	@PostMapping
	@Operation(summary = "일정 등록", description = "로그인된 사용자의 userId를 세션에서 가져와 새 일정을 생성합니다.")
    public ResponseEntity<ApiResponse<?>> insertSchedule(@RequestBody Schedule schedule, HttpSession session) {
		long userId = (Long) session.getAttribute("userId");
		// long userId = 1L; // swagger 테스트용 하드코딩 (나중에 삭제)

		schedule.setUserId(userId);

		boolean result = scheduleService.insertSchedule(schedule);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("일정 등록에 실패했습니다."));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("일정이 등록되었습니다.", null));
	}

	// 일정 조회
	@GetMapping
	@Operation(summary = "월별 일정 조회", description = "월 정보(YYYY-MM)를 Query Parameter로 받아 해당 사용자의 월별 일정을 조회합니다.")
    public ResponseEntity<?> selectScheduleByMonth(@RequestParam String month, HttpSession session) {
		long userId = (Long) session.getAttribute("userId");
		// long userId = 1L; // swagger 테스트용 하드코딩 (나중에 삭제)
		

		List<Schedule> result = scheduleService.selectScheduleByMonth(userId, month);

        return ResponseEntity.ok(ApiResponse.success(Map.of("schedules", result)));
	}

	// 일정 수정
	@PatchMapping("/{scheduleId}")
	@Operation(summary = "일정 수정", description = "scheduleId에 해당하는 일정을 수정합니다. 해당 일정의 소유자인지 userId로 확인합니다.")
    public ResponseEntity<ApiResponse<?>> updateSchedule(@PathVariable long scheduleId, @RequestBody Schedule schedule,
            HttpSession session) {

		long userId = (Long) session.getAttribute("userId");
		// long userId = 1L; // swagger 테스트용 하드코딩 (나중에 삭제)
		schedule.setScheduleId(scheduleId);
		schedule.setUserId(userId);
		
		boolean result = scheduleService.updateSchedule(userId, scheduleId, schedule);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("일정 수정에 실패했습니다."));
        }

        return ResponseEntity.ok(ApiResponse.success("일정이 수정되었습니다.", null));
	}

	@DeleteMapping("/{scheduleId}")
	@Operation(summary = "일정 삭제", description = "scheduleId에 해당하는 일정을 삭제합니다. 로그인한 사용자 본인의 일정만 삭제 가능합니다.")
    public ResponseEntity<ApiResponse<?>> deleteSchedule(@PathVariable long scheduleId, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		// long userId = 1L; // swagger 테스트용 하드코딩 (나중에 삭제)

		boolean result = scheduleService.deleteSchedule(userId, scheduleId);

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("일정 삭제에 실패했습니다."));
        }

        return ResponseEntity.ok(ApiResponse.success("일정이 삭제되었습니다.", null));
	}

}
