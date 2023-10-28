package com.example.eyeprotext.concentrateInvite;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.concentrateInvite.request.AddFriendToConcentrateRequest;
import com.example.eyeprotext.concentrateInvite.request.AddInviteRoomRequest;
import com.example.eyeprotext.concentrateInvite.request.RemoveInviteRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(path = "/addToInviteRoom")
    public GeneralResponse addToInviteRoom(@RequestBody AddInviteRoomRequest addInviteRoomRequest) {
        return inviteConcentrateRoomService.addToInviteRoom(addInviteRoomRequest);
    }

    @PostMapping(path = "/removeToInviteRoom")
    public GeneralResponse removeToInviteRoom(@RequestBody RemoveInviteRoomRequest removeInviteRoomRequest) {
        return inviteConcentrateRoomService.removeToInviteRoom(removeInviteRoomRequest);
    }

    @PostMapping(path = "/refreshInviteRoomMemberList")
    public GeneralResponse refreshInviteRoomMemberList(@RequestBody InviteConcentrateRoom inviteRoom) {
        return inviteConcentrateRoomService.refreshInviteRoomMemberList(inviteRoom.getInviteRoomId());
    }



}
