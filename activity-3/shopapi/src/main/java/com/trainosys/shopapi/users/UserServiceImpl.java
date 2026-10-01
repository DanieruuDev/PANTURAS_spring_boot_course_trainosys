package com.trainosys.shopapi.users;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findByUserId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    }

    public String getUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return "User containing email: "+ email + "\n" + user;
    }

    public String createUser(User user) {
        if (user == null || user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: User username cannot be empty");
        }
        user.setUserId(null);
        userRepository.save(user);
        return "Successfully created user: " + user.getUserName();
    }

    public User updateUser(long id, UserUpdateDTO userDetails) {
        if (userDetails == null || userDetails.getName() == null || userDetails.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Updated username cannot be empty");
        }
        User existingUser = userRepository.findByUserId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        existingUser.setUserName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        return existingUser;
    }

    public String deleteUser(long id) {
        boolean removed = userRepository.deleteByUserId(id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
        return "User with ID " + id + " has been successfully deleted.";
    }
}
