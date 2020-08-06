package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@RestController
public class RsController {
    private final UserRepository userRepository;
    private final RsRepository rsRepository;

    public RsController(UserRepository userRepository, RsRepository rsRepository) {
        this.userRepository = userRepository;
        this.rsRepository = rsRepository;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEntity>> getRsListBetween(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) throws InvalidParamException {
        if (start != null && end != null) {
            if (start < 0 || end > rsRepository.findAll().size()) {
                throw new InvalidParamException("invalid request param");
            }
            return ResponseEntity.ok(rsRepository.findAll().subList(start-1, end));
        }


        return ResponseEntity.ok(rsRepository.findAll());
    }

//    @PostMapping("/rs/event")
//    public ResponseEntity createRsEvent(@RequestBody @Valid RsEvent rsEvent, BindingResult result) throws InvalidParamException {
//        if (result.hasErrors()) {
//            throw new InvalidParamException("invalid param");
//        }
//        rsRepository.save(new RsEntity(rsEvent));
//        if (!userRepository.findAll().contains(userId)) {
//            UserEntity userEntity = UserEntity.builder()
//                    .name();
//            userRepository.save(userEntity);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("index", String.valueOf(rsRepository.findAll().size()));
//        return new ResponseEntity(headers, HttpStatus.CREATED);
//    }

//    @GetMapping("/rs/{index}")
//    public ResponseEntity<Optional<RsEntity>> getRsListByIndex(@PathVariable Integer index) throws InvalidParamException {
//        if (index < 0 || index > rsRepository.findAll().size()) {
//            throw new InvalidParamException("invalid index");
//        }
//        return ResponseEntity.ok(rsRepository.findById(index));
//    }

//    @PostMapping("/rs/modify/{index}")
//    public ResponseEntity modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
//        rsList.get(index).setEventName(rsEvent.getEventName());
//        rsList.get(index).setKeyword(rsEvent.getKeyword());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("index", String.valueOf(index));
//        return new ResponseEntity(headers, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/rs/delete/{index}")
//    public ResponseEntity deleteRsEvent(@PathVariable int index) {
//        rsList.remove(index);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("index", String.valueOf(index));
//        return new ResponseEntity(headers, HttpStatus.CREATED);
//    }
}
