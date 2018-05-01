package com.example.controller

import com.example.model.Event
import com.example.model.Team
import com.example.model.User
import com.example.repository.EventRepository
import com.example.repository.TeamRepository
import com.example.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

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

    @Autowired
    private TeamRepository teamRepository

    @RequestMapping(value = "/calendar/{date}", method = RequestMethod.GET)
    public ModelAndView calender(
            @PathVariable String date, @RequestParam(required = false, value = "team") String team) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        String username = "null"
        List<Event> events;

        if(auth.principal != "anonymousUser") {
            Map<String, String> map = auth.principal
            println(map)

            Map.Entry<String, String> entry = map.entrySet().iterator().next();
            String key = entry.getKey()
            String slackName = entry.getValue()
            System.out.println(key) //literal name
            System.out.println(slackName) //their name

            username = slackName
        }else{
            username = "Anonymousss"
        }
//        if (team == null) {
//            team = "ALL"
//        }

        println(team)

        //User user = userService.findUserByEmail(auth.getName())
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())
        List<Team> teams = teamRepository.findAll()

        String[] dateArray = date.split("-")
        String month = dateArray[0]
        String day = dateArray[1]
        String year = dateArray[2]

        int monthNum = Integer.parseInt(month)
        int dayNum = Integer.parseInt(day)
        int yearNum = Integer.parseInt(year)

        if (team != null) {
            events = eventRepository.findByTeam(team)
        } else {
            events = eventRepository.findAll()
        }
        // events.add("{title: 'beer chugging contest', start: '04-29-2018'}")

        println(events)

        events.each {e -> e.teams.each {t -> t.events = []}}

        modelAndView.addObject("userName", username)
        modelAndView.addObject("team", team)
        modelAndView.addObject("teams", teams)
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

    @RequestMapping(value = "/calendar/{dateUpdate}/edit", method = RequestMethod.GET)
    public ModelAndView calendarEdit(@PathVariable String dateUpdate) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        String userEmail = "null"
        Map<String, String> map = auth.principal

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            } else if (userInfo.getKey() == "email") {
                userEmail = userInfo.getValue()
            }
        }

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        Event event = new Event()

        List<Team> teams = teamRepository.findAll()

        modelAndView.addObject("event", event)
        modelAndView.addObject("userName", username)
        //modelAndView.addObject("events", events)
        modelAndView.addObject("dateUpdate", dateUpdate)
        modelAndView.addObject("curDate", timeStamp)

        modelAndView.addObject("teams", teams)

        modelAndView.setViewName("edit")
        return modelAndView
    }

    @RequestMapping(value = "/calendar/{dateUpdate}/edit", method = RequestMethod.POST)
    public ModelAndView calendarPostEdit(
            @PathVariable String dateUpdate,
            @RequestParam(value = "teamm8") String teamName,@RequestParam(value = "datem8") String dateAndTime, @RequestParam(value = "datem82") String endDateAndTime, @Valid Event event, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        String username = "null"
        String userEmail = "null"
        Map<String, String> map = auth.principal

        String[] teams = teamName.split(",")



        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            } else if (userInfo.getKey() == "email") {
                userEmail = userInfo.getValue()
            }
        }

        println(dateAndTime)
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        LocalDate fancyDate = LocalDate.parse(dateUpdate, DateTimeFormatter.ofPattern("MM-dd-yyyy"))
        String reformattedDate = fancyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        String reformattedDate2;
        String reformattedDate3;
        if (dateAndTime.length() > 11) {
            LocalDateTime fancyDate2 = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a"))
            reformattedDate2 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        }else{
            LocalDate fancyDate2 = LocalDate.parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            reformattedDate2 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        if (endDateAndTime.length() > 11) {
            LocalDateTime fancyDate2 = LocalDateTime.parse(endDateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a"))
            reformattedDate3 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        }else if(endDateAndTime != null && endDateAndTime != ''){
            LocalDate fancyDate2 = LocalDate.parse(endDateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            reformattedDate3 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        println(teamName)
        if (bindingResult.hasErrors()) {
            println("there was an error")
            modelAndView.setViewName("edit");
        } else {
            println("trying to save")
            event.setEmail(userEmail);
            event.setName(username)
            event.setDate(reformattedDate2)
            event.setEnddate(reformattedDate3)
            event.title = event.title
            for(String t : teams){
                def team = teamRepository.findByteamname(t)
                team.addEvent(event);
                event = eventRepository.save(event);
                teamRepository.save(team)
            }
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

    @RequestMapping(value = "/calendar/edit/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventEdit(@PathVariable int eventId) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        Map<String, String> map = auth.principal

        boolean hasEndDate;
        boolean hasTime

        Map.Entry<String, String> entry = map.entrySet().iterator().next();
        String key = entry.getKey()
        String slackName = entry.getValue()
        username = slackName

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())
        List<Team> teams = teamRepository.findAll()

        Event event = eventRepository.findById(eventId)
        String eventDate = event.date

        hasEndDate = (event.enddate!=null)
        hasTime = event.date.contains("T")


        modelAndView.addObject("event", event)
        modelAndView.addObject("teams", teams);
        modelAndView.addObject("eventDate", eventDate)
        modelAndView.addObject("userName", username)
        //modelAndView.addObject("events", events)
        modelAndView.addObject("eventId", eventId)
        modelAndView.addObject("curDate", timeStamp)
        modelAndView.addObject("hasEndDate", hasEndDate)
        modelAndView.addObject("hasTime", hasTime)



        modelAndView.setViewName("editEvent")
        return modelAndView
    }

    @RequestMapping(value = "/calendar/edit/{eventId}", method = RequestMethod.POST)
    public ModelAndView posteventEdit(@PathVariable int eventId, @Valid Event event,@RequestParam(value = "datem8") String dateAndTime, @RequestParam(value = "datem82") String endDateAndTime, @RequestParam(value = "teamm8") String teamName, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        String userEmail = "null"
        Map<String, String> map = auth.principal

        String[] teams = teamName.split(",")

        for (Map.Entry<String, String> userInfo : map.entrySet()) {
            if (userInfo.getKey() == "name") {
                username = userInfo.getValue()
            } else if (userInfo.getKey() == "email") {
                userEmail = userInfo.getValue()
            }
        }

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())


        String reformattedDate2;
        String reformattedDate3;
        if (dateAndTime.length() > 11) {
            LocalDateTime fancyDate2 = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a"))
            reformattedDate2 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        }else{
            LocalDate fancyDate2 = LocalDate.parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            reformattedDate2 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        if (endDateAndTime.length() > 11) {
            LocalDateTime fancyDate2 = LocalDateTime.parse(endDateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm a"))
            reformattedDate3 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        }else  if(endDateAndTime != null && endDateAndTime != ''){
            LocalDate fancyDate2 = LocalDate.parse(endDateAndTime, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            reformattedDate3 = fancyDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }


        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("editEvent");
        } else {
            //event.setEmail(user.getEmail());
            event.id = eventId
            event.name = username
            event.email = userEmail
            event.title = event.title
            event.date = reformattedDate2
            event.enddate = reformattedDate3
            for(String t : teams){
                def team = teamRepository.findByteamname(t)
                team.addEvent(event);
                event = eventRepository.save(event);
                teamRepository.save(team)
            }
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

    @RequestMapping(value = "/calendar/view/{eventId}", method = RequestMethod.GET)
    public ModelAndView viewEvent(@PathVariable int eventId) {
        ModelAndView modelAndView = new ModelAndView()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        User userExists = userService.findUserByEmail(auth.getName());
        String username = "null"
        Map<String, String> map = auth.principal

        Boolean hasTeams;
        String teamnames
        Boolean hasend = false;

        Map.Entry<String, String> entry = map.entrySet().iterator().next();
        String key = entry.getKey()
        String slackName = entry.getValue()
        username = slackName

        String timeStamp = new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime())

        Event event = eventRepository.findById(eventId)
        hasTeams = event.teams.size() > 1

        if(hasTeams){
//            event.teams.each {t ->
//                teamnames += t.teamname + ", "}
            teamnames = event.teams.collect({Team t -> t.teamname}).join(', ')
        }else{
            teamnames = event.teams[0].teamname
        }

        if(event.enddate != null){
            hasend = true
        }

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
        modelAndView.addObject("hasTeams", hasTeams)
        modelAndView.addObject("teamnames", teamnames)
        modelAndView.addObject("hasend", hasend)



        modelAndView.setViewName("viewEvent")
        return modelAndView
    }

}
