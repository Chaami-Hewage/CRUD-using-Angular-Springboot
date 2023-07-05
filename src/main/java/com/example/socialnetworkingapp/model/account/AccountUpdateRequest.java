package com.example.socialnetworkingapp.model.account;

import com.example.socialnetworkingapp.model.bio.Bio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String imageUrl;
    private Bio bio;

    public AccountUpdateRequest(long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.phone = "";
        this.bio = null;
    }
}
