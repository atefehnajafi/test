package com.neshan.springh2project.service;

import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;

import static org.junit.Assert.*;

import com.neshan.springh2project.exception.UrlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.neshan.springh2project.InitialData;

import java.net.URL;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UrlServiceTest {

    @Autowired
    private UrlInfoService urlInfoService;

    @Autowired
    UserService userService;

    @Autowired
    InitialData initialData;


    @Test
    public void create_createShortUrlWhenGetOriginalUrl_Success() {
        User user = initialData.createUser();
        UrlInfo urlInfo;

        String originalUrl = "https://mkyong.com/spring-security/spring-security-hibernate-annotation-example/";
        try {
            urlInfo = urlInfoService.createShortUrl(originalUrl, user.getUserName());
            assertNotNull(urlInfo);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void create_createShortUrlMoreThanTenForUser_fail() throws UrlException {

        User user = initialData.createUser();

        String originalUrl = "https://mkyong.com/spring-security/spring-security-hibernate-annotation-example/";

        UrlException thrown = Assertions.assertThrows(UrlException.class, () -> {
            for (int i = 0; i < 10; i++) {
                urlInfoService.createShortUrl(originalUrl, user.getUserName());
            }
            urlInfoService.createShortUrl(originalUrl, user.getUserName());
        });
        Assertions.assertEquals("There are more than 10 url for this user", thrown.getMessage());

    }

    @Test
    public void get_getUrlListForUser_success() throws UrlException {
        initialData.createTwoUrl();
        List<UrlInfo> urlList = urlInfoService.listUrl("Ali");
        Assertions.assertEquals(urlList.size(), 2);
    }

    @Test
    public void delete_deleteUrlFormUrlTable_success() throws UrlException {
        initialData.createTwoUrl();
        urlInfoService.deleteUrl(urlInfoService.listUrl("Ali").get(0).getId());
        List<UrlInfo> urlList = urlInfoService.listUrl("Ali");
        Assertions.assertEquals(urlList.size(), 1);
    }

    @Test
    public void create_createOriginalUrlWithParameter_success() {
        User user = initialData.createUser();
        UrlInfo urlInfo;

        String originalUrl = "https://mkyong.com/spring-security/spring-security-hibernate-annotation-example/";
        try {
            urlInfo = urlInfoService.createShortUrl(originalUrl, user.getUserName());
            URL url = new URL(urlInfo.getShortUrl()+"?name=x");
            String query = url.getQuery();
            assertEquals(query , "name=x");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
