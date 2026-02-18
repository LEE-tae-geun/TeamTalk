package com.example.chat_server.service;

import com.example.chat_server.dto.SignupRequestCreateRequest;
import com.example.chat_server.entity.SignupRequest;
import com.example.chat_server.mapper.SignupRequestMapper;
import com.example.chat_server.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupRequestService {

    private final SignupRequestMapper signupRequestMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public SignupRequestService(SignupRequestMapper signupRequestMapper,
                                UserMapper userMapper,
                                PasswordEncoder passwordEncoder) {
        this.signupRequestMapper = signupRequestMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Long create(SignupRequestCreateRequest req) {
        // null-safe trim
        String username = req.getUsername() == null ? null : req.getUsername().trim();
        String password = req.getPassword() == null ? null : req.getPassword().trim();
        String email = req.getEmail() == null ? null : req.getEmail().trim();
        String name = req.getName() == null ? null : req.getName().trim();
        String phoneNum = req.getPhoneNum() == null ? null : req.getPhoneNum().trim();

        // 기본 유효성
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username은 필수입니다.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password는 필수입니다.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email은 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name은 필수입니다.");
        if (phoneNum == null || phoneNum.isBlank()) throw new IllegalArgumentException("phoneNum은 필수입니다.");

        // 중복 체크: signup_request 내부
        if (signupRequestMapper.existsByUsername(username) > 0) {
            throw new IllegalArgumentException("이미 가입요청된 username입니다.");
        }
        if (signupRequestMapper.existsByEmail(email) > 0) {
            throw new IllegalArgumentException("이미 가입요청된 email입니다.");
        }

        // 중복 체크: auth_user(username)에도 이미 계정이 있으면 요청 불가
        if (userMapper.findByUsername(username) != null) {
            throw new IllegalArgumentException("이미 가입된 username입니다.");
        }

        SignupRequest s = new SignupRequest();
        s.setName(name);
        s.setEmail(email);
        s.setPhoneNum(phoneNum);
        s.setUsername(username);
        s.setPasswordHash(passwordEncoder.encode(password));
        s.setStatus("PENDING");

        signupRequestMapper.insert(s);
        return s.getReqId();
    }
}
