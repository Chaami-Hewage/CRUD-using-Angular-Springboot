package com.example.socialnetworkingapp.model.post;

import com.example.socialnetworkingapp.model.account.Account;
import com.example.socialnetworkingapp.model.account.AccountRole;
import com.example.socialnetworkingapp.model.account.AccountService;
import com.example.socialnetworkingapp.model.comment.CommentResponse;
import com.example.socialnetworkingapp.model.comment.CommentService;
import com.example.socialnetworkingapp.model.like.LikeResponse;
import com.example.socialnetworkingapp.model.like.LikeService;
import com.example.socialnetworkingapp.model.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final AccountService accountService;
    private final LikeService likeService;
    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        Tag tag = new Tag((long) request.getTagId(), "");
        Post newPost = new Post(request.getPayload(), newUser, new Date(), tag, null, null, null, false);
        return new ResponseEntity<>(postService.addPost(newPost), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        if (newUser.getRole().equals(AccountRole.ADMIN) || newUser.getRole().equals(AccountRole.PREMIUM_USER)) {
            return new ResponseEntity<>(postService.findAllPosts(newUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all/{tagId}")
    public ResponseEntity<List<PostResponse>> getAllPostsByTag(@PathVariable("tagId") Long tagId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        return new ResponseEntity<>(postService.findAllPostsByTag(newUser, tagId), HttpStatus.OK);
    }

    @GetMapping("/all-reported")
    public ResponseEntity<List<PostResponse>> getAllReportedPostsForAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        if (newUser.getRole().equals(AccountRole.ADMIN))
            return new ResponseEntity<>(postService.getAllReportedPosts(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PutMapping("/update/{pid}")
    public ResponseEntity<HttpStatus> updatePostPayload(@PathVariable("pid") Long postId,
                                                        @RequestBody String payload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        Post existingPost = this.postService.findPostById(postId);
        existingPost.setPayload(payload);
        this.postService.updatePost(existingPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        for (LikeResponse like : this.likeService.findAllLikesOfPost(id)) {
            this.likeService.deleteLikeById(like.getId());
        }
        for (CommentResponse cm : this.commentService.findAllCommentsOfPost(id)) {
            this.commentService.deleteById(cm.getId());
        }
        this.postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/report/{pid}")
    public ResponseEntity<HttpStatus> reportPostById(@PathVariable("pid") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account newUser = accountService.findAccountByEmail(user);
        Post existingPost = this.postService.findPostById(postId);
        this.postService.reportPost(existingPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
