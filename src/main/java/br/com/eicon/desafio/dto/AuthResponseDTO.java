package br.com.eicon.desafio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    
	@ApiModelProperty(position = 0)
	private boolean auth;
	
	@ApiModelProperty(position = 1)
	private UserResponseDTO user;
	
	@ApiModelProperty(position = 2)
    private String token;
}