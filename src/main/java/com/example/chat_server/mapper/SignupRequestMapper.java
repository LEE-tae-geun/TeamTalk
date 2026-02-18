package com.example.chat_server.mapper;

import com.example.chat_server.entity.SignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SignupRequestMapper {
    int insert(SignupRequest req);

    SignupRequest findById(@Param("reqId") Long reqId);

    List<SignupRequest> listByStatus(@Param("status") String status);

    int existsByUsername(@Param("username") String username);

    int existsByEmail(@Param("email") String email);
}