package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.Wallet;
import com.example.SocialMedia.payload.response.UserWalletInfoResponse;
import com.example.SocialMedia.repository.RoleRepository;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void createUser(User user) {
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }

        if (user.getWallet() != null) {
            user.getWallet().setUser(user);
        }

        user.setRoles(resolveRoles(user.getRoles()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findDetailsById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Wallet not found for user id: " + userId));
    }

    @Transactional(readOnly = true)
    public UserWalletInfoResponse getUserInfoByWalletNumber(String walletNumber) {
        return userRepository.findUserInfoByWalletNumber(walletNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found for wallet number: " + walletNumber));
    }

    private Set<Role> resolveRoles(Set<Role> requestedRoles) {
        Set<Role> resolvedRoles = new HashSet<>();

        if (requestedRoles == null) {
            return resolvedRoles;
        }

        for (Role requestedRole : requestedRoles) {
            String roleName = requestedRole.getName().trim().toUpperCase(Locale.ROOT);
            Role role = roleRepository.findByNameIgnoreCase(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        return roleRepository.save(newRole);
                    });
            resolvedRoles.add(role);
        }

        return resolvedRoles;
    }
}
