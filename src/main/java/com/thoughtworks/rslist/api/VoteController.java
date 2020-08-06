package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class VoteController {
    private final UserRepository userRepository;
    private final RsRepository rsRepository;
    private final VoteRepository voteRepository;

    public VoteController(UserRepository userRepository, RsRepository rsRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsRepository = rsRepository;
        this.voteRepository = voteRepository;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteWithUserIdAndRsId(@RequestBody @Valid int rsEventId) throws InvalidParamException {

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

}
