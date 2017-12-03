package com.sun.dao;


import org.apache.ibatis.annotations.Param;

import com.sun.entity.User;

public interface TestDao {
User select(@Param("id") int id);
}
