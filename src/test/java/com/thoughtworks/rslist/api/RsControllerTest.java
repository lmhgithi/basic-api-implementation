package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void initUser() {
        UserController.users.add(new User("Lily1", "male", 18, "a@b.com", "12345678901"));
        UserController.users.add(new User("Lily2", "female", 20, "b@b.com", "12345678902"));
        UserController.users.add(new User("Lily3", "male", 21, "c@b.com", "12345678903"));
    }

    @BeforeEach
    public void init() {
        RsController.rsList = new LinkedList<RsEvent>() {{
            add(new RsEvent("第一条事件", "无", UserController.users.get(0)));
            add(new RsEvent("第二条事件", "无", UserController.users.get(1)));
            add(new RsEvent("第三条事件", "无", UserController.users.get(2)));
        }};
    }

    @Test
    void shouldGetRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
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
    void shouldAddOneRsEvent() throws Exception {
//        User user = new User("Lily4", "male", 22, "d@b.com", "12345678904");
//        RsEvent rsEvent = new RsEvent("第四条事件", "无", user);
//        ObjectMapper objMapper = new ObjectMapper();
//        String requestJson = objMapper.writeValueAsString(rsEvent);
        String requestJson = "{\"eventName\":\"第四条事件\"," +
                " \"keyword\":\"无\"," +
                "\"user\" :{\"user_name\":\"Lily4\", \"user_gender\":\"male\", \"user_age\":22, \"user_email\":\"d@b.com\", \"user_phone\":\"12345678904\"}}";
        assertEquals(3, UserController.users.size());
        mockMvc.perform(post("/rs/event")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(RsController.rsList.size() - 1)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyword", is("无")))
                .andExpect(status().isOk());
        assertEquals(4, UserController.users.size());
        assertEquals("Lily3", UserController.users.get(2).getName());
        assertEquals("Lily4", UserController.users.get(3).getName());

        String requestJson2 = "{\"eventName\":\"第五条事件\"," +
                " \"keyword\":\"无\"," +
                "\"user\" :{\"user_name\":\"Lily4\", \"user_gender\":\"male\", \"user_age\":22, \"user_email\":\"d@b.com\", \"user_phone\":\"12345678904\"}}";

        mockMvc.perform(post("/rs/event")
                .content(requestJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(RsController.rsList.size() - 1)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[4].eventName", is("第五条事件")))
                .andExpect(jsonPath("$[4].keyword", is("无")))
                .andExpect(status().isOk());
        assertEquals(4, UserController.users.size());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/-1"))
                .andExpect(jsonPath("$.error", is("invalid index")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldModifyRsEvent() throws Exception {
//        String requestJson = "{\"eventName\":\"已修改事件\",\"keyword\":\"已修改分类\"}";
        RsEvent rsEvent = new RsEvent("已修改事件", "已修改分类", null);
        ObjectMapper objMapper = new ObjectMapper();
        String requestJson = objMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("已修改事件")))
                .andExpect(jsonPath("$.keyword", is("已修改分类")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRsEvent() throws Exception {
        mockMvc.perform(post("/rs/delete/1"))
                .andExpect(header().string("index", String.valueOf(1)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(status().isOk());
    }

}