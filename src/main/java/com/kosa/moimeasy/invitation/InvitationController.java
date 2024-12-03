package com.kosa.moimeasy.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitations")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public String sendInvitation(@RequestBody EmailRequest request) {
        Long userId = 2L; 
        return invitationService.sendInvitation(userId, request);
    }
}
