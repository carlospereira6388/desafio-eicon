package br.com.eicon.desafio.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders") 
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long controlNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double unitPrice;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "customer_code", nullable = false)
    private AppUser customer;

    @Column(nullable = false)
    private double totalValue;
}
