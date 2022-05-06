package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.service.dto.MemberRegisterDto;
import com.i3e3.mindlet.global.enums.Community;

public interface MemberService {

    Member register(MemberRegisterDto memberRegisterDto);

    boolean isExistsId(String id);

    void changeCommunity(Long memberSeq, Community community);

    void changeSound(Long memberSeq, boolean soundOff);

    void changeLanguage(Long memberSeq, AppConfig.Language language);
}
