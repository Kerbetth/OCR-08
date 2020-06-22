package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.userservice.UserPreferences;
import tourGuide.clients.dto.userservice.User;

import java.net.http.HttpResponse;

@RestController
public class UserController {

    @Autowired
    UserClient userClient;

    @PostMapping("/setUserPreferences")
    public HttpResponse<String> setUserPreferences(
            @RequestBody UserPreferences userPreferences) {
        return userClient.setUserPreferences(userPreferences);
    }

    @GetMapping("/getUser")
    public User getUser(String userName) {
        return userClient.getUser(userName);
    }

}