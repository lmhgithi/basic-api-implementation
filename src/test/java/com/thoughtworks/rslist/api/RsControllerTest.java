package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
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
        RsEntity rsEntity = RsEntity.builder()
                .eventName("第一条事件")
                .keyword("无")
                .userId("1")
                .build();
        rsRepository.save(rsEntity);
        RsEntity rsEntity2 = RsEntity.builder()
                .eventName("第二条事件")
                .keyword("无")
                .userId("2")
                .build();
        rsRepository.save(rsEntity2);
    }
    @Test
    void shouldAddOneRsEvent() throws Exception {
        RsEvent rsEntity = RsEvent.builder()
                .eventName("第三条事件")
                .keyword("无")
                .userId("1")
                .build();
        String requestJson = objectMapper.writeValueAsString(rsEntity);
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
        int idToModify = rsRepository.findAll().get(1).getRsId();
        RsEvent rsEvent = new RsEvent("已修改事件", "已修改分类", "2");
        ObjectMapper objMapper = new ObjectMapper();
        String requestJson = objMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(idToModify)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/" + idToModify))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is("已修改分类")))
                .andExpect(jsonPath("$.userId", is("2")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotModifyRsEventWhenUserIdNotEqual() throws Exception {
        int idToModify = rsRepository.findAll().get(1).getRsId();
        RsEvent rsEvent = new RsEvent("已修改事件", "已修改分类", "3");
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
        String originKeyword = rsRepository.findById(idToModify).get().getKeyword();

        RsEvent rsEvent = new RsEvent("已修改事件", null, "2");
        String requestJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/" + idToModify)
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(idToModify)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/" + idToModify))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is(originKeyword)))
                .andExpect(status().isOk());

        RsEvent rsEvent2 = new RsEvent(null, "已修改分类", "2");
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
//    @Test
//    void shouldGetRsList() throws Exception {
//        RsEntity rsEntity = RsEntity.builder()
//                .eventName("1")
//                .keyword("key")
//                .userId("0")
//                .build();
//        rsRepository.save(rsEntity);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyword", is("无")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyword", is("无")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void shouldGetRsListBetween() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=2"))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/list?start=1&end=100"))
//                .andExpect(jsonPath("$.error", is("invalid request param")))
//                .andExpect(status().isBadRequest());
//    }



//    @Test
//    void shouldGetOneRsEvent() throws Exception {
//        mockMvc.perform(get("/rs/0"))
//                .andExpect(jsonPath("$.eventName", is("第一条事件")))
//                .andExpect(jsonPath("$", not(hasKey("user"))))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(jsonPath("$.eventName", is("第三条事件")))
//                .andExpect(jsonPath("$", not(hasKey("user"))))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/-1"))
//                .andExpect(jsonPath("$.error", is("invalid index")))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    @Test
//    void shouldDeleteRsEvent() throws Exception {
//        mockMvc.perform(post("/rs/delete/1"))
//                .andExpect(header().string("index", String.valueOf(1)))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
//                .andExpect(status().isOk());
//    }

}
