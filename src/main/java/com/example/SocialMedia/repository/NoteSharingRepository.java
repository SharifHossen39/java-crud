package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.NoteSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteSharingRepository extends JpaRepository<NoteSharing, Long> {
}
