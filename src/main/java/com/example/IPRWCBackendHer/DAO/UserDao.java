package com.example.IPRWCBackendHer.DAO;

import com.example.IPRWCBackendHer.models.User;
import com.example.IPRWCBackendHer.repository.UserRepository;
import com.example.IPRWCBackendHer.services.CheckAdminService;
import org.springframework.stereotype.Component;
import java.util.UUID;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserDao {
    private final UserRepository userRepository;
    private final CheckAdminService checkAdminService;

    public UserDao(UserRepository userRepository, CheckAdminService checkAdminService) {
        this.userRepository = userRepository;
        this.checkAdminService = checkAdminService;
    }

    public void saveToDatabase(User user) {
        this.userRepository.save(user);
    }
    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) this.userRepository.findAll();
    }

    public void deleteUserFromDatabase(UUID id) {
        this.userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();

        for (User user : users) {
            if (user.getEmail().contains(email)) {

                return Optional.ofNullable(user);
            }
        }
        return Optional.empty();
    }

    public boolean isUserAdmin(User user){
        return checkAdminService.IsUserAdmin(userRepository.findById(user.getId()));
    }
}
