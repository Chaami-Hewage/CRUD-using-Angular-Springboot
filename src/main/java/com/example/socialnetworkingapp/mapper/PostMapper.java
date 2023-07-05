package com.example.socialnetworkingapp.mapper;

import com.example.socialnetworkingapp.model.account.Account;
import com.example.socialnetworkingapp.model.post.Post;
import com.example.socialnetworkingapp.model.post.PostResponse;
import com.example.socialnetworkingapp.model.tags.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "authorFirstName", expression = "java(mapFirstName(post.getAuthor()))")
    @Mapping(target = "authorLastName", expression = "java(mapLastName(post.getAuthor()))")
    @Mapping(target = "authorEmail", expression = "java(mapEmail(post.getAuthor()))")
    @Mapping(target = "authorImage", expression = "java(mapImage(post.getAuthor()))")
    @Mapping(target = "tagId", expression = "java(mapTagId(post.getTag()))")
    @Mapping(target = "tagName", expression = "java(mapTagName(post.getTag()))")
    PostResponse PostToPostResponse(Post post);

    default String mapFirstName(Account account){
        return account.getFirstName();
    }
    default String mapLastName(Account account){
        return account.getLastName();
    }
    default String mapEmail(Account account){
        return account.getUsername();
    }
    default String mapImage(Account account){
        return account.getImageUrl();
    }
    default Long mapTagId(Tag tag){
        return tag.getId();
    }
    default String mapTagName(Tag tag){
        return tag.getTag();
    }

}
