package com.example.offer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String offerId;        // like OFF001

    @Column(nullable = false)
    private String origin;         // pickup location

    @Column(nullable = false)
    private String destination;    // delivery location

    private String freightType;    // VAN, INTERMODAL, BULK

    private String status;         // CREATED, ACCEPTED, REJECTED

    private Double price;

    private String carrierId;      // carrier assigned
}