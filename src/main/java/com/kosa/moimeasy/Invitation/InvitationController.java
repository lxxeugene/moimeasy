package com.kosa.moimeasy.Invitation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitations")
@CrossOrigin(origins = "http://localhost:3000")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public String sendInvitation(@RequestBody EmailRequest request) {
        
        Long userId = 5L; 
        invitationService.sendInvitation(userId, request, request.getHtmlContent());
        return "초대가 성공적으로 전송되었습니다.";
    }

    @GetMapping
    public List<Invitation> getInvitations() {
        return invitationService.getAllInvitations();
    }
}
