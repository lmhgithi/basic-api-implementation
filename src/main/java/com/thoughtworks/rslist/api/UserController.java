package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.InvalidParamException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> users = new ArrayList<>();

    @PostMapping("/user")
    public static ResponseEntity<String> register(@RequestBody @Valid User user, BindingResult result) throws InvalidParamException {
        if(result.hasErrors()){
            throw new InvalidParamException("invalid user");
        }
        users.add(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(users.size() - 1));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("/user/getAll")
    public static ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }
}
