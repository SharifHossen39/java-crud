package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.NoteSharing;
import com.example.SocialMedia.repository.NoteSharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotSharingService {
    @Autowired
    private NoteSharingRepository noteSharingRepository;

    public String createNote(NoteSharing noteSharing){
        noteSharingRepository.save(noteSharing);
        return "Note created successfully";
    }

    public NoteSharing findByID(Long Id) {
        NoteSharing note = noteSharingRepository.findById(Id).get();
        return note;
    }

    public List<NoteSharing> findAllInfo() {
        return noteSharingRepository.findAll();
    }

    public String deleteNote(Long Id) {
        noteSharingRepository.deleteById(Id);
        return "Note deleted successfully";
    }
}
