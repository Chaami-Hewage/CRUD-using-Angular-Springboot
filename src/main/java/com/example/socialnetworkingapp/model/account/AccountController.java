package com.example.socialnetworkingapp.model.account;

import com.example.socialnetworkingapp.model.bio.Bio;
import com.example.socialnetworkingapp.model.connection_request.ConnectionReqService;
import com.example.socialnetworkingapp.model.education.Education;
import com.example.socialnetworkingapp.model.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ConnectionReqService connectionReqService;

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account currUser = accountService.findAccountByEmail(user);
        List<Account> accounts = this.accountService.findAllAccounts();
        accounts.remove(currUser);

        List<AccountResponse> accs = new ArrayList<>();
        for(Account acc: accounts) {
            accs.add(new AccountResponse(acc));
        }
        return new ResponseEntity<>(accs, HttpStatus.OK);
    }

    @GetMapping("/find/id/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") Long id){
        Account account = this.accountService.findAccountById(id);
        return new ResponseEntity<>(new AccountResponse(account), HttpStatus.OK);
    }

    @GetMapping("/find/mail/{email}")
    public ResponseEntity<AccountResponse> getAccountByEmail(@PathVariable("email") String email) {
        Account account = this.accountService.findAccountByEmail(email);
        return new ResponseEntity<>(new AccountResponse(account), HttpStatus.OK);
    }

    @GetMapping("/find/names/{keyword}")
    public ResponseEntity<List<AccountResponse>> getAccountsBySimilarName(@PathVariable("keyword") String keyword) {
        List<Account> accounts = this.accountService.findAccountsBySimilarName(keyword);
        List<AccountResponse> accs = new ArrayList<>();
        for(Account acc: accounts) {
            accs.add(new AccountResponse(acc));
        }
        return new ResponseEntity<>(accs, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody AccountUpdateRequest accountUpdateRequest){
        Account newAccount = this.accountService.updateAccount(accountUpdateRequest);
        return new ResponseEntity<>(new AccountResponse(newAccount), HttpStatus.OK);
    }

//    @PutMapping("/about-update")
//    public ResponseEntity<AccountResponse> aboutUpdateAccount(@RequestBody AccountUpdateRequest account){
//        Account newAccount = this.accountService.aboutUpdateAccount(account);
//        return new ResponseEntity<>(new AccountResponse(newAccount), HttpStatus.OK);
//    }

    @PostMapping("/confirmation")
    public ResponseEntity<Boolean> passwordConfirmation(@RequestBody String password){
        return new ResponseEntity<Boolean>(this.accountService.passwordConfirmation(password), HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity<Boolean> updatePassword(@RequestBody String newPassword){
        return new ResponseEntity<Boolean>(this.accountService.updatePassword(newPassword), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        accountService.findAccountByEmail(user);

        Account currUser = accountService.findAccountById(id);
        List<String> emailsToDelete = new ArrayList<>();
        for(Account usr: currUser.getNetwork()) {
            emailsToDelete.add(usr.getEmail());
        }
        for(String email: emailsToDelete) {
            this.connectionReqService.deleteConnection(currUser.getEmail(), email);
        }

        this.accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/education/delete/{id}")
    public ResponseEntity<?> deleteEducationById(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Account currUser = accountService.findAccountByEmail(user);
        this.accountService.deleteEducation(currUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/network/delete/{email}")
    public ResponseEntity<?> deleteFromNetwork(@PathVariable("email") String otherEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        this.connectionReqService.deleteConnection(currUser.getEmail(), otherEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/education/get")
    public ResponseEntity<List<Education>> getEducation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        return new ResponseEntity<>(currUser.getEducation(), HttpStatus.OK);
    }

    @PostMapping("/bio/add")
    public ResponseEntity<Bio> addBio(@RequestBody String bio){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        return new ResponseEntity<>(this.accountService.addBio(currUser, new Bio(bio)),
                HttpStatus.OK);
    }

    @DeleteMapping("/bio/delete/{uid}")
    public ResponseEntity<HttpStatus> deleteBio(@PathVariable("uid") Long uid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        this.accountService.deleteBio(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* ReportedPost a {email, education} */
    @PostMapping("/education/add")
    public ResponseEntity<AccountResponse> addEducation(@RequestBody Education education) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        return new ResponseEntity<>(new AccountResponse(this.accountService.addEducation(currUser, education)),
                HttpStatus.OK);
    }

    @PostMapping("/education/update")
    public ResponseEntity<Education> editEducation(@RequestBody Education education) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currUser = accountService.findAccountByEmail(authentication.getName());
        return new ResponseEntity<Education>(this.accountService.updateEducation(currUser, education),
                HttpStatus.OK);
    }

}
