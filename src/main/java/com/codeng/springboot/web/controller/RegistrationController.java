package com.codeng.springboot.web.controller;

import com.codeng.springboot.domain.Account;
import com.codeng.springboot.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {

    Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
    public static final String REGISTRATION_PAGE = "register";

    private JdbcUserDetailsManager userDetailsService;

    private AccountService accountService;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    @Inject
    public RegistrationController(JdbcUserDetailsManager userDetailsService, AccountService accountService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView displayForm() {
        return new ModelAndView(REGISTRATION_PAGE, "registrationForm", new RegistrationForm());
    }

    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("registrationForm") RegistrationForm form, HttpServletRequest request) {
        LOGGER.debug("Registering new user");
        String email = form.getEmail();
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setFirstname(form.getFirstname());
        newAccount.setLastname(form.getLastname());
        accountService.createAccount(newAccount);

        GrantedAuthority userAuthority = new SimpleGrantedAuthority("USER");
        List<GrantedAuthority> authorities = Arrays.asList(userAuthority);
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User userDetails = new User(email, encodedPassword, authorities);
        userDetailsService.createUser(userDetails);

        autoLogin(request, email, encodedPassword);

        return new ModelAndView("redirect:/");
    }

    private void autoLogin(HttpServletRequest request, String username, String password) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = authenticationManager.authenticate(token);
            LOGGER.debug("Logging in with [{}]", authentication.getPrincipal());
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        } catch (Exception e) {
            securityContext.setAuthentication(null);
            LOGGER.error("Failed to autoLogin", e);
        }
    }
}
