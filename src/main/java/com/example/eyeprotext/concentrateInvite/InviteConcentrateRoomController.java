package com.example.eyeprotext.concentrateInvite;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.concentrateInvite.request.AddFriendToConcentrateRequest;
import com.example.eyeprotext.concentrateInvite.request.AnserInviteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/inviteConcentrateRoomController")
public class InviteConcentrateRoomController {
    private final InviteConcentrateRoomService inviteConcentrateRoomService;

    @Autowired
    public InviteConcentrateRoomController(InviteConcentrateRoomService inviteConcentrateRoomService) {
        this.inviteConcentrateRoomService = inviteConcentrateRoomService;
    }

    @PostMapping(path = "/createInviteRoom")
    public GeneralResponse creatInviteRoom(@RequestBody InviteConcentrateRoom inviteConcentrateRoom) {
        return inviteConcentrateRoomService.createInviteRoom(inviteConcentrateRoom);
    }

    @PostMapping(path = "/addFriendToInviteRoom")
    public GeneralResponse addFriendToInviteRoom(@RequestBody AddFriendToConcentrateRequest RoomIdAndReciveAccountId) {
        return inviteConcentrateRoomService.addFriendToInviteRoom(RoomIdAndReciveAccountId);
    }

    @PostMapping(path = "/anserInvite")
    public GeneralResponse anserInvite(@RequestBody AnserInviteRequest anserInvite) {
        return inviteConcentrateRoomService.anserInvite(anserInvite);
    }

    @PostMapping(path = "/refreshInviteRoomInfo")
    public GeneralResponse refreshInviteRoomInfo(@RequestBody UUID inviteRoomId) {
        return inviteConcentrateRoomService.refreshInviteRoomInfo(inviteRoomId);
    }



}
