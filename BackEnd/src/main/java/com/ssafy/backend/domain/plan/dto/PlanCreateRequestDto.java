package com.ssafy.backend.domain.plan.dto;

import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.planmember.entity.PlanMember;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PlanCreateRequestDto {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "시작일은 필수 입력값입니다.")
    private LocalDate startDate;

    @NotBlank(message = "종료일은 필수 입력값입니다.")
    private LocalDate endDate;

    private List<PlanMember> planMembers;
    // DTO -> Entity
    public Plan toEntity() {
        return Plan.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .planMembers(planMembers)
                .build();
    }
}