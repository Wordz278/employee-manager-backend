package com.kwambi.employeesystem.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.kwambi.employeesystem.constant.UserImplConstant;
import com.kwambi.employeesystem.domain.User;
import com.kwambi.employeesystem.domain.UserPrincipal;
import com.kwambi.employeesystem.enumeration.Role;
import com.kwambi.employeesystem.exception.domain.EmailExistException;
import com.kwambi.employeesystem.exception.domain.UserNotFoundException;
import com.kwambi.employeesystem.exception.domain.UsernameExistException;
import com.kwambi.employeesystem.repository.UserRepository;
import com.kwambi.employeesystem.service.LoginAttemptService;
import com.kwambi.employeesystem.service.UserService;



import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional //Manage propagation whenever you are dealing with one service
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            LOGGER.error(UserImplConstant.NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(UserImplConstant.NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate()); //Last time they logged in
            user.setLastLoginDate(new Date()); //new Last time they logged in
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(UserImplConstant.RETURNNIG_FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }



    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()){
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setNotLocked(false);
            }else{
                user.setNotLocked(true);
            }
        }else{
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }



    @Override
    public User register(String firstName, String lastName, String username, String email) 
        throws UserNotFoundException, UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());
        userRepository.save(user);
        LOGGER.info("New user password: "+ password);
        return user;
    }



    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(UserImplConstant.DEFAULT_USER_IMAGE_PATH).toUriString();
    }



    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }



    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }



    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);

        if(StringUtils.isNotBlank(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null){
                throw new UserNotFoundException(UserImplConstant.NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())){
                throw new UsernameExistException(UserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXITS);
            }
            return currentUser;
        }else {
            if(userByNewUsername != null){
                throw new UsernameExistException(UserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null){
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXITS);
            }
            return null;
        }
    }



    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }



    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }



    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    
}
