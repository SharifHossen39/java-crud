package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserWalletRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void savingUserCascadesToItsWallet() {
        User user = new User();
        user.setUsername("wallet-test-user");
        user.setEmail("wallet@example.com");

        Wallet wallet = new Wallet();
        wallet.setWalletNumber("WALLET-1001");
        wallet.setBalance(new BigDecimal("500.00"));
        wallet.setCurrency("BDT");
        wallet.setStatus("ACTIVE");

        user.setWallet(wallet);
        wallet.setUser(user);

        User savedUser = userRepository.saveAndFlush(user);

        Wallet savedWallet = walletRepository.findAll().get(0);
        assertThat(savedWallet.getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(savedUser.getWallet()).isSameAs(wallet);
    }
}
