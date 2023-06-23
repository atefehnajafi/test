package com.neshan.springh2project.service;
import com.neshan.springh2project.entity.user.User;

public interface UserService {

    User saveUser(User user);

    User findUserByUserName(String userName);
}
