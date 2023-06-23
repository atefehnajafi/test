package com.neshan.springh2project.controller;


import com.neshan.springh2project.service.UserService;
import com.neshan.springh2project.dto.UserDto;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.exception.DuplicateUserNameException;
import com.neshan.springh2project.exception.EmptyDataException;
import com.neshan.springh2project.service.UrlInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UrlInfoService urlInfoService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userInfo", new User());
        return "register";
    }


    @PostMapping("/process_register")
    public String processRegister(@ModelAttribute("user") UserDto userDto, Model model) {

        User user = modelMapper.map(userDto, User.class);
        try {
            userService.saveUser(user);
            return "message/message_success";
        } catch (DuplicateUserNameException ex) {
            model.addAttribute("errorMessage", "User already registered");
            return "message/message_fail";
        } catch (EmptyDataException e) {
            model.addAttribute("errorMessage", "user or pass is  null");
            return "message/message_fail";
        }
    }


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userInfo", new User());
        return "login";
    }


    @PostMapping("/process_login")
    public String processLogin(Model model, UserDto userdto) {
        User userMap = modelMapper.map(userdto, User.class);

        User userInfo = userService.findUserByUserName(userMap.getUserName());

        if (userInfo != null) {
            model.addAttribute("urls", urlInfoService.listUrl(userdto.getUserName()));
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("urlInfo", new UrlInfo());
            return "convert_shortUrl";
        } else {
            model.addAttribute("userInfo", new User());
            return "login";
        }
    }

}
