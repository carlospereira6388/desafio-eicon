package br.com.eicon.desafio.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDataDTO {

    @ApiModelProperty(position = 0, notes = "Número de controle.")
    @NotEmpty(message = "O campo número de controle é obrigátorio")
    private Long controlNumber;

    @ApiModelProperty(position = 1, notes = "Data de cadastro do pedido.")
    private Date registrationDate;

    @ApiModelProperty(position = 2, required = true, notes = "Nome do produto.")
    @NotEmpty(message = "O campo nome do produto é obrigátorio")
    private String productName;

    @ApiModelProperty(position = 3, required = true, notes = "Valor monetário unitário do produto.")
    @NotEmpty(message = "O campo valor é obrigátorio")
    private double unitPrice;
    
    @ApiModelProperty(position = 4, notes = "Quantidade de produtos.")
    private Integer quantity;
}