package com.example.socialnetworkingapp.model.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    void deletePostById(Long id);
    Optional<Post> findPostById(Long id);

    @Query("SELECT p FROM Post p WHERE p.author.id = ?1")
    List<Post> findPostsByAuthorId(Long id);

    @Query("SELECT p FROM Post p WHERE p.isReported = true")
    List<Post> findReportedPosts();

    @Query("UPDATE Post p SET p.isReported = true WHERE p.id = ?1")
    void reportPostById(Long id);

    @Query("SELECT p FROM Post p WHERE p.author.id = ?1 AND p.tag.id = ?2")
    List<Post> findPostsByAuthorIdAndField(Long id, Long tagId);
}
