package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.RoleRepository;
import com.example.SocialMedia.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRoleRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void usersAndRolesHaveManyToManyRelationship() {
        Role userRole = new Role();
        userRole.setName("USER");
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.saveAllAndFlush(List.of(userRole, adminRole));

        User firstUser = new User();
        firstUser.setUsername("first-user");
        firstUser.getRoles().addAll(List.of(userRole, adminRole));

        User secondUser = new User();
        secondUser.setUsername("second-user");
        secondUser.getRoles().add(userRole);
        userRepository.saveAllAndFlush(List.of(firstUser, secondUser));

        Long firstUserId = firstUser.getId();
        Long userRoleId = userRole.getId();
        entityManager.clear();

        User reloadedUser = userRepository.findById(firstUserId).orElseThrow();
        Role reloadedRole = roleRepository.findById(userRoleId).orElseThrow();
        assertThat(reloadedUser.getRoles())
                .extracting(Role::getName)
                .containsExactlyInAnyOrder("USER", "ADMIN");
        assertThat(reloadedRole.getUsers()).hasSize(2);
    }
}
