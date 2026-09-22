package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }

        if (user.getWallet() != null) {
            user.getWallet().setUser(user);
        }

        userRepository.save(user);
    }
}
