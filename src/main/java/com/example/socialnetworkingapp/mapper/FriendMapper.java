package com.example.socialnetworkingapp.mapper;

import com.example.socialnetworkingapp.model.account.Account;
import com.example.socialnetworkingapp.model.message.FriendsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendMapper {


    @Mapping(target = "firstName", expression = "java(mapFirstName(account))")
    @Mapping(target = "senderLastName", expression = "java(mapLastName(account))")
    @Mapping(target = "senderEmail", expression = "java(mapEmail(account))")
    @Mapping(target = "imageUrl", expression = "java(mapImage(account))")

    default String mapFirstName(Account account) {
        return account.getFirstName();
    }

    default String mapImage(Account account) {
        return account.getImageUrl();
    }

    default String mapLastName(Account account) {
        return account.getLastName();
    }

    default String mapEmail(Account account) {
        return account.getUsername();
    }

    FriendsResponse AccountToFriend(Account account);
}
