package com.example.chat_server.mapper;

import com.example.chat_server.entity.SignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SignupRequestMapper {

    int insert(SignupRequest signupRequest);

    int existsByUsername(@Param("username") String username);

    int existsByEmail(@Param("email") String email);

    SignupRequest findById(@Param("reqId") Long reqId);

    List<SignupRequest> listByStatus(@Param("status") String status);

    int markApproved(@Param("reqId") Long reqId,
                     @Param("assignedDeptId") Long assignedDeptId,
                     @Param("reviewedBy") Long reviewedBy);

    int markRejected(@Param("reqId") Long reqId,
                     @Param("reviewedBy") Long reviewedBy,
                     @Param("rejectReason") String rejectReason);
}
