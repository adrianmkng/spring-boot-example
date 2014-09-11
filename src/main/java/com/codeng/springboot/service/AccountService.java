package com.codeng.springboot.service;

import com.codeng.springboot.datastore.AccountRepository;
import com.codeng.springboot.domain.Account;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AccountService {

    private AccountRepository accountRepo;

    @Inject
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public void createAccount(Account account) {
        accountRepo.save(account);
    }
}
