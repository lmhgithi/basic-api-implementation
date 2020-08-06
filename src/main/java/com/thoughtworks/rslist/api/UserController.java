package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RsRepository rsRepository;

    public UserController(UserRepository userRepository, RsRepository rsRepository) {
        this.userRepository = userRepository;
        this.rsRepository = rsRepository;
    }

    @PostMapping("/user")
    public ResponseEntity<String> register(@RequestBody @Valid User user, BindingResult result) throws InvalidParamException {
        if (result.hasErrors()) {
            throw new InvalidParamException("invalid user");
        }
        if (userRepository.findAllByName(user.getName()).size() > 0) {
            throw new InvalidParamException("user name already exist");
        }
        UserEntity userEntity = UserEntity.builder()
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .vote(10)
                .build();

        userRepository.save(userEntity);
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(userRepository.findAll().size() - 1));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("/user/getAll")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/user/{index}")
    public ResponseEntity deleteUser(@PathVariable Integer index) throws InvalidParamException {
        if (index <= 0)
            throw new InvalidParamException("Invalid delete index");
        try {
            userRepository.deleteById(index);
        }catch (Exception e){
            throw new InvalidParamException("Invalid delete index(had tried to delete)");
        }
        return ResponseEntity.ok().build();
    }
}
