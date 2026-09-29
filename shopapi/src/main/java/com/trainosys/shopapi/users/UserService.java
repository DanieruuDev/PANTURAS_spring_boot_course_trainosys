package com.trainosys.shopapi.users;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {
    private final List<User> userList = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public UserService() {
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
                .orElse(null);
    }

    public String getUserEmail(String email) {
        return userList.stream()
                .map(User::getEmail)
                .filter(uEmail -> uEmail.equalsIgnoreCase(email))
                .findFirst()
                .orElse("Email not found");
    }

    public void createUser(User user) {
        user.setId(idGenerator.getAndIncrement());
        userList.add(user);
    }

    public void updateUser(int id, UserUpdateDTO userDetails) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
        }
    }

    public void deleteUser(int id) {
        userList.removeIf(u -> u.getId() == id);
    }
}
