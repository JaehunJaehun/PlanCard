package com.ssafy.backend.domain.plandetail.service;

import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailListResponseDto;

import java.util.List;

public interface PlanDetailService {
    //상세 계획 생성 수정
    void createAndUpdatePlanDetail(Long planId, String action, List<PlanDetailCreateRequestDto> planDetailCreateRequestDtoList);
    //상세 계획 리스트 조회
    List<PlanDetailListResponseDto> getPlanDetailList(Long planId);

}