package com.example.controller

import com.example.model.SlackUser
import com.example.service.SlackUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class SlackController {

    @Autowired
    private SlackUserService slackUserService;

    @RequestMapping(value = ['/', '/login', '/slack'], method = RequestMethod.GET)
    public ModelAndView slackRegistration() {
        ModelAndView modelAndView = new ModelAndView()
        SlackUser slackUser = new SlackUser();

        modelAndView.addObject("slackUser", slackUser)
        modelAndView.setViewName("slack")
        return modelAndView
    }

}
