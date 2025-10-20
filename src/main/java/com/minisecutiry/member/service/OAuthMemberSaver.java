package com.minisecutiry.member.service;

import com.minisecutiry.member.infra.MiniMember;

public interface OAuthMemberSaver {
    void save(MiniMember miniMember);
}
