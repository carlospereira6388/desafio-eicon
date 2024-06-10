package br.com.eicon.desafio.service;

import java.util.List;

import br.com.eicon.desafio.dto.AuthResponseDTO;
import br.com.eicon.desafio.dto.UserDataDTO;
import br.com.eicon.desafio.dto.UserResponseDTO;

public interface UserService {
	AuthResponseDTO login(String email, String password);
	AuthResponseDTO register(UserDataDTO userDataDTO);
    List<UserResponseDTO> getAllUsers();
}