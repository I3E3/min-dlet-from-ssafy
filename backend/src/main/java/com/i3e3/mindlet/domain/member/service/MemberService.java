package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.global.enums.Community;

public interface MemberService {

    void changeCommunity(Long memberSeq, Community community);

}
