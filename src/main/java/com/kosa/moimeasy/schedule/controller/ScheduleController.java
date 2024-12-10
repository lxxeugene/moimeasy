package com.kosa.moimeasy.schedule.controller;


import com.sun.jdi.request.EventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ScheduleController {


    @PostMapping("/events")
    public ResponseEntity<String> addEvent(@RequestBody EventRequest eventRequest) {
        // eventRequest 객체에 클라이언트로부터 전달된 데이터가 매핑됨
        System.out.println("이벤트 데이터 수신: " + eventRequest);
        // 필요한 로직 처리 후 응답
        return ResponseEntity.ok("이벤트 추가 성공");
    }

}
