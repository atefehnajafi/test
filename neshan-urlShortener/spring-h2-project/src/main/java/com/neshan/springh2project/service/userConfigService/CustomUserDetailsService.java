package com.neshan.springh2project.service.userConfigService;

import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName()
                    , user.getPassword() , null);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
