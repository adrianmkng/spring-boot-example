package com.codeng.springboot.web.controller;

import com.codeng.springboot.domain.Account;
import com.codeng.springboot.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private JdbcUserDetailsManager jdbcUserDetailsManagerMock;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void displayFormReturnsRegistrationPageWithForm() {
        // when
        ModelAndView mav = registrationController.displayForm();

        // then
        assertViewName(mav, "register");
        assertModelAttributeAvailable(mav, "registrationForm");
    }

    @Test
    public void submitReturnsHomePage() {
        // given
        RegistrationForm form = new RegistrationForm();

        // when
        ModelAndView mav = registrationController.submit(form);

        // then
        assertViewName(mav, "home");
    }

    @Test
    public void submitSavesAccountDetails() {
        // given
        String email = "email";
        String firstname = "firstname";
        String lastname = "lastname";

        RegistrationForm form = new RegistrationForm();
        form.setEmail(email);
        form.setFirstname(firstname);
        form.setLastname(lastname);

        // when
        registrationController.submit(form);

        // then
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountServiceMock).createAccount(accountCaptor.capture());
        Account account = accountCaptor.getValue();
        assertEquals("Expected email on account not set", email, account.getEmail());
        assertEquals("Expected firstname on account not set", firstname, account.getFirstname());
        assertEquals("Expected lastname on account not set", lastname, account.getLastname());
    }

    @Test
    public void submitSavesUserCredentialsWithEncodedPassword() {
        // given
        String email = "email";
        String password = "password";
        String encodedPassword = "encodedPassword";

        RegistrationForm form = new RegistrationForm();
        form.setEmail(email);
        form.setPassword(password);

        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        // when
        registrationController.submit(form);

        // then
        SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("USER");

        ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);
        verify(jdbcUserDetailsManagerMock).createUser(userDetailsCaptor.capture());
        UserDetails userDetails = userDetailsCaptor.getValue();
        assertEquals("Expected username on user not set", email, userDetails.getUsername());
        assertEquals("Expected encoded password on user not set", encodedPassword, userDetails.getPassword());
        assertEquals("Unexpected number of granted authorities for user", 1, userDetails.getAuthorities().size());
        assertTrue("Expected granted authorities on user not set", userDetails.getAuthorities().contains(expectedAuthority));
    }
}