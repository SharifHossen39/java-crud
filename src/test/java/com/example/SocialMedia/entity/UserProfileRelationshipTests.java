package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.UserProfileRepository;
import com.example.SocialMedia.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserProfileRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savingUserCascadesToItsProfile() {
        User user = new User();
        user.setUsername("relationship-test-user");
        user.setEmail("relationship@example.com");

        UserProfile profile = new UserProfile();
        profile.setFirstName("Test");
        profile.setLastName("User");

        user.setProfile(profile);
        profile.setUser(user);

        User savedUser = userRepository.saveAndFlush(user);
        Long savedUserId = savedUser.getId();
        Long savedProfileId = profile.getId();
        entityManager.clear();

        User reloadedUser = userRepository.findById(savedUserId).orElseThrow();
        assertThat(reloadedUser.getProfile().getId()).isEqualTo(savedProfileId);
        UserProfile reloadedProfile = userProfileRepository.findById(savedProfileId).orElseThrow();
        assertThat(reloadedProfile.getUser().getId()).isEqualTo(savedUserId);
    }

    @Test
    void existingProfileCanRemainUnlinkedDuringRelationshipMigration() {
        UserProfile existingProfile = new UserProfile();
        existingProfile.setFirstName("Existing");
        existingProfile.setLastName("Profile");

        UserProfile savedProfile = userProfileRepository.saveAndFlush(existingProfile);

        assertThat(savedProfile.getId()).isNotNull();
    }
}
