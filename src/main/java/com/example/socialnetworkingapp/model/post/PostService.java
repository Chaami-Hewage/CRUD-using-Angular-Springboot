package com.example.socialnetworkingapp.model.post;

import com.example.socialnetworkingapp.mapper.PostMapper;
import com.example.socialnetworkingapp.model.account.Account;
import com.example.socialnetworkingapp.model.comment.CommentService;
import com.example.socialnetworkingapp.model.like.LikeService;
import com.example.socialnetworkingapp.model.post_view.PostViewRepository;
import com.example.socialnetworkingapp.model.post_view.PostViewService;
import com.example.socialnetworkingapp.model.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final LikeService likeService;
    private final CommentService commentService;
    private final PostViewService postViewService;
    private final PostViewRepository postViewRepository;

    public PostResponse addPost(Post post) {

        List<Post> saved = new ArrayList<>();
        saved.add(this.postRepository.save(post));
        return saved.stream().map(postMapper::PostToPostResponse).collect(Collectors.toList()).get(0);
    }

    public List<PostResponse> findAllPosts(Account user) {
        // Get user's network
        List<Account> userNetwork = user.getNetwork();

        // Get posts by user
        List<PostResponse> postsToReturn = this.findPostsByAuthorId(user.getId());

        // If user has no friends, return only his/her posts
        if (userNetwork.isEmpty()) {
            return postsToReturn
                    .stream()
                    .sorted(Comparator.comparing(PostResponse::getDate).reversed())
                    .collect(Collectors.toList());
        }

        // Get posts of all connections in user's network
        for (Account contact : userNetwork) {

            List<PostResponse> friendPosts = this.findPostsByAuthorId(contact.getId());
            if (!friendPosts.isEmpty()) {
                postsToReturn.addAll(friendPosts);
            }
        }

        // Return latest posts first
        return postsToReturn
                .stream()
                .sorted(Comparator.comparing(PostResponse::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<PostResponse> findAllPostsByTag(Account user, Long tagId) {
        // Get user's network
        List<Account> userNetwork = user.getNetwork();

        // Get posts by user
        List<PostResponse> postsToReturn = this.findPostsByAuthorIdAndTagId(user.getId(), tagId);

        // If user has no friends, return only his/her posts
        if (userNetwork.isEmpty()) {
            return postsToReturn
                    .stream()
                    .sorted(Comparator.comparing(PostResponse::getDate).reversed())
                    .collect(Collectors.toList());
        }

        // Get posts of all connections in user's network
        for (Account contact : userNetwork) {

            List<PostResponse> friendPosts = this.findPostsByAuthorIdAndTagId(contact.getId(), tagId);
            if (!friendPosts.isEmpty()) {
                postsToReturn.addAll(friendPosts);
            }
        }

        // Return latest posts first
        return postsToReturn
                .stream()
                .sorted(Comparator.comparing(PostResponse::getDate).reversed())
                .collect(Collectors.toList());
    }


    public List<PostResponse> getAllReportedPosts() {
        List<PostResponse> postsToReturn = this.findAllReportedPosts();

        return postsToReturn
                .stream()
                .sorted(Comparator.comparing(PostResponse::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<PostResponse> findPostsByAuthorId(Long authorId) {
        return this.postRepository.findPostsByAuthorId(authorId).stream().map(postMapper::PostToPostResponse).collect(Collectors.toList());
    }

    public List<PostResponse> findPostsByAuthorIdAndTagId(Long id, Long tagId) {
        return this.postRepository.findPostsByAuthorIdAndField(id, tagId).stream().map(postMapper::PostToPostResponse).collect(Collectors.toList());
    }

    public List<PostResponse> findAllReportedPosts() {
        return this.postRepository.findReportedPosts().stream().map(postMapper::PostToPostResponse).collect(Collectors.toList());
    }

    public void deletePost(Long id) {
        this.postRepository.deletePostById(id);
    }

    public Post reportPost(Post p) {
        Optional<Post> postPresent = this.postRepository.findPostById(p.getId());
        if (!postPresent.isPresent()) {
            throw new IllegalStateException("Post with id " + p.getId() + " does not exist!");
        }

        Post post = postPresent.get();
        post.setReported(true);
        return this.postRepository.save(post);
    }

    public Post findPostById(Long id) {
        return postRepository.findPostById(id).orElseThrow(() -> new IllegalStateException("Post with id " + id.toString() + " was not found !"));
    }

    public Post updatePost(Post p) {

        Optional<Post> postPresent = this.postRepository.findPostById(p.getId());
        if (!postPresent.isPresent()) {
            throw new IllegalStateException("Post with id " + p.getId() + " does not exist!");
        }

        Post post = postPresent.get();
        post.setPayload(p.getPayload());
        post.setAuthor(p.getAuthor());
        post.setDate(p.getDate());
        post.setImagePath(p.getImagePath());
        post.setVideoPath(p.getVideoPath());
        post.setSoundPath(p.getSoundPath());
        return this.postRepository.save(post);
    }
}
