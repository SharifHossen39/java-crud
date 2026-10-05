package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createRole(@RequestBody Role role) {
        roleService.createRole(role);

        return "Role Created Successfully";
    }
}
