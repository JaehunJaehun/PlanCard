package com.ssafy.backend.domain.alarm.service;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.dto.AlarmFriendRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmPlanRequestDto;
import com.ssafy.backend.domain.alarm.entity.Alarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.alarm.exception.AlarmError;
import com.ssafy.backend.domain.alarm.exception.AlarmException;
import com.ssafy.backend.domain.alarm.repository.AlarmRepository;
import com.ssafy.backend.domain.fcm.service.FCMService;
import com.ssafy.backend.domain.friend.repository.FriendshipRepository;
import com.ssafy.backend.domain.friend.service.FriendshipService;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.global.common.dto.SliceResponse;
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
    private final FCMService fcmService;

    @Override
    public void createAlarm(Long fromMemberId, AlarmCreateRequestDto createRequestDto) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        Member toMember = memberRepository.findById(createRequestDto.getToMemberId()).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        Alarm alarm = createRequestDto.toEntity(fromMember, toMember);
        alarmRepository.save(alarm);

        fcmService.sendMessageTo(toMember.getId(), alarm.getContent());
    }

    @Override
    public void friendRequestAlarm(Long fromMemberId, AlarmFriendRequestDto friendRequestDto) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        Member toMember = memberRepository.findByEmail(friendRequestDto.getEmail()).orElseThrow(()
        -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        if (fromMember.equals(toMember)) {
            throw new AlarmException(AlarmError.CANNOT_FRIEND_REQUEST_SELF);
        }

        Alarm alarm = friendRequestDto.toEntity(fromMember, toMember);
        alarmRepository.save(alarm);

        fcmService.sendMessageTo(toMember.getId(), alarm.getContent());
    }

    @Override
    public void planRequestAlarm(Long fromMemberId, List<AlarmPlanRequestDto> planRequestDtoList) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() ->
                new MemberException(MemberError.NOT_FOUND_MEMBER));

        planRequestDtoList.forEach(planRequestDto -> {
            Member toMember = memberRepository.findById(planRequestDto.getFriendId()).orElseThrow(() ->
                    new MemberException(MemberError.NOT_FOUND_MEMBER));

            Alarm alarm = planRequestDto.toEntity(fromMember, toMember);
            alarmRepository.save(alarm);

            // FCM 서비스를 이용하여 알람 메시지 전송
            fcmService.sendMessageTo(toMember.getId(), alarm.getContent());
        });

    }

//    @Override
//    public SliceResponse getAlarmList(Long memberId, Pageable pageable) {
//        Slice<AlarmDto> alarms = alarmRepository.findAlarmSliceByMemberId(memberId, pageable);
//        return SliceResponse.of(alarms);
//    }

    @Override
    public List<AlarmDto> getAlarmList(Long memberId, Long lastAlarmId, int limit) {
        return alarmRepository.findAlarmsAfterId(memberId, lastAlarmId, limit);
    }

    @Override
    public void handleAlarm(Long memberId, Long alarmId, AlarmStatus action) {
        // TODO: 알람 관련 Exception 처리하기
        Alarm alarm = alarmRepository.findById(alarmId)
                .filter(a -> a.getToMember().getId().equals(memberId))  // 알람의 수신자가 현재 사용자와 일치하는지 확인
                .orElseThrow(() -> new RuntimeException("알람을 찾을 수 없습니다."));

        switch (action.getValue()) {
            case "ACCEPT":
                processAcceptAlarm(alarm, memberId);
                break;
            case "REJECT":
                alarmRepository.delete(alarm);
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
    }

    @Override
    public void deleteAlarmList(Long memberId) {
        List<Alarm> alarmList = alarmRepository.findByToMemberId(memberId);
        alarmRepository.deleteAll(alarmList);
    }

    private void processAcceptAlarm(Alarm alarm, Long memberId) {
        alarm.accept();
        if (alarm.getType() == AlarmType.FRIEND) {
            friendshipService.accept(memberId, alarm.getFromMember().getId());
        } else if (alarm.getType() == AlarmType.CONFERENCE) {
            // TODO: 회의 참여 요청 알람인 경우, 추가적인 처리하기 (화상회의 url 반환 등)
        }
    }
}
