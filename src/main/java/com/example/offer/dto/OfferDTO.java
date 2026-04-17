package com.example.offer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    private Long id;

    @NotBlank(message = "Offer ID is required")
    private String offerId;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Freight type is required")
    @Pattern(
            regexp = "^(VAN|INTERMODAL|BULK|CROSS_BORDER)$",
            message = "Freight type must be VAN, INTERMODAL, BULK or CROSS_BORDER"
    )
    private String freightType;

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "^(CREATED|ACCEPTED|REJECTED|PENDING)$",
            message = "Status must be CREATED, ACCEPTED, REJECTED or PENDING"
    )
    private String status;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private String carrierId;
}