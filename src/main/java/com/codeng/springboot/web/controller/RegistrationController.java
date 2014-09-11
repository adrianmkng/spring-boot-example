package com.codeng.springboot.web.controller;

import com.codeng.springboot.domain.Account;
import com.codeng.springboot.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {

    Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
    public static final String REGISTRATION_PAGE = "register";
    public static final String HOME_PAGE = "home";

    private JdbcUserDetailsManager userDetailsService;

    private AccountService accountService;

    private PasswordEncoder passwordEncoder;

    @Inject
    public RegistrationController(JdbcUserDetailsManager userDetailsService, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView displayForm() {
        return new ModelAndView(REGISTRATION_PAGE, "registrationForm", new RegistrationForm());
    }

    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("registrationForm") RegistrationForm form) {
        LOGGER.debug("Registering new user");
        Account newAccount = new Account();
        newAccount.setEmail(form.getEmail());
        newAccount.setFirstname(form.getFirstname());
        newAccount.setLastname(form.getLastname());
        accountService.createAccount(newAccount);

        GrantedAuthority userAuthority = new SimpleGrantedAuthority("USER");
        List<GrantedAuthority> authorities = Arrays.asList(userAuthority);
        User userDetails = new User(form.getEmail(), passwordEncoder.encode(form.getPassword()), authorities);
        userDetailsService.createUser(userDetails);

        return new ModelAndView(HOME_PAGE);
    }



}
