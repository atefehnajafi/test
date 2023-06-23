package com.neshan.springh2project;


import com.neshan.springh2project.config.RoundRobinLoadBalancer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BaseTest {

    @Test
    public void create_createOriginalUrlWithParameterWhenContainParam_success() throws MalformedURLException {
        String originalUrlWithParameter = "http://google.com?id=1";
        String shortUrl = "http://shr.ir/12";

        String shortUrlWithParameter = " http://shr.ir/12?name=x";

        URL url = new URL(shortUrlWithParameter);
        String param = url.getQuery();

        String convertToOriginalWithParameter = originalUrlWithParameter + "&" + param;

        Assertions.assertEquals("http://google.com?id=1&name=x", convertToOriginalWithParameter);
    }


    @Test
    public void create_createOriginalUrlWithParameterWhenOriginalHasNotParam_success() throws MalformedURLException {
        String originalUrlWithParameter = "https://neshan.org/maps/@35.713423,51.294204,18.1z,0.0p/places/42cbca39ba8d6ebc06fd1c933c0f491c";
        String shortUrl = "https://nshn.ir/42_bvHCDPxVpm6";

        String shortUrlWithParameter = "https://nshn.ir/42_bvHCDPxVpm6?name=x";

        URL url = new URL(shortUrlWithParameter);
        String param = url.getQuery();

        String convertToOriginalWithParameter = originalUrlWithParameter + "?" + param;

        Assertions.assertEquals("https://neshan.org/maps/@35.713423,51.294204,18.1z,0.0p/places/42cbca39ba8d6ebc06fd1c933c0f491c?name=x", convertToOriginalWithParameter);
    }

    @Test
    public void remove_RemoveSlashEndOfUrl_success() {
        String url = "https://neshan.org/maps/";
        String removeSlash = StringUtils.removeEnd(url, "/");
        Assertions.assertEquals(removeSlash, "https://neshan.org/maps");

    }


}
