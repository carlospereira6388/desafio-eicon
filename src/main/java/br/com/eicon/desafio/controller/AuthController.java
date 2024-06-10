package br.com.eicon.desafio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eicon.desafio.dto.AuthResponseDTO;
import br.com.eicon.desafio.dto.UserDataDTO;
import br.com.eicon.desafio.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/auth")
@Api(tags = "auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	@ApiOperation(
        value = "Efetua login", 
        response = AuthResponseDTO.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Login efetuado com sucesso"),
		@ApiResponse(code = 400, message = "Solicitação inválida. Verifique o formato dos seus dados"), 
		@ApiResponse(code = 422, message = "E-mail/senha inválidos") 
	})
	public ResponseEntity<AuthResponseDTO> login(@RequestBody UserDataDTO userDataDTO) {
	    AuthResponseDTO user = userService.login(userDataDTO.getEmail(), userDataDTO.getPassword());
	    return ResponseEntity.ok(user);
	}
	
	@PostMapping("/register")
	@ApiOperation(
        value = "Registra um novo usuário", 
        response = AuthResponseDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Usuário registrado com sucesso"),
		@ApiResponse(code = 400, message = "Solicitação inválida. Verifique o formato dos seus dados"),
		@ApiResponse(code = 403, message = "Acesso negado. Você precisa de autorização para acessar esse endpoint"),
		@ApiResponse(code = 422, message = "E-mail já está em uso") 
	})
	public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserDataDTO userDataDTO) {
	    AuthResponseDTO user = userService.register(userDataDTO);
	    return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
}