package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByTransactionReference(String transactionReference);

    Optional<Transaction> findByTransactionReference(String transactionReference);

    @Query("SELECT t FROM Transaction t " +
           "JOIN FETCH t.senderWallet sw " +
           "JOIN FETCH sw.user su " +
           "JOIN FETCH t.receiverWallet rw " +
           "JOIN FETCH rw.user ru " +
           "WHERE sw.user.id = :userId OR rw.user.id = :userId " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);
}
