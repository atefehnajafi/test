package com.neshan.springh2project.service;

import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.exception.DuplicateUserNameException;
import com.neshan.springh2project.exception.EmptyDataException;
import com.neshan.springh2project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User beforeAdd(User user) {
        User existingUser = findUserByUserName(user.getUserName());

        if (existingUser != null){
            throw new DuplicateUserNameException("User already registered");
        }
        if (user.getUserName() == null || user.getPassword() ==null)
            throw new EmptyDataException("user or pass is  null");
        return user;
    }

    @Override
    public User saveUser(User user) {
        beforeAdd(user);
        User newUser = new User(user.getUserName(), passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }


    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
