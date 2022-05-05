package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final AppConfigRepository appConfigRepository;

    @Override
    public boolean isExistsId(String id) {
        return memberRepository.existsByIdContainsDeleted(id);
    }

    @Transactional
    @Override
    public void changeCommunity(Long memberSeq, Community community) {
        AppConfig appConfig = appConfigRepository.findByMemberSeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        appConfig.changeCommunity(community);
    }
}
