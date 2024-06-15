package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AccountEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.account.AccountRequest;
import com.SWD.Order_Dish.model.account.AccountResponse;
import com.SWD.Order_Dish.repository.AccountRepository;
import com.SWD.Order_Dish.util.DateProcess;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AccountResponse> findAll() {
        LOGGER.info("Find all accounts");
        List<AccountEntity> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            LOGGER.warn("No accounts were found!");
        }
        return accounts.stream()
                .map(this::accountResponseGenerator)
                .collect(Collectors.toList());
    }

    public AccountResponse findById(String id) {
        LOGGER.info("Find account with id " + id);
        Optional<AccountEntity> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            LOGGER.warn("No account was found!");
            return null;
        }
        return account.map(this::accountResponseGenerator).get();
    }

    public AccountResponse save(AccountRequest accountRequest) throws ParseException {
        AccountEntity account;
        AccountEntity acc = accountRepository.findAccountEntityByPhoneNumber(accountRequest.getPhoneNumber());
        if (accountRequest.getUserId() != null) {
            LOGGER.info("Update account with id " + accountRequest.getUserId());
            checkExist(accountRequest.getUserId());
            if (Objects.equals(acc.getUserId(), accountRequest.getUserId())) {
                accountRequest.setPhoneNumber(null);
            }
            account = accountRepository.findById(accountRequest.getUserId()).get();
            updateAccount(account, accountRequest);
            accountRepository.save(account);
        } else {
            LOGGER.info("Create new account");
            if (acc != null) {
                throw new CustomValidationException(List.of("Phone has been used"));
            }
            account = createAccount(accountRequest);
            accountRepository.save(account);
        }
        return accountResponseGenerator(account);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete account with id " + id);
            checkExist(id);
            AccountEntity account = accountRepository.findById(id).get();
            accountRepository.delete(account);
        }
    }

    private AccountEntity createAccount(AccountRequest request) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountEntity account = AccountEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdDate(new Date())
                .createdBy(authentication.getName())
                .build();
        updateAccount(account, request);
        return account;
    }

    private void updateAccount(AccountEntity account, AccountRequest request) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        account.setImageURL(request.getImageURL());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setFullName(request.getFullName());
        account.setBirthday(DateProcess.convertToSimpleDate(request.getBirthday()));
        if (request.getPhoneNumber() != null) {
            account.setPhoneNumber(request.getPhoneNumber());
        }
        account.setAddress(request.getAddress());
        account.setRole(request.getRole());
        account.setIsAvailable(request.getIsAvailable());
        account.setIsEnable(request.getIsEnable());
        account.setIsUnlocked(request.getIsUnlocked());
        account.setUpdatedDate(new Date());
        account.setModifiedBy(authentication.getName());
    }

    private AccountResponse accountResponseGenerator(AccountEntity account) {
        return new AccountResponse(
                account.getUserId(),
                account.getEmail(),
                account.getImageURL(),
                account.getFullName(),
                account.getBirthday(),
                account.getPhoneNumber(),
                account.getAddress(),
                account.getRole(),
                account.getIsAvailable(),
                account.getIsEnable(),
                account.getIsUnlocked(),
                account.getCreatedDate(),
                account.getUpdatedDate(),
                account.getModifiedBy(),
                account.getCreatedBy()
        );
    }

    private void checkExist(String id) {
        Optional<AccountEntity> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new CustomValidationException(List.of("No account was found!"));
        }
    }
}
