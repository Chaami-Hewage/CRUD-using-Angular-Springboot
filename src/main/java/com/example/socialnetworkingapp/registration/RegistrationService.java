package com.example.socialnetworkingapp.registration;

import com.example.socialnetworkingapp.model.account.Account;
import com.example.socialnetworkingapp.model.account.AccountService;
import com.example.socialnetworkingapp.exception.NotValidEmailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidatorService emailValidatorService;
    private final AccountService accountService;

    public Account register(RegistrationRequest request) throws InterruptedException {
        boolean isValidEmail = emailValidatorService.test(request.getEmail());

        if(!isValidEmail){
            throw new NotValidEmailException("Email not valid!");
        }

        return accountService.accountSignUp(request);
    }
}
