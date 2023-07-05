package com.example.socialnetworkingapp.model.account.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FullAccountDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String field;
    private String imageUrl;
    private String dateCreated;

    private List<AccountDetails> network;

    private List<EducationDetails> education;

    private String bio;

    private List<PostDetails> posts;
    private List<LikeDetails> likes;
    private List<CommentDetails> comments;
}