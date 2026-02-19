package com.example.chat_server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DepartmentMapper {
    int existsById(@Param("deptId") Long deptId);
}
