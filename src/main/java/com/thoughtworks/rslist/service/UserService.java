package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> register(User user) throws InvalidParamException {
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

    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

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
