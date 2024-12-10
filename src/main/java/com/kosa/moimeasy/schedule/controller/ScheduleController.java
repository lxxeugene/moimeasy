package com.kosa.moimeasy.schedule.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScheduleController {


    @PostMapping("/events")
    public String addScheduleEvent(){

        return "ㅇㅇ";
    }

}
