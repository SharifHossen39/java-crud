package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.UserProfile;
import com.example.SocialMedia.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/createUserProfile")
    private String createUser(@RequestBody UserProfile userProfile) {
        userProfileService.createUserProfile(userProfile);

        return "User Profile created successfully";
    }
}
