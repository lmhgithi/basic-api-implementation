package com.thoughtworks.rslist;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Order(1)
    @Test
    void shouldGetRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void shouldGetRsListBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().string("[第一条事件, 第二条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void shouldAddOneRsEvent() throws Exception {
        mockMvc.perform(post("/rs/event").content("第四条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件, 第四条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(content().string("第三条事件"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void shouldModifyRsEvent() throws Exception {
        mockMvc.perform(post("/rs/modify/1").content("已修改事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().string("已修改事件"))
                .andExpect(status().isOk());
    }
}
