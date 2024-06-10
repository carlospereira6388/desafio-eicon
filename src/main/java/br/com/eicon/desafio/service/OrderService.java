package br.com.eicon.desafio.service;

import java.util.List;

import br.com.eicon.desafio.dto.OrderDataDTO;
import br.com.eicon.desafio.dto.OrderResponseDTO;

public interface OrderService {
	
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrder(Long controlNumber);
    boolean deleteOrder(Long controlNumber);
	List<OrderResponseDTO> createOrders(List<OrderDataDTO> orderDTOs);
	OrderResponseDTO updateOrder(Long controlNumber, OrderDataDTO orderDTO);
}
