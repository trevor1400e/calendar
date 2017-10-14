package com.example.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.ChargeRequest;
import com.example.model.Event;
import com.example.model.User;
import com.example.repository.EventRepository;
import com.example.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	@Autowired
    private EventRepository eventRepository;
	
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

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
		modelAndView.addObject("userBal", user.getBalance());
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
		modelAndView.addObject("eventCount", eventRepository.count());
		modelAndView.addObject("searchType", "None");
		modelAndView.setViewName("find");

		
		return modelAndView;
	}
	
	@RequestMapping(value="/find", method = RequestMethod.POST)
	public ModelAndView usersearch(@RequestParam("search") String search, @RequestParam("searchtype") String searchType){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		
			List<Event> event = eventRepository.findByTitleContaining(search);
			List<String> titles = new ArrayList<String>();
			List<Integer> links = new ArrayList<Integer>();
			modelAndView.addObject("successMessage", "Search has been done successfully");
			modelAndView.addObject("searchType", searchType);
			modelAndView.addObject("eventCount", event.size());
			//modelAndView.addObject("eventCount", searchType);
			//System.out.println(search + " : " + event.get(1).getTitle());
			for (Event item : event) {
			    System.out.println(item.getTitle());
			    titles.add(item.getTitle());
			    links.add(item.getId());
			}
			modelAndView.addObject("titles", titles);
			modelAndView.addObject("links", links);
			
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
		//TODO search purch by userid not email
		Double price = (round(event.getPrice(), 2) * 100);
		int i = (int) price.doubleValue();

		
		modelAndView.addObject("event", event);
		modelAndView.addObject("eventTitle", event.getTitle());
		modelAndView.addObject("eventContent", event.getContent());
		modelAndView.addObject("eventDate", event.getDate());
		modelAndView.addObject("eventPrice", i);
		
		modelAndView.addObject("stripePublicKey", stripePublicKey);
		modelAndView.addObject("currency", ChargeRequest.Currency.USD);

		
		modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		modelAndView.addObject("userEmail", user.getEmail());
		modelAndView.setViewName("event");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getImage/{imageId}")
	@ResponseBody
	public byte[] getImage(@PathVariable String imageId, HttpServletRequest request)  {
	Path path = Paths.get("images/"+imageId+".png");
	byte[] data;
	try {
		data = Files.readAllBytes(path);
		return data;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	return null;
	}
	
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	

}
