package br.com.eicon.desafio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eicon.desafio.dto.AuthResponseDTO;
import br.com.eicon.desafio.dto.UserDataDTO;
import br.com.eicon.desafio.dto.UserResponseDTO;
import br.com.eicon.desafio.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDataDTO userDataDTO;
    private AuthResponseDTO authResponseDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void setup() throws Exception {
        userDataDTO = new UserDataDTO();
        userDataDTO.setName("Usuário Teste");
        userDataDTO.setEmail(UUID.randomUUID().toString() + "@teste.com");
        userDataDTO.setPassword("Teste@102030");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName(userDataDTO.getName());
        userResponseDTO.setEmail(userDataDTO.getEmail());
        
        authResponseDTO = new AuthResponseDTO(true, userResponseDTO, "accessTokenTest");

        when(userService.login(any(String.class), any(String.class))).thenReturn(authResponseDTO);
        when(userService.register(any(UserDataDTO.class))).thenReturn(authResponseDTO);
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDataDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.auth").value(true))
                .andExpect(jsonPath("$.user.name").value(userResponseDTO.getName()))
                .andExpect(jsonPath("$.user.email").value(userResponseDTO.getEmail()))
                .andExpect(jsonPath("$.token").exists());

        verify(userService, times(1)).login(any(String.class), any(String.class));
    }

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDataDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.auth").value(true))
                .andExpect(jsonPath("$.user.name").value(userResponseDTO.getName()))
                .andExpect(jsonPath("$.user.email").value(userResponseDTO.getEmail()))
                .andExpect(jsonPath("$.token").exists());

        verify(userService, times(1)).register(any(UserDataDTO.class));
    }
}