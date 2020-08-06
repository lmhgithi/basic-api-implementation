package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;


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
    public ResponseEntity voteWithUserIdAndRsId(@PathVariable int rsEventId, @RequestBody Vote vote) throws InvalidParamException {

        if(!userRepository.findById(vote.getUserId()).isPresent() || !rsRepository.findById(rsEventId).isPresent()){
            throw new InvalidParamException(".voteWithUserIdAndRsId not present error");
        }
        if(userRepository.findById(vote.getUserId()).get().getVote() >= vote.getVoteNum() ){
            VoteEntity voteEntity = VoteEntity.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .voteNum(vote.getVoteNum())
                    .rs(rsRepository.findById(rsEventId).get())
                    .user(userRepository.findById(vote.getUserId()).get())
                    .build();

            RsEntity rsEntity = rsRepository.findById(rsEventId).get();
            rsEntity.setVoteNum(vote.getVoteNum() + rsEntity.getVoteNum());
            rsRepository.save(rsEntity);
            voteRepository.save(voteEntity);
        }else{
            throw new InvalidParamException("votes is not enough");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(rsEventId));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("rs/vote")
    public ResponseEntity<List<VoteEntity>> getVoteBetweenTime(@RequestParam Timestamp start, @RequestParam Timestamp end){
        List<VoteEntity> votes = voteRepository.findByTimestampBetween(start, end);

        return ResponseEntity.ok(votes);
    }

}
