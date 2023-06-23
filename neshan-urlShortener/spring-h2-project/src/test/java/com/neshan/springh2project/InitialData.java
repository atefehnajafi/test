package com.neshan.springh2project;

import com.neshan.springh2project.service.UrlInfoService;
import com.neshan.springh2project.service.UserService;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.exception.UrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitialData {

    @Autowired
    UserService userService;

    @Autowired
    UrlInfoService urlInfoService;



    public User createUser() {
        User user1 = new User();
        user1.setUserName("Ali");
        user1.setPassword("Ali123");
        return userService.saveUser(user1);
    }

    public List<UrlInfo> createTwoUrl() throws UrlException {
        User user = createUser();
        String originalUrl1 = "https://junit.org/junit5/docs/current/user-guide/#extensions-exception-handling";
        UrlInfo url1 = urlInfoService.createShortUrl(originalUrl1, user.getUserName());
        String originalUrl2 = "https://stackoverflow.com/questions/156503/how-do-you-assert-that-a-certain-exception-is-thrown-in-junit-tests/41019785#41019785";
        UrlInfo url2 =  urlInfoService.createShortUrl(originalUrl2, user.getUserName());

        ArrayList<UrlInfo> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);

        return urls;
    }

}
