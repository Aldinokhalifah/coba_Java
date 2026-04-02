package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalStateException("Name is null or blank");
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        userRepository.save(user);
        return user;
    }

    public Optional<User> getUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is null");
        }

        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is null or blank");
        }

        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
