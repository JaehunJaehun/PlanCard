package com.ssafy.backend.domain.alarm.dto;

import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlarmDto {
    private Long id;
    private Long fromMemberId;
    private Long toMemberId;
    private AlarmType type;
    private String content;
}
