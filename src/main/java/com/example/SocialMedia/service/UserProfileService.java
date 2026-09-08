package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.UserProfile;
import com.example.SocialMedia.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public void createUserProfile(UserProfile userProfile){
        userProfileRepository.save(userProfile);
    }
}
