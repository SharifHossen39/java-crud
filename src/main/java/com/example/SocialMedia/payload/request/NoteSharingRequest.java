package com.example.SocialMedia.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteSharingRequest {

    private String title;
    private String description;
    private String author;
    private String authorId;
    private String authorPassword;

}
