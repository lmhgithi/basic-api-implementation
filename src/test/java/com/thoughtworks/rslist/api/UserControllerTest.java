package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        UserEntity userEntity = UserEntity.builder()
                .name("Lily1")
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
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Lily3", "male", 18, "a@b.com", "12345678901");
        String userJson = objectMapper.writeValueAsString(user);
        List<UserEntity> users = userRepository.findAll();
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index", String.valueOf(users.size())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/user/getAll"))
                .andExpect(jsonPath("$[0].name", is("Lily1")))
                .andExpect(jsonPath("$[0].gender", is("female")))
                .andExpect(jsonPath("$[0].age", is(18)))
                .andExpect(jsonPath("$[0].email", is("a@b.com")))
                .andExpect(jsonPath("$[0].phone", is("12345678901")))
                .andExpect(jsonPath("$[0].vote", is(10)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        int before = userRepository.findAll().size();
        int idToDelete = userRepository.findAll().get(1).getId();
        mockMvc.perform(delete("/user/" + idToDelete))
                .andExpect(status().isOk());
        int after = userRepository.findAll().size();
        assertEquals(before, after + 1);
    }

    @Test
    void shouldNotDeleteUserWhenIdNotValid() throws Exception {
        mockMvc.perform(delete("/user/0"))
                .andExpect(jsonPath("$.error", is("Invalid delete index")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddUserWhenNameExist() throws Exception {
        User user = new User("Lily2", "male", 19, "a@b.com", "12345678901");
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
                .content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("user name already exist")))
                .andExpect(status().isBadRequest());
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
                .andExpect(status().isCreated());
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
                .andExpect(status().isCreated());
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
                .andExpect(status().isCreated());
    }
}