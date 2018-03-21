package com.example.controller

import com.example.model.Event
import com.example.model.User
import com.example.repository.EventRepository
import com.example.service.UserService
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.swing.text.DateFormatter
import javax.validation.Valid
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class CalendarController {
    @Autowired
    private UserService userService

    @Autowired
    private EventRepository eventRepository

    @RequestMapping(value="/calendar/{date}", method = RequestMethod.GET)
    public ModelAndView calender(@PathVariable String date, @RequestParam(required = false,  value="team") String team) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName())
        String username = "null"
        List<Event> events;

        Map<String, String> map = auth.principal
        println(map)

        Map.Entry<String, String> entry = map.entrySet().iterator().next();
        String key = entry.getKey()
        String slackName = entry.getValue()
        System.out.println(key) //literal name
        System.out.println(slackName) //their name

        username = slackName

//        if (team == null) {
//            team = "ALL"
//        }

        println(team)

        //User user = userService.findUserByEmail(auth.getName())
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        String[] dateArray = date.split("-")
        String month = dateArray[0]
        String day = dateArray[1]
        String year = dateArray[2]

        int monthNum = Integer.parseInt(month)
        int dayNum = Integer.parseInt(day)
        int yearNum = Integer.parseInt(year)

        if (team != null) {
            events = eventRepository.findByTeam(team)
        }else{
            events = eventRepository.findAll()
        }
        // events.add("{title: 'beer chugging contest', start: '04-29-2018'}")

        println(events)

        modelAndView.addObject("userName", username)
        modelAndView.addObject("team", team)
        modelAndView.addObject("Date", timeStamp)
        modelAndView.addObject("month", month)
        modelAndView.addObject("day", day)
        modelAndView.addObject("year", year)
        modelAndView.addObject("monthNum", monthNum)
        modelAndView.addObject("dayNum", dayNum)
        modelAndView.addObject("events", events)



        modelAndView.setViewName("fullcalendar/demos/basic-views")
        return modelAndView
    }

    @RequestMapping(value="/calendar/{dateUpdate}/edit", method = RequestMethod.GET)
    public ModelAndView calendarEdit(@PathVariable String dateUpdate){
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        String userEmail = "null"
            Map<String,String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if(userInfo.getKey() == "name"){
                username = userInfo.getValue()
            }else if(userInfo.getKey() == "email"){
                userEmail = userInfo.getValue()
            }
        }

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        Event event = new Event()

        /*
        String[] dateArray = date.split("-")
        String month = dateArray[0]
        String day = dateArray[1]
        String year = dateArray[2]

        int monthNum = Integer.parseInt(month)
        int dayNum = Integer.parseInt(day)
        int yearNum = Integer.parseInt(year)
*/

        modelAndView.addObject("event", event)
        modelAndView.addObject("userName", username)
        //modelAndView.addObject("events", events)
        modelAndView.addObject("dateUpdate", dateUpdate)
        modelAndView.addObject("curDate", timeStamp)



        modelAndView.setViewName("edit")
        return modelAndView
    }

    @RequestMapping(value="/calendar/{dateUpdate}/edit", method = RequestMethod.POST)
    public ModelAndView calendarPostEdit(@PathVariable String dateUpdate, @Valid Event event, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        String username = "null"
        String userEmail = "null"
        Map<String,String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if(userInfo.getKey() == "name"){
                username = userInfo.getValue()
            }else if(userInfo.getKey() == "email"){
                userEmail = userInfo.getValue()
            }
        }

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        LocalDate fancyDate = LocalDate.parse(dateUpdate, DateTimeFormatter.ofPattern("MM-dd-yyyy"))
        String reformattedDate = fancyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("edit");
        } else {
            event.setEmail(userEmail);
            event.setName(username)
            event.setDate(reformattedDate)
            event.title = event.title
            eventRepository.save(event);
            modelAndView.addObject("successMessage", "Event has been registered successfully");
            //modelAndView.addObject("event", new Event());
            modelAndView.addObject("event", event);
            modelAndView.addObject("userName", username)
           // modelAndView.addObject("events", events)
            modelAndView.addObject("dateUpdate", dateUpdate)
            modelAndView.addObject("curDate", timeStamp)
            modelAndView.setViewName("edit")
        }

        return modelAndView
    }

    @RequestMapping(value="/calendar/edit/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventEdit(@PathVariable int eventId){
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        Map<String,String> map = auth.principal

        Map.Entry<String,String> entry = map.entrySet().iterator().next();
        String key= entry.getKey()
        String slackName=entry.getValue()
        username = slackName

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        Event event = eventRepository.findById(eventId)
        String eventDate = event.date

        modelAndView.addObject("event", event)
        modelAndView.addObject("eventDate", eventDate)
        modelAndView.addObject("userName", username)
        //modelAndView.addObject("events", events)
        modelAndView.addObject("eventId", eventId)
        modelAndView.addObject("curDate", timeStamp)



        modelAndView.setViewName("editEvent")
        return modelAndView
    }

    @RequestMapping(value="/calendar/edit/{eventId}", method = RequestMethod.POST)
    public ModelAndView posteventEdit(@PathVariable int eventId, @Valid Event event, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        String userEmail = "null"
        Map<String,String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if(userInfo.getKey() == "name"){
                username = userInfo.getValue()
            }else if(userInfo.getKey() == "email"){
                userEmail = userInfo.getValue()
            }
        }

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

//        LocalDate fancyDate = LocalDate.parse(dateUpdate, DateTimeFormatter.ofPattern("MM-dd-yyyy"))
//        String reformattedDate = fancyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("editEvent");
        } else {
            //event.setEmail(user.getEmail());
            event.id = eventId
            event.name = username
            event.email = userEmail
            event.title = event.title
            eventRepository.save(event);
            modelAndView.addObject("successMessage", "Event has been edited successfully");
            //modelAndView.addObject("event", new Event());
            modelAndView.addObject("event", event);
            modelAndView.addObject("userName", username)
            // modelAndView.addObject("events", events)
//            modelAndView.addObject("dateUpdate", dateUpdate)
            modelAndView.addObject("curDate", timeStamp)
            modelAndView.setViewName("editEvent")
        }
        return modelAndView
    }

    @RequestMapping(value="/calendar/view/{eventId}", method = RequestMethod.GET)
    public ModelAndView viewEvent(@PathVariable int eventId){
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        Map<String,String> map = auth.principal

        Map.Entry<String,String> entry = map.entrySet().iterator().next();
        String key= entry.getKey()
        String slackName=entry.getValue()
        username = slackName

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        Event event = eventRepository.findById(eventId)
        /*
        String[] dateArray = date.split("-")
        String month = dateArray[0]
        String day = dateArray[1]
        String year = dateArray[2]

        int monthNum = Integer.parseInt(month)
        int dayNum = Integer.parseInt(day)
        int yearNum = Integer.parseInt(year)
*/

        modelAndView.addObject("event", event)
        modelAndView.addObject("userName", username)
        //modelAndView.addObject("events", events)
        modelAndView.addObject("curDate", timeStamp)



        modelAndView.setViewName("viewEvent")
        return modelAndView
    }

}
