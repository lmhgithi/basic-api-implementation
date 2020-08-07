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

    @PostMapping("/rs/event")
    public ResponseEntity createRsEvent(@RequestBody @Valid RsEvent rsEvent, BindingResult result) throws InvalidParamException {
        if (result.hasErrors()) {
            throw new InvalidParamException("invalid param");
        }
        RsEntity rsEntity = RsEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsRepository.save(rsEntity);
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(rsRepository.findAll().size()));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEntity>> getRsListBetween(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) throws InvalidParamException {
        if (start != null && end != null) {
            if (start < 0 || end > rsRepository.findAll().size()) {
                throw new InvalidParamException("invalid request param");
            }
            return ResponseEntity.ok(rsRepository.findAll().subList(start - 1, end));
        }
        return ResponseEntity.ok(rsRepository.findAll());
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEntity> getRsListBetween(@PathVariable Integer index) throws InvalidParamException {
        if (index < 1) {
            throw new InvalidParamException("invalid index");
        }
        Optional<RsEntity> modifyRsEvent = rsRepository.findById(index);
        if (modifyRsEvent.isPresent()) {
            return ResponseEntity.ok(modifyRsEvent.get());
        } else {
            throw new InvalidParamException("id not exist");
        }
    }

    @PatchMapping("/rs/{index}")
    public ResponseEntity modifyRsEventByIndex(@PathVariable Integer index, @RequestBody RsEvent rsEvent) throws InvalidParamException {
        if (index < 1) {
            throw new InvalidParamException("invalid index");
        }
        Optional<RsEntity> rsEntity = rsRepository.findById(index);

        if (!rsEntity.isPresent()) {
            throw new InvalidParamException("id not exist");
        } else if (!rsEntity.get().getUserId().equals(rsEvent.getUserId())) {
            throw new InvalidParamException("userId is not correct");
        } else {
            if (rsEvent.getEventName() != null) {
                rsEntity.get().setEventName(rsEvent.getEventName());
            }
            if (rsEvent.getKeyword() != null) {
                rsEntity.get().setKeyword(rsEvent.getKeyword());
            }
            rsEntity.get().setUserId(rsEvent.getUserId());
            rsRepository.save(rsEntity.get());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(index));
        return new ResponseEntity(headers, HttpStatus.OK);
    }



    @DeleteMapping("/rs/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsRepository.deleteById(index);
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(index));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
