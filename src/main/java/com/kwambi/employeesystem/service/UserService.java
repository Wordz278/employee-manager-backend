package com.kwambi.employeesystem.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import com.kwambi.employeesystem.domain.User;
import com.kwambi.employeesystem.exception.domain.EmailExistException;
import com.kwambi.employeesystem.exception.domain.EmailNotFoundException;
import com.kwambi.employeesystem.exception.domain.UserNotFoundException;
import com.kwambi.employeesystem.exception.domain.UsernameExistException;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;
    
    List<User> getUsers();
    
    User findUserByUsername(String username);
    
    User findUserByEmail(String email);
    
    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
    
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
    
    void deleteUser(long id);
    
    void resetPassword(String email) throws EmailNotFoundException, MessagingException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException;
}
