package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsListBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().string("[第一条事件, 第二条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRsEvent() throws Exception {
        mockMvc.perform(post("/rs/event").content("第四条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件, 第四条事件]"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(content().string("第一条事件"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(content().string("第三条事件"))
                .andExpect(status().isOk());
    }
}
