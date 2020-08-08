package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> register(@RequestBody @Valid User user, BindingResult result) throws InvalidParamException {
        if (result.hasErrors()) {
            throw new InvalidParamException("invalid user");
        }
        return userService.register(user);
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/user/{index}")
    public ResponseEntity deleteUser(@PathVariable Integer index) throws InvalidParamException {
        return userService.deleteUser(index);
    }
}
