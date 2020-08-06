package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsRepository rsRepository;
    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    public void init() {
        voteRepository.deleteAll();
        rsRepository.deleteAll();
        userRepository.deleteAll();
        UserEntity userEntity = UserEntity.builder()
                .name("Lily")
                .gender("female")
                .age(18)
                .email("a@b.com")
                .phone("12345678901")
                .vote(10)
                .build();
        userRepository.save(userEntity);
        int idOfUser = userRepository.findAll().get(0).getUserId();
        RsEntity rsEntity = RsEntity.builder()
                .eventName("第一条事件")
                .keyword("无")
                .userId(idOfUser)
                .build();
        rsRepository.save(rsEntity);
    }

    @Test
    void shouldVoteWithUserIdAndRsId() throws Exception {
        int userId = userRepository.findAll().get(0).getUserId();
        int rsId = rsRepository.findAll().get(0).getRsId();
        Vote vote = Vote.builder()
                .voteNum(3)
                .userId(userId)
                .build();
        String requestJson = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsId)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotVoteIfVotesNotEnough() throws Exception {
        int userId = userRepository.findAll().get(0).getUserId();
        int rsId = rsRepository.findAll().get(0).getRsId();
        Vote vote = Vote.builder()
                .voteNum(12)
                .userId(userId)
                .build();
        String requestJson = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsId)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("votes is not enough")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetVoteBetweenTime() throws Exception {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        VoteEntity vote = VoteEntity.builder()
                .voteNum(2)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .rs(rsRepository.findAll().get(0))
                .user(userRepository.findAll().get(0)).build();
        voteRepository.save(vote);
        VoteEntity vote2 = VoteEntity.builder()
                .voteNum(3)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .rs(rsRepository.findAll().get(0))
                .user(userRepository.findAll().get(0)).build();
        voteRepository.save(vote2);

        Timestamp end = new Timestamp(System.currentTimeMillis());
        mockMvc.perform(get("/rs/vote?start=" + start + "&end=" + end))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }
}
