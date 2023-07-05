package com.example.socialnetworkingapp.model.post;

import com.example.socialnetworkingapp.model.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String payload;
    private String authorFirstName;
    private String authorLastName;
    private String authorEmail;
    private String authorImage;
    private String date;
    private Long tagId;
    private String tagName;
    private String imagePath;
    private String videoPath;
    private String soundPath;
}
