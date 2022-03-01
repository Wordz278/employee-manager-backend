package com.kwambi.employeesystem.service;

import java.util.List;

import com.kwambi.employeesystem.domain.User;
import com.kwambi.employeesystem.exception.domain.EmailExistException;
import com.kwambi.employeesystem.exception.domain.UserNotFoundException;
import com.kwambi.employeesystem.exception.domain.UsernameExistException;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
