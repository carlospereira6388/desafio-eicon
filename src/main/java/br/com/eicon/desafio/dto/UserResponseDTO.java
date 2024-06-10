package br.com.eicon.desafio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDTO {
    
    @ApiModelProperty(position = 0)
    private String name;
    
    @ApiModelProperty(position = 1)
    private String email;

    @ApiModelProperty(position = 2)
    private Set<String> roles;
}
