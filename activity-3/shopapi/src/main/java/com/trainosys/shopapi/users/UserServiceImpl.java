package com.trainosys.shopapi.users;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> userList = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public UserServiceImpl() {
        User u1 = new User();
        u1.setId(idGenerator.getAndIncrement());
        u1.setName("Alice Smith");
        u1.setEmail("alice@example.com");

        User u2 = new User();
        u2.setId(idGenerator.getAndIncrement());
        u2.setName("Bob Jones");
        u2.setEmail("bob@example.com");

        userList.add(u1);
        userList.add(u2);
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserById(int id) {
        return userList.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));
    }

    public String getUserEmail(String email) {
        return userList.stream()
                .map(User::getEmail)
                .filter(uEmail -> uEmail.equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
    }

    public String createUser(User user) {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: User name cannot be empty");
        }
        user.setId(idGenerator.getAndIncrement());
        userList.add(user);
        return "Successfully created user: " + user.getName() + " with ID: " + user.getId();
    }

    public User updateUser(int id, UserUpdateDTO userDetails) {
        if (userDetails == null || userDetails.getName() == null || userDetails.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Updated name cannot be empty");
        }
        User existingUser = userList.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        return existingUser;
    }

    public String deleteUser(int id) {
        boolean removed = userList.removeIf(u -> u.getId() == id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
        return "User with ID " + id + " has been successfully deleted.";
    }
}
