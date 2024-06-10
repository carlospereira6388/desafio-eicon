package br.com.eicon.desafio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.eicon.desafio.dto.OrderDataDTO;
import br.com.eicon.desafio.dto.OrderResponseDTO;
import br.com.eicon.desafio.exception.CustomException;
import br.com.eicon.desafio.model.AppUser;
import br.com.eicon.desafio.model.Order;
import br.com.eicon.desafio.repository.OrderRepository;
import br.com.eicon.desafio.security.CustomUserDetailsService;
import br.com.eicon.desafio.utils.Mapper;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private Mapper mapper;
    
    @Override
    public List<OrderResponseDTO> createOrders(List<OrderDataDTO> orderDTOs) {
        AppUser loggedUser = customUserDetailsService.getLoggedUser();
        List<Order> orders = new ArrayList<>();
        for (OrderDataDTO orderDTO : orderDTOs) {
            if (orderRepository.existsByControlNumber(orderDTO.getControlNumber())) {
                throw new CustomException("Número de controle já cadastrado: " + orderDTO.getControlNumber(), HttpStatus.BAD_REQUEST);
            }
            Order order = mapper.convert(orderDTO, Order.class);
            setDefaultValues(order);
            applyBusinessRules(order);
            order.setCustomer(loggedUser);
            orders.add(orderRepository.save(order));
        }
        return orders.stream()
                     .map(order -> mapper.convert(order, OrderResponseDTO.class))
                     .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    applyBusinessRules(order);
                    return mapper.convert(order, OrderResponseDTO.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrder(Long controlNumber) {
        Order order = orderRepository.findByControlNumber(controlNumber).orElseThrow(() ->
            new CustomException("Pedido não encontrado", HttpStatus.NOT_FOUND));
        return mapper.convert(order, OrderResponseDTO.class);
    }
    
    public OrderResponseDTO updateOrder(Long controlNumber, OrderDataDTO orderDTO) {
        try {
            Order order = orderRepository.findByControlNumber(controlNumber).orElseThrow(() ->
                    new CustomException("Pedido não encontrado", HttpStatus.NOT_FOUND));
            applyBusinessRules(order);
            order = orderRepository.save(order);
            return mapper.convert(order, OrderResponseDTO.class);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar pedido", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean deleteOrder(Long controlNumber) {
        Order order = orderRepository.findByControlNumber(controlNumber).orElseThrow(() ->
            new CustomException("Pedido não encontrado", HttpStatus.NOT_FOUND));
        orderRepository.delete(order);
        return true;
    }

    private void setDefaultValues(Order order) {
        if (order.getRegistrationDate() == null) {
            order.setRegistrationDate(new Date());
        }
        if (order.getQuantity() == null) {
            order.setQuantity(1);
        }
    }

    private void applyBusinessRules(Order order) {
        double totalValue = order.getUnitPrice() * order.getQuantity();
        if (order.getQuantity() >= 10) {
            totalValue *= 0.9;
        } else if (order.getQuantity() >= 5) {
            totalValue *= 0.95;
        }
        order.setTotalValue(totalValue);
    }
}
