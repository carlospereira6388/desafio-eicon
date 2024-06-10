package br.com.eicon.desafio.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponseDTO {

    @ApiModelProperty(position = 0, notes = "Número de controle do pedido.")
    private Long controlNumber;

    @ApiModelProperty(position = 1, notes = "Data de cadastro do pedido.")
    private Date registrationDate;

    @ApiModelProperty(position = 2, notes = "Nome do produto.")
    private String productName;

    @ApiModelProperty(position = 3, notes = "Valor monetário unitário do produto.")
    private double unitPrice;

    @ApiModelProperty(position = 4, notes = "Quantidade de produtos.")
    private Integer quantity;

    @ApiModelProperty(position = 5, notes = "Informações do cliente.")
    private UserResponseDTO customer;

    @ApiModelProperty(position = 6, notes = "Valor total do pedido com descontos aplicados.")
    private double totalValue;
}
