package com.example.chat_server.service;

import com.example.chat_server.entity.Member;
import com.example.chat_server.entity.SignupRequest;
import com.example.chat_server.entity.User;
import com.example.chat_server.mapper.DepartmentMapper;
import com.example.chat_server.mapper.MemberMapper;
import com.example.chat_server.mapper.SignupRequestMapper;
import com.example.chat_server.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminSignupRequestService {

    private final SignupRequestMapper signupRequestMapper;
    private final DepartmentMapper departmentMapper;
    private final MemberMapper memberMapper;
    private final UserMapper userMapper;

    public AdminSignupRequestService(SignupRequestMapper signupRequestMapper,
                                    DepartmentMapper departmentMapper,
                                    MemberMapper memberMapper,
                                    UserMapper userMapper) {
        this.signupRequestMapper = signupRequestMapper;
        this.departmentMapper = departmentMapper;
        this.memberMapper = memberMapper;
        this.userMapper = userMapper;
    }

    public List<SignupRequest> list(String status) {
        return signupRequestMapper.listByStatus(status);
    }

    @Transactional
    public void approve(Long reqId, Long deptId, Long adminMemberId) {
        SignupRequest req = signupRequestMapper.findById(reqId);
        if (req == null) throw new IllegalArgumentException("가입요청을 찾을 수 없습니다.");
        if (!"PENDING".equals(req.getStatus())) throw new IllegalArgumentException("PENDING 상태만 승인할 수 있습니다.");

        if (deptId == null || departmentMapper.existsById(deptId) == 0) {
            throw new IllegalArgumentException("부서(deptId)가 존재하지 않습니다.");
        }

        if (userMapper.findByUsername(req.getUsername()) != null) {
            throw new IllegalArgumentException("이미 승인되어 계정이 존재하는 아이디입니다.");
        }

        if (req.getEmail() != null && memberMapper.existsByEmail(req.getEmail()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (req.getPhoneNum() != null && memberMapper.existsByPhone(req.getPhoneNum()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }

        Member m = new Member();
        m.setDeptId(deptId);
        m.setName(req.getName());
        m.setEmail(req.getEmail());
        m.setPhoneNum(req.getPhoneNum());

        // member 테이블의 NOT NULL 컬럼 기본값(프로젝트 정책에 맞게 추후 수정 권장)
        m.setPosition("NEW");
        m.setBirth(LocalDate.of(2000, 1, 1));
        m.setStateId(1L);
        m.setJoinDate(LocalDateTime.now());

        memberMapper.insertMember(m);
        if (m.getMemberId() == null) throw new IllegalStateException("member_id 생성에 실패했습니다.");

        User u = new User();
        u.setMemberId(m.getMemberId());
        u.setDeptId(deptId);
        u.setUsername(req.getUsername());
        u.setPassword(req.getPasswordHash());
        u.setRole("ROLE_USER");
        userMapper.insert(u);

        signupRequestMapper.markApproved(reqId, deptId, adminMemberId);
    }

    @Transactional
    public void reject(Long reqId, Long adminMemberId, String reason) {
        SignupRequest req = signupRequestMapper.findById(reqId);
        if (req == null) throw new IllegalArgumentException("가입요청을 찾을 수 없습니다.");
        if (!"PENDING".equals(req.getStatus())) throw new IllegalArgumentException("PENDING 상태만 거절할 수 있습니다.");

        String msg = (reason == null || reason.isBlank()) ? null : reason.trim();
        signupRequestMapper.markRejected(reqId, adminMemberId, msg);
    }
}
