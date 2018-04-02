package com.example.controller

import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import java.text.SimpleDateFormat

@RestController
class PairController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/pair", method = RequestMethod.GET)
    public ModelAndView calender() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "null"
        Map<String, String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            }
        }
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime());

        modelAndView.addObject("Date", timeStamp)
        modelAndView.addObject("userName", username)
        modelAndView.setViewName("pair")
        return modelAndView
    }
}