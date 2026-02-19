package com.example.chat_server.mapper;

import com.example.chat_server.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    int insertMember(Member member);

    int existsByEmail(@Param("email") String email);

    int existsByPhone(@Param("phoneNum") String phoneNum);
}
