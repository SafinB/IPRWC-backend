package com.example.IPRWCBackendHer.services;

import com.example.IPRWCBackendHer.DAO.UserDao;
import com.example.IPRWCBackendHer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ArrayList<User> getAllUsers(){
        return this.userDao.getAllUsers();
    }

    public void saveUserToDatabase(User user) {
        this.userDao.saveToDatabase(user);
    }

}
