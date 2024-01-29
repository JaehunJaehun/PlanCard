package com.ssafy.backend.domain.alarm.service;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.entity.Alarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.alarm.repository.AlarmRepository;
import com.ssafy.backend.domain.friend.repository.FriendshipRepository;
import com.ssafy.backend.domain.friend.service.FriendshipService;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmServiceImpl implements AlarmService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final FriendshipService friendshipService;

    @Override
    public void createAlarm(Long fromMemberId, AlarmCreateRequestDto createRequestDto) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        Member toMember = memberRepository.findById(createRequestDto.getToMemberId()).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        Alarm alarm = createRequestDto.toEntity(fromMember, toMember);
        alarmRepository.save(alarm);
    }

    @Override
    public List<AlarmDto> getAlarmList(Long memberId, Pageable pageable) {
        Slice<AlarmDto> alarmSlice = alarmRepository.findAlarmSliceByMemberId(memberId, pageable);
        return alarmSlice.getContent();
    }

    @Override
    public void acceptAlarm(Long memberId, Long alarmId) {
        // TODO: 알람 관련 Exception 처리하기
        Alarm alarm = alarmRepository.findById(alarmId).filter(a -> a.getToMember().getId().equals(memberId)) // 알람의 수신자가 현재 사용자와 일치하는지 확인
                .orElseThrow(() -> new RuntimeException("알람을 찾을 수 없습니다."));

        if (alarm.getType() == AlarmType.FRIEND) {
            friendshipService.accept(memberId, alarm.getFromMember().getId());
        } else if (alarm.getType() == AlarmType.CONFERENCE) {
            // TODO: 회의 참여 요청 알람인 경우, 추가적인 처리하기 (화상회의 url 반환 등)
        }

        alarm.accept();
    }
}
