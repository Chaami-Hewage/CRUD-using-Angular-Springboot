package com.example.socialnetworkingapp.registration;

import com.example.socialnetworkingapp.model.tags.Tag;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private String phone = "";
    private String accountType = "";
    private int tagId = 0;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String firstName, String lastName, String email, String password, String phone, String accountType, int tagId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.accountType = accountType;
        this.tagId = tagId;
    }
}
