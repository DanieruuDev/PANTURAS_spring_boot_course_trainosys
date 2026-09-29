package com.trainosys.shopapi.users;

import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public String getUserEmail(@PathVariable String email){
        String foundEmail = userService.getUserEmail(email);
        return "Email: " + foundEmail;
    }

    @PostMapping
    public String createUser(@RequestBody User user){
        userService.createUser(user);
        return "Successfully created user: " + user.getName() + " with ID: " + user.getId();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id ,@RequestBody UserUpdateDTO user){
        userService.updateUser(id, user);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return "User with ID " + id + " has been successfully deleted.";
    }


}
