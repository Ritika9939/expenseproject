package com.expense.service;

import com.expense.entity.User;
import com.expense.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User createUser(User newUser) {
        return userRepo.save(newUser);
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepo.findById(id);

        if (existingUser.isPresent()) {
            updatedUser.setId(id);
            return Optional.of(userRepo.save(updatedUser));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        } else {
            return false;
        }
    }
}

