package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.payload.response.RoleUserResponse;
import com.example.SocialMedia.payload.response.UserWalletInfoResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"profile", "wallet", "roles"})
    Optional<User> findDetailsById(Long id);

    @Query("SELECT new com.example.SocialMedia.payload.response.UserWalletInfoResponse(u.username, u.phoneNumber) " +
           "FROM User u WHERE u.wallet.walletNumber = :walletNumber")
    Optional<UserWalletInfoResponse> findUserInfoByWalletNumber(@Param("walletNumber") String walletNumber);

    @Query("SELECT new com.example.SocialMedia.payload.response.RoleUserResponse(" +
           "CONCAT(CONCAT(p.firstName, ' '), p.lastName), p.gender, u.email) " +
           "FROM User u JOIN u.profile p JOIN u.roles r " +
           "WHERE LOWER(r.name) = LOWER(:roleName) ORDER BY u.id")
    List<RoleUserResponse> findUsersByRoleName(@Param("roleName") String roleName);
}
