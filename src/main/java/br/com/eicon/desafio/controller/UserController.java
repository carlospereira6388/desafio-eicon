package br.com.eicon.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.eicon.desafio.dto.UserResponseDTO;
import br.com.eicon.desafio.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(
        value = "Listar todos os usuários", 
        response = UserResponseDTO.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Usuários listados com sucesso"),
        @ApiResponse(code = 403, message = "Acesso negado. Você precisa de autorização para acessar esse endpoint")
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}