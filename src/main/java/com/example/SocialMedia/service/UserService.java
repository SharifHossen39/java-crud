package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.repository.RoleRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
