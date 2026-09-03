package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.NoteSharing;
import com.example.SocialMedia.payload.request.NoteSharingRequest;
import com.example.SocialMedia.payload.response.NoteSharingResponse;
import com.example.SocialMedia.repository.NoteSharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotSharingService {
    @Autowired
    private NoteSharingRepository noteSharingRepository;

    public String createNote(NoteSharingRequest noteSharingRequest){
        NoteSharing noteSharing = convertToRequest(noteSharingRequest);
        noteSharingRepository.save(noteSharing);
        return "Note created successfully";
    }

    public NoteSharing convertToRequest(NoteSharingRequest noteSharingRequest) {
        NoteSharing noteSharing = new NoteSharing();
        noteSharing.setAuthor(noteSharingRequest.getAuthor());
        noteSharing.setTitle(noteSharingRequest.getTitle());
        noteSharing.setDescription(noteSharingRequest.getDescription());
        noteSharing.setAuthorId(noteSharingRequest.getAuthorId());
        noteSharing.setAuthorPassword(noteSharingRequest.getAuthorPassword());
        return noteSharing;
    }

    public NoteSharingResponse convertToResponse(NoteSharing noteSharing) {
        NoteSharingResponse noteSharingResponse = new NoteSharingResponse();

        noteSharingResponse.setAuthor(noteSharing.getAuthor());
        noteSharingResponse.setTitle(noteSharing.getTitle());
        noteSharingResponse.setDescription(noteSharing.getDescription());

        return noteSharingResponse;
    }

    public NoteSharingResponse findByID(Long Id) {
        NoteSharing note = noteSharingRepository.findById(Id).get();
        NoteSharingResponse noteSharingResponse = convertToResponse(note);
        return noteSharingResponse;
    }

    public List<NoteSharingResponse> findAllInfo() {
        List<NoteSharing> noteSharings = noteSharingRepository.findAll();

        List<NoteSharingResponse> noteSharingResponses = new ArrayList<>();

        for(NoteSharing noteSharing : noteSharings) {
            NoteSharingResponse noteSharingResponse = convertToResponse(noteSharing);
            noteSharingResponses.add(noteSharingResponse);
        }

        return noteSharingResponses;
    }

    public String deleteNote(Long Id) {
        noteSharingRepository.deleteById(Id);
        return "Note deleted successfully";
    }
}
