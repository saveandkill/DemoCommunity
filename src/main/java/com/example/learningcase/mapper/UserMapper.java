package com.example.learningcase.mapper;

import com.example.learningcase.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> queryAll();

    User queryOne(Long id);

    void add(User user);

    void update(User user);

}
