package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.payload.response.RoleUserResponse;
import com.example.SocialMedia.repository.RoleRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Role createRole(Role requestedRole) {
        if (requestedRole.getName() == null || requestedRole.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role name is required");
        }

        String roleName = requestedRole.getName().trim();
        if (roleRepository.findByNameIgnoreCase(roleName).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Role already exists: " + roleName);
        }

        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public List<RoleUserResponse> getUsersByRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role name is required");
        }

        Role role = roleRepository.findByNameIgnoreCase(roleName.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found: " + roleName));

        return userRepository.findUsersByRoleName(role.getName());
    }
}
