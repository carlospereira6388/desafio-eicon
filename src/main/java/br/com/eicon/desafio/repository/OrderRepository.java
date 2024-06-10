package br.com.eicon.desafio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eicon.desafio.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByControlNumber(Long controlNumber);
    
    boolean existsByControlNumber(Long controlNumber);
}
