package com.neshan.springh2project.controller;

import com.neshan.springh2project.exception.UrlNotFoundException;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.exception.UrlException;
import com.neshan.springh2project.service.UrlInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Controller
public class UrlInfoController {

    private final UrlInfoService urlInfoService;

    public UrlInfoController(UrlInfoService urlInfoService) {
        this.urlInfoService = urlInfoService;
    }


    @PostMapping("/createShortUrl")
    public String createShortUrl(Model model, @RequestParam(name = "userName") String userName,
                                 @RequestParam(name = "originalUrl") String originalUrl) throws UnsupportedEncodingException {
        try {
            UrlInfo urlInfo = urlInfoService.createShortUrl(originalUrl, userName);
            model.addAttribute("urlInfo", urlInfo);
            model.addAttribute("username", userName);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There are more than 10 url for this user ! ");
            return "message/message_fail";
        }
        return "shortUrl";
    }

    @GetMapping("/deleteLink/{id}")
    public String deleteLink(@PathVariable Long id, Model model) {
        try {
            urlInfoService.deleteUrl(id);
            return "message/message_success";
        } catch (UrlException ex) {
            model.addAttribute("errorMessage", "can not find url with id : " + id);
            return "message/message_fail";
        }
    }

    @RequestMapping(value = "/redirectToOriginal/{id}")
    public String redirectToOriginal(Model model, @PathVariable("id") Long id) {
        try {
            String redirect = urlInfoService.redirectToOriginal(id);
            return "redirect:" + redirect;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "url not exist");
            return "message/message_fail";
        }
    }


    @RequestMapping(value = "/redirectToOriginalWithParameter")
    public String redirectWithParameter(Model model,
                                        @RequestParam(name = "shortUrlWithParameter") String shortUrlWithParameter,
                                        @RequestParam(name = "urlId") Long urlId) {
        try {
            String redirect = urlInfoService.redirectToOriginalWithParameter(shortUrlWithParameter, urlId);
            return "redirect:" + redirect;
        } catch (UrlNotFoundException e) {
            model.addAttribute("errorMessage", "not exist url by shortUrl :  " + shortUrlWithParameter);
            return "message/message_fail";
        } catch (MalformedURLException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "message/message_fail";
        }
    }

    @RequestMapping(value = "/countOfClick/{id}")
    public String countOfClick(Model model, @PathVariable("id") Long id) {
        try {
            int count = urlInfoService.countOfShortUrlClick(id);
            model.addAttribute("count", count);
            return "message/message_click";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "url not exist");
            return "message/message_fail";
        }
    }


//    @GetMapping(value = "/redirectToOriginal/{shortUrl}}")
//    public ResponseEntity<Void> getAndRedirect(@PathVariable String shortUrl) {
//        String url = urlInfoService.getOriginalUrl(shortUrl);
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(URI.create(url))
//                .build();
//    }
}
