package com.taxiWithBack.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxiWithBack.domain.member.dto.UserDTO;
import com.taxiWithBack.domain.member.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class UserApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void join() throws Exception {
        String eMail="gb4205@naver.com";
        String passWord="thslr1234";
        String nickName="niiick";

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserDTO(eMail,passWord,nickName))))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입 실패 - eMail 중복")
    void join_fail() throws Exception {
        String eMail="gb4205@naver.com";
        String passWord="thslr1234";
        String nickName="niiick";

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserDTO(eMail,passWord,nickName))))
                .andDo(print())
                .andExpect(status().isConflict());

    }

}