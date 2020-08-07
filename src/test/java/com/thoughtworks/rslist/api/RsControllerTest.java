package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
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
        UserEntity userEntity2 = UserEntity.builder()
                .name("Lily2")
                .gender("female")
                .age(20)
                .email("a@b.com")
                .phone("12345678901")
                .vote(10)
                .build();
        userRepository.save(userEntity2);
        int idOfUser1 = userRepository.findAll().get(0).getUserId();
        int idOfUser2 = userRepository.findAll().get(1).getUserId();
        RsEntity rsEntity = RsEntity.builder()
                .eventName("第一条事件")
                .keyword("无")
                .userId(idOfUser1)
                .build();
        rsRepository.save(rsEntity);
        RsEntity rsEntity2 = RsEntity.builder()
                .eventName("第二条事件")
                .keyword("无")
                .userId(idOfUser2)
                .build();
        rsRepository.save(rsEntity2);
    }

    @Test
    void shouldAddOneRsEvent() throws Exception {
        int idOfUser = userRepository.findAll().get(0).getUserId();
        RsEvent rsEvent = RsEvent.builder()
                .eventName("第三条事件")
                .keyword("无")
                .userId(idOfUser)
                .voteNum(0)
                .build();
        String requestJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(rsRepository.findAll().size())))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldModifyRsEvent() throws Exception {
        int idOfUser = userRepository.findAll().get(1).getUserId();
        int idToModify = rsRepository.findAll().get(1).getRsId();
        RsEvent rsEvent = new RsEvent("已修改事件", "已修改分类", idOfUser, 0);
        ObjectMapper objMapper = new ObjectMapper();
        String requestJson = objMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(idToModify)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/" + idToModify))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is("已修改分类")))
                .andExpect(jsonPath("$.userId", is(idOfUser)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotModifyRsEventWhenUserIdNotEqual() throws Exception {
        int idToModify = rsRepository.findAll().get(1).getRsId();
        RsEvent rsEvent = new RsEvent("已修改事件", "已修改分类", 0, 0);
        ObjectMapper objMapper = new ObjectMapper();
        String requestJson = objMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("userId is not correct")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldModifyRsEventOptional() throws Exception {
        int idToModify = rsRepository.findAll().get(1).getRsId();
        int idOfUser = userRepository.findAll().get(1).getUserId();
        String originKeyword = rsRepository.findById(idToModify).get().getKeyword();

        RsEvent rsEvent = new RsEvent("已修改事件", null, idOfUser, 0);
        String requestJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(idToModify)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/" + idToModify))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is(originKeyword)))
                .andExpect(status().isOk());

        RsEvent rsEvent2 = new RsEvent(null, "已修改分类", idOfUser, 0);
        String requestJson2 = objectMapper.writeValueAsString(rsEvent2);
        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(idToModify)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/" + idToModify))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is("已修改分类")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsListBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list?start=1&end=100"))
                .andExpect(jsonPath("$.error", is("invalid request param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetVote() throws Exception {
        int userId = userRepository.findAll().get(0).getUserId();
        int rsId = rsRepository.findAll().get(0).getRsId();
        Vote vote = Vote.builder()
                .voteNum(5)
                .userId(userId)
                .build();
        String requestJson = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsId)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].voteNum", is(5)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        int idToGet = rsRepository.findAll().get(0).getRsId();
        mockMvc.perform(get("/rs/" + idToGet))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/-1"))
                .andExpect(jsonPath("$.error", is("invalid index")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteRsEvent() throws Exception {
        int idToDelete = rsRepository.findAll().get(0).getRsId();
        mockMvc.perform(delete("/rs/" + idToDelete))
                .andExpect(header().string("index", String.valueOf(idToDelete)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(status().isOk());
    }

}
