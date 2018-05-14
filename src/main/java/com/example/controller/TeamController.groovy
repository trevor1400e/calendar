package com.example.controller

import com.example.model.Team
import com.example.repository.TeamRepository
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid
import java.text.SimpleDateFormat

@RestController
class TeamController {
    @Autowired
    private UserService userService;

    @Autowired
    private TeamRepository teamRepository

    @RequestMapping(value = "/team/add", method = RequestMethod.GET)
    public ModelAndView addTeam() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "null"
        Map<String, String> map = auth.principal
        Team team = new Team()

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            }
        }
        String timeStamp = new SimpleDateFormat("YYYY-MM-DD").format(Calendar.getInstance().getTime());

        modelAndView.addObject("Date", timeStamp)
        modelAndView.addObject("team", team)
        modelAndView.addObject("userName", username)
        modelAndView.setViewName("addTeam")
        return modelAndView
    }

    @RequestMapping(value = "/team/add", method = RequestMethod.POST)
    public ModelAndView postTeam(@Valid Team team, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "null"
        Map<String, String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            }
        }
        String timeStamp = new SimpleDateFormat("YYYY-MM-DD").format(Calendar.getInstance().getTime());

        if (bindingResult.hasErrors()) {
            println("there was an error")
            modelAndView.setViewName("addTeam");
        } else {
            println("trying to save")
            teamRepository.save(team)
            modelAndView.addObject("successMessage", "Team created successfully!");
            modelAndView.addObject("Date", timeStamp)
            modelAndView.addObject("team", team)
            modelAndView.addObject("userName", username)
            modelAndView.setViewName("addTeam")
        }
        return modelAndView
    }

}