package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.UserProfileRepository;
import com.example.SocialMedia.repository.UserRepository;
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

    @Test
    void savingUserCascadesToItsProfile() {
        User user = new User();
        user.setUserName("relationship-test-user");
        user.setEmail("relationship@example.com");

        UserProfile profile = new UserProfile();
        profile.setFirstName("Test");
        profile.setLastName("User");

        user.setUserProfile(profile);
        profile.setUser(user);

        User savedUser = userRepository.saveAndFlush(user);

        UserProfile savedProfile = userProfileRepository.findAll().get(0);
        assertThat(savedProfile.getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(savedUser.getUserProfile()).isSameAs(profile);
    }

    @Test
    void existingProfileCanRemainUnlinkedDuringRelationshipMigration() {
        UserProfile existingProfile = new UserProfile();
        existingProfile.setFirstName("Existing");
        existingProfile.setLastName("Profile");

        UserProfile savedProfile = userProfileRepository.saveAndFlush(existingProfile);

        assertThat(savedProfile.getId()).isNotNull();
        assertThat(savedProfile.getUser()).isNull();
    }
}
