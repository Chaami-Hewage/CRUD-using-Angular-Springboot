package com.example.socialnetworkingapp.model.account;

import com.example.socialnetworkingapp.exception.UserAlreadyRegisteredException;
import com.example.socialnetworkingapp.exception.UserNotFoundException;
import com.example.socialnetworkingapp.model.account.details.*;
import com.example.socialnetworkingapp.model.bio.Bio;
import com.example.socialnetworkingapp.model.bio.BioService;
import com.example.socialnetworkingapp.model.comment.Comment;
import com.example.socialnetworkingapp.model.comment.CommentService;
import com.example.socialnetworkingapp.model.education.Education;
import com.example.socialnetworkingapp.model.education.EducationService;
import com.example.socialnetworkingapp.model.like.Like;
import com.example.socialnetworkingapp.model.like.LikeService;
import com.example.socialnetworkingapp.model.post.PostResponse;
import com.example.socialnetworkingapp.model.post.PostService;
import com.example.socialnetworkingapp.model.tags.Tag;
import com.example.socialnetworkingapp.model.tags.TagService;
import com.example.socialnetworkingapp.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TagService tagService;
    private final EducationService educationService;
    private final BioService bioService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    /*
     *   In Account, 3 fields are unique:
     *   1. Id (Generated automatically)
     *   2. Email
     *   3. Phone
     *
     * In order for us to NOT get an Internal SLQ error, we have to check for duplicates.
     */
    public Optional<Account> checkExistence(String fieldName, Optional<Account> opt) {

        if (opt.isPresent()) {
            throw new UserAlreadyRegisteredException("User with the same " + fieldName + " already exists!");
        }
        return opt;
    }

    public Account checkAccount(Account account) {

        checkExistence("email", this.accountRepository.findAccountByEmail(account.getEmail()));
        checkExistence("phone", this.accountRepository.findAccountByPhone(account.getPhone()));

        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        return account;
    }

    public Account accountSignUp(RegistrationRequest request) {
        Tag tag = new Tag((long) request.getTagId(), "");
        Account account;
        if (Objects.equals(request.getAccountType(), "premium")) {
            account = new Account(AccountRole.PREMIUM_USER, request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getPhone(), tag);
        } else {
            account = new Account(AccountRole.REGULAR_USER, request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getPhone(), tag);
        }
        return this.accountRepository.save(checkAccount(account));
    }

    public List<Account> findAllAccounts() {
        List<Account> accounts = this.accountRepository.findAll();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == 1) {
                accounts.remove(accounts.get(i));
            }
        }
        return accounts;
    }

    public List<FullAccountDetails> getFullAccountsDetails(List<Long> accountIds) throws IOException {
        List<FullAccountDetails> details = new ArrayList<>();
        for (Long id : accountIds) {
            /* Account's personal details */
            Account tempAccount = this.findAccountById(id);
            /* Account's network */
            List<AccountDetails> network = new ArrayList<>();
            for (Account friend : tempAccount.getNetwork()) {
                network.add(
                        new AccountDetails(
                                friend.getFirstName(),
                                friend.getLastName(),
                                friend.getEmail(),
                                friend.getPhone(),
                                friend.getImageUrl(),
                                friend.getDateCreated().toString()
                        )
                );
            }

            /* Account's education */
            List<EducationDetails> education = new ArrayList<>();
            for (Education edu : tempAccount.getEducation()) {
                education.add(
                        new EducationDetails(
                                edu.getSchool(),
                                edu.getDegree(),
                                edu.getField(),
                                edu.getStartDate(),
                                edu.getEndDate(),
                                edu.getGrade(),
                                edu.getDescription()
                        )
                );
            }
            /* Account's posts */
            List<PostDetails> posts = new ArrayList<>();
            for (PostResponse post : this.postService.findPostsByAuthorId(tempAccount.getId())) {
                posts.add(
                        new PostDetails(
                                post.getPayload(),
                                post.getDate(),
                                post.getImagePath(),
                                post.getVideoPath(),
                                post.getSoundPath()
                        )
                );
            }

            /* Account's likes */
            List<LikeDetails> likes = new ArrayList<>();
            for (Like like : this.likeService.findLikesByUserId(tempAccount.getId())) {
                likes.add(
                        new LikeDetails(
                                like.getDateCreated().toString(),
                                like.getPost().getAuthor().getFirstName(),
                                like.getPost().getAuthor().getLastName(),
                                like.getPost().getAuthor().getEmail(),
                                like.getPost().getPayload(),
                                like.getPost().getDate().toString(),
                                like.getPost().getImagePath(),
                                like.getPost().getVideoPath(),
                                like.getPost().getSoundPath()
                        )
                );
            }
            /* Account's comments */
            List<CommentDetails> comments = new ArrayList<>();
            for (Comment comment : this.commentService.findCommentsByUserId(tempAccount.getId())) {
                comments.add(
                        new CommentDetails(
                                comment.getPayload(),
                                comment.getDate(),
                                comment.getPost().getAuthor().getFirstName(),
                                comment.getPost().getAuthor().getLastName(),
                                comment.getPost().getAuthor().getEmail(),
                                comment.getPost().getPayload(),
                                comment.getPost().getDate().toString(),
                                comment.getPost().getImagePath(),
                                comment.getPost().getVideoPath(),
                                comment.getPost().getSoundPath()

                        )
                );
            }

            details.add(
                    new FullAccountDetails(
                            tempAccount.getFirstName(),
                            tempAccount.getLastName(),
                            tempAccount.getEmail(),
                            tempAccount.getPhone(),
                            tempAccount.getTag().getTag(),
                            tempAccount.getImageUrl(),
                            tempAccount.getDateCreated().toString(),
                            network,
                            education,
                            tempAccount.getBio() != null ? tempAccount.getBio().getDescription() : "No bio available",
                            posts,
                            likes,
                            comments
                    )
            );
        }
        return details;
    }


    public Account updateAccount(AccountUpdateRequest accountUpdateRequest) {

        Optional<Account> accPresent = this.accountRepository.findAccountById(accountUpdateRequest.getId());
        if (!accPresent.isPresent()) {
            throw new UserNotFoundException("User with id " + accountUpdateRequest.getId() + " does not exist!");
        }

        Account acc = accPresent.get();
        if (!accountUpdateRequest.getFirstName().equals(""))
            acc.setFirstName(accountUpdateRequest.getFirstName());

        if (!accountUpdateRequest.getLastName().equals(""))
            acc.setLastName(accountUpdateRequest.getLastName());

//        if (!accountUpdateRequest.getEmail().equals("")) {
//            acc.setEmail(accountUpdateRequest.getEmail());
//        }

//        if (account.getPassword() != null && !account.getPassword().equals(""))
//            acc.setPassword(account.getPassword());

        if (!accountUpdateRequest.getPhone().equals(""))
            acc.setPhone(accountUpdateRequest.getPhone());

//        acc.setTag(accountUpdateRequest.getTag());

        if (accountUpdateRequest.getImageUrl() != null){
            acc.setImageUrl(accountUpdateRequest.getImageUrl());
        }

        if (accountUpdateRequest.getBio() != null){
            if(acc.getBio() == null){
                this.addBio(acc, new Bio(accountUpdateRequest.getBio().getDescription()));
            }else{
                acc.setBio(new Bio(acc.getBio().getId(), accountUpdateRequest.getBio().getDescription()));
            }
        }

        return this.accountRepository.save(acc);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteAccountById(id);
    }

    public Account findAccountById(Long id) {
        return accountRepository.findAccountById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found!"));
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found!"));
    }

    public List<Account> findAccountsBySimilarName(String keyword) {
        List<Account> accounts = accountRepository.findAccountBySimilarName(keyword).get();
        /* Remove admin. We don't want him in network entities. */
        for (Account account : accounts) {
            if (account.getRole() == AccountRole.ADMIN) {
                accounts.remove(account);
                break;
            }
        }
        return accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found!"));
    }

    public boolean passwordConfirmation(String password) {
        Account account = this.findAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        return this.bCryptPasswordEncoder.matches(password, account.getPassword());
    }

    public boolean updatePassword(String newPassword) {
        Account account = this.findAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        this.accountRepository.save(account);
        return true;
    }

    public Bio addBio(Account account, Bio bio) {
        account.setBio(this.bioService.addBio(bio));
        this.accountRepository.save(account);
        return account.getBio();
    }

    public Account addEducation(Account account, Education education) {
        account.getEducation().add(this.educationService.addEducation(education));
        return this.accountRepository.save(account);
    }

    public Education updateEducation(Account account, Education education) {
        Optional<Education> previousEducation = this.educationService.findEducationById(education.getId());

        if (!previousEducation.isPresent()) {
            throw new UserNotFoundException("Could find education with id " + education.getId() + " while trying to update it!");
        }

        Education prevEducation = previousEducation.get();

        if (!education.getSchool().equals(""))
            prevEducation.setSchool(education.getSchool());

        if (!education.getDegree().equals(""))
            prevEducation.setDegree(education.getDegree());

        if (!education.getField().equals(""))
            prevEducation.setField(education.getField());

        if (!education.getStartDate().equals(""))
            prevEducation.setStartDate(education.getStartDate());

        prevEducation.setEndDate(education.getEndDate());

        if (education.getGrade() != 0)
            prevEducation.setGrade(education.getGrade());

        if (!education.getDescription().equals(""))
            prevEducation.setDescription(education.getDescription());

        Education newEducation = this.educationService.updateEducation(prevEducation);
        this.accountRepository.save(account);
        return newEducation;
    }

    public void deleteBio(Long uid) {
        Account account = this.accountRepository.findAccountById(uid).orElseThrow(
                () -> new UserNotFoundException("User with id " + uid.toString() + " does not exist!")
        );
        account.setBio(null);
        this.accountRepository.save(account);
    }

    public void connect(String senderEmail, String receiverEmail) {
        Account sender = findAccountByEmail(senderEmail);
        Account receiver = findAccountByEmail(receiverEmail);
        sender.getNetwork().add(receiver);
        receiver.getNetwork().add(sender);
        this.accountRepository.save(sender);
        this.accountRepository.save(receiver);
    }

    public void removeConnection(String user1Email, String user2Email) {
        Account usr1 = findAccountByEmail(user1Email);
        Account usr2 = findAccountByEmail(user2Email);
        usr1.getNetwork().remove(usr2);
        usr2.getNetwork().remove(usr1);
        this.accountRepository.save(usr1);
        this.accountRepository.save(usr2);
    }

    public Account aboutUpdateAccount(AccountUpdateRequest account) {

        Optional<Account> accPresent = this.accountRepository.findAccountByEmail(account.getEmail());
        if (!accPresent.isPresent()) {
            throw new UserNotFoundException("User with email " + account.getEmail() + " does not exist!");
        }

        Account acc = accPresent.get();

        if (!account.getFirstName().equals(""))
            acc.setFirstName(account.getFirstName());
        if (!account.getLastName().equals(""))
            acc.setLastName(account.getLastName());
        if (!account.getPhone().equals(""))
            acc.setPhone(account.getPhone());
        return this.accountRepository.save(acc);
    }

    public void deleteEducation(Account user, Long id) {
        Education edu = this.educationService.findEducationById(id).orElseThrow(
                () -> new IllegalStateException("Education with id " + id.toString() + " does not exist!")
        );
        user.getEducation().remove(edu);
        this.educationService.deleteEducation(id);
    }
}
