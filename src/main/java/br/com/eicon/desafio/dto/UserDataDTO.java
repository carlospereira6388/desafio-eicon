package br.com.eicon.desafio.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.eicon.desafio.model.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataDTO {
    
    @ApiModelProperty(position = 0)
    @NotEmpty(message = "O campo nome é obrigátorio")
    private String name;
    
    @ApiModelProperty(position = 1)
    @NotEmpty(message = "O campo e-mail é obrigátorio")
    @Email(message = "O e-mail deve ser válido")
    private String email;
    
    @ApiModelProperty(position = 2)
    @NotEmpty(message = "O senha é obrigátorio")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    @ApiModelProperty(position = 3)
    private Set<Role> roles;
}
