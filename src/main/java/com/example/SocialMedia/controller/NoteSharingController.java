package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.NoteSharing;
import com.example.SocialMedia.payload.request.NoteSharingRequest;
import com.example.SocialMedia.payload.response.NoteSharingResponse;
import com.example.SocialMedia.service.NotSharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteSharingController {
    @Autowired
    private NotSharingService noteSharingService;

    @PostMapping("/create")
    private String create(@RequestBody NoteSharingRequest noteSharingRequest) {
        noteSharingService.createNote(noteSharingRequest);
        return "success";
    }

    @GetMapping("/byId")
    private NoteSharingResponse findById(@RequestParam Long id) {
        NoteSharingResponse note = noteSharingService.findByID(id);
        return note;
    }

    @GetMapping("/allInfo")
    private List<NoteSharingResponse> findallinformation() {
        return noteSharingService.findAllInfo();
    }

    @DeleteMapping("id")
    private String deleteInfo(@RequestParam Long indentity) {
        return noteSharingService.deleteNote(indentity);
    }
}
