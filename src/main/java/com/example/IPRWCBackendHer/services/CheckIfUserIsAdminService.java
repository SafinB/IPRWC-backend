package com.example.IPRWCBackendHer.services;

import com.example.IPRWCBackendHer.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckIfUserIsAdminService {
    public boolean IsUserAdmin(Optional<User> user) {
        return user.map(User::isRole).orElse(false);
    }
}
