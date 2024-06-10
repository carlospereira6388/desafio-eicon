package br.com.eicon.desafio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eicon.desafio.dto.OrderDataDTO;
import br.com.eicon.desafio.dto.OrderResponseDTO;
import br.com.eicon.desafio.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/orders")
@Api(tags = "orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Criar novos pedidos", response = OrderResponseDTO.class, responseContainer = "List", authorizations = {@Authorization(value="apiKey")})
	@ApiResponses(value = {
	   @ApiResponse(code = 201, message = "Pedidos criados com sucesso"),
	   @ApiResponse(code = 400, message = "Dados inválidos do pedido"),
	   @ApiResponse(code = 413, message = "Excedido o limite de pedidos permitidos")
	})
	public ResponseEntity<List<OrderResponseDTO>> createOrders(@Valid @RequestBody List<OrderDataDTO> orderDTOs) {
	   if (orderDTOs.size() > 10) {
	       return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
	   }
	   List<OrderResponseDTO> orders = orderService.createOrders(orderDTOs);
	   return ResponseEntity.status(HttpStatus.CREATED).body(orders);
	}


    @GetMapping
    @ApiOperation(value = "Listar todos os pedidos", response = OrderResponseDTO.class, responseContainer = "List", authorizations = {@Authorization(value="apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Pedidos listados com sucesso")})
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{controlNumber}")
    @ApiOperation(value = "Obter um pedido pelo número de controle", response = OrderResponseDTO.class, authorizations = {@Authorization(value="apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Pedido encontrado"), @ApiResponse(code = 404, message = "Pedido não encontrado")})
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long controlNumber) {
        OrderResponseDTO order = orderService.getOrder(controlNumber);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{controlNumber}")
    @ApiOperation(value = "Atualizar um pedido", response = OrderResponseDTO.class, authorizations = {@Authorization(value="apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Pedido atualizado com sucesso"), @ApiResponse(code = 400, message = "Dados inválidos do pedido"), @ApiResponse(code = 404, message = "Pedido não encontrado")})
    public ResponseEntity<OrderResponseDTO> updateOrder(@Valid @PathVariable Long controlNumber, @RequestBody OrderDataDTO orderDataDTO) {
        OrderResponseDTO order = orderService.updateOrder(controlNumber, orderDataDTO);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{controlNumber}")
    @ApiOperation(value = "Deletar um pedido", authorizations = {@Authorization(value="apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Pedido excluído com sucesso"), @ApiResponse(code = 404, message = "Pedido não encontrado")})
    public ResponseEntity<Void> deleteOrder(@PathVariable Long controlNumber) {
        boolean deleted = orderService.deleteOrder(controlNumber);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
