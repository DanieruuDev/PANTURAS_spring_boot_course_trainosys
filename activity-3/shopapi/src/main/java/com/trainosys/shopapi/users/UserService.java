package com.trainosys.shopapi.users;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public interface UserService {

    public List<User> getAllUsers();

    public User getUserById(int id);

    public String getUserEmail(String email);

    public String createUser(User user);

    public User updateUser(int id, UserUpdateDTO userDetails);

    public String deleteUser(int id);
}
