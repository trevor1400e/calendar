package com.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.Event;
import com.example.model.User;
import com.example.repository.EventRepository;
import com.example.repository.RoleRepository;
import com.example.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	@Autowired
    private EventRepository eventRepository;

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	/*
    @GetMapping("/home")
    public String userhome(Model model) {
 
        return "home";
    }
    */
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public ModelAndView userhome(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		modelAndView.addObject("userTest", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/find", method = RequestMethod.GET)
	public ModelAndView userfind(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		modelAndView.setViewName("find");
		return modelAndView;
	}
	@RequestMapping(value="/host", method = RequestMethod.GET)
	public ModelAndView userhost(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		Event event = new Event();
		modelAndView.addObject("event", event);
		modelAndView.setViewName("host");
		return modelAndView;
	}
	
	@RequestMapping(value = "/host", method = RequestMethod.POST)
	public ModelAndView createNewPost(@Valid Event event, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("host");
		} else {
			event.setEmail(user.getEmail());
			eventRepository.save(event);
			modelAndView.addObject("successMessage", "Event has been registered successfully");
			modelAndView.addObject("event", new Event());
			modelAndView.setViewName("host");
			
		}				
		return modelAndView;
	}
	
	
	@RequestMapping(value="/event/{id}", method = RequestMethod.GET)
	public ModelAndView event(@PathVariable int id){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Event event = eventRepository.findById(id);
		modelAndView.addObject("eventTitle", event.getTitle());
		modelAndView.addObject("eventContent", event.getContent());
		modelAndView.addObject("eventDate", event.getDate());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		modelAndView.setViewName("event");
		return modelAndView;
	}
	
	

}
