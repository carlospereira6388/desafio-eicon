package br.com.eicon.desafio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import br.com.eicon.desafio.dto.OrderDataDTO;
import br.com.eicon.desafio.dto.OrderResponseDTO;
import br.com.eicon.desafio.dto.UserDataDTO;
import br.com.eicon.desafio.model.Role;
import br.com.eicon.desafio.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderDataDTO orderDataDTO;
    private List<OrderDataDTO> orderDataDTOList;
    
    private String adminAccessToken;

    @BeforeEach
    public void setup() throws Exception {
        orderDataDTO = new OrderDataDTO();
        orderDataDTO.setControlNumber(1L);
        orderDataDTO.setProductName("Produto Teste");
        orderDataDTO.setUnitPrice(100.0);
        orderDataDTO.setQuantity(1);

        orderDataDTOList = Arrays.asList(orderDataDTO, orderDataDTO, orderDataDTO);

        registerUser(Role.ROLE_ADMIN);
        registerUser(Role.ROLE_USER);
    }

    private void registerUser(Role role) throws Exception {
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setName("Usuário " + role.name());
        userDataDTO.setEmail(UUID.randomUUID().toString() + "@teste.com");
        userDataDTO.setPassword("Senha@102030");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userDataDTO.setRoles(roles);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDataDTO)))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    AuthResponseDTO authResponse = objectMapper.readValue(response, AuthResponseDTO.class);
                    if (role == Role.ROLE_ADMIN) {
                        adminAccessToken = authResponse.getToken();
                    }
                });
    }

    @Test
    public void testAdminCanCreateOrders() throws Exception {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        when(orderService.createOrders(any(List.class))).thenReturn(Arrays.asList(orderResponseDTO));

        mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDataDTOList)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).createOrders(any(List.class));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        List<OrderResponseDTO> orders = Arrays.asList(new OrderResponseDTO());
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders")
                .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        when(orderService.getOrder(1L)).thenReturn(orderResponseDTO);

        mockMvc.perform(get("/orders/1")
                .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getOrder(1L);
    }
}
