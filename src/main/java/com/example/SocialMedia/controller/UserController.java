package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.Wallet;
import com.example.SocialMedia.payload.response.TransactionResponse;
import com.example.SocialMedia.payload.response.UserWalletInfoResponse;
import com.example.SocialMedia.service.TransactionService;
import com.example.SocialMedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) {
        userService.createUser(user);
        return "User created successfully";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{userId}/wallet")
    public Wallet getWalletByUserId(@PathVariable Long userId) {
        return userService.getWalletByUserId(userId);
    }

    @GetMapping("/by-wallet/{walletNumber}")
    public UserWalletInfoResponse getUserByWalletNumber(@PathVariable String walletNumber) {
        return userService.getUserInfoByWalletNumber(walletNumber);
    }

    @GetMapping("/{userId}/transactions")
    public List<TransactionResponse> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }
}
