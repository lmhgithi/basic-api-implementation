package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        UserController.users.clear();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Lily", "male", 18, "a@b.com", "12345678901");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void nameShouldNotLongerThan8() throws Exception {
        User user = new User("Lily56789", "male", 18, "a@b.com", "12345678901");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nameShouldNotNull() throws Exception {
        User user = new User(null, "male", 18, "a@b.com", "12345678901");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genderShouldNotNull() throws Exception {
        User user = new User("Lily", null, 18, "a@b.com", "12345678901");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldBetween18and100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Lily", "male", 11, "a@b.com", "12345678901");
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user2 = new User("Lily", "male", 110, "a@b.com", "12345678901");
        String userJson2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/user")
                .content(userJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user3 = new User("Lily", "male", 50, "a@b.com", "12345678901");
        String userJson3 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(post("/user")
                .content(userJson3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void emailShouldValid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Lily", "male", 18, "abb.com", "12345678901");
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user2 = new User("Lily", "male", 18, "abb@.com", "12345678901");
        String userJson2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/user")
                .content(userJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user3 = new User("Lily", "male", 18, "ab@c.com", "12345678901");
        String userJson3 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(post("/user")
                .content(userJson3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void phoneShouldValid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Lily", "male", 18, "a@b.com", "1234567890123");
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user2 = new User("Lily", "male", 18, "a@b.com", "123456");
        String userJson2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/user")
                .content(userJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        User user3 = new User("Lily", "male", 18, "a@b.com", "12345678901");
        String userJson3 = objectMapper.writeValueAsString(user3);
        mockMvc.perform(post("/user")
                .content(userJson3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}