package com.example.socialnetworkingapp.model.account;

import com.example.socialnetworkingapp.model.bio.Bio;
import com.example.socialnetworkingapp.model.education.Education;
import com.example.socialnetworkingapp.model.tags.Tag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AccountResponse {

    private final Long id;
    private final AccountRole appUserRole;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final Long tagId;
    private final String tagName;
    private final String imageUrl;
    private final LocalDate dateCreated;

    @JsonSerialize(using = CustomNetworkSerializer.class)
    private final List<Account> network;
    private final List<Education> education;
    private final Bio bio;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.appUserRole = account.getRole();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.tagId = account.getTag().getId();
        this.tagName = account.getTag().getTag();
        this.imageUrl = account.getImageUrl();
        this.dateCreated = account.getDateCreated();
        this.network = account.getNetwork();
        this.education = account.getEducation();
        this.bio = account.getBio();
    }
}