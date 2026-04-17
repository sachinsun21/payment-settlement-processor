package com.example.offer.repository;

import com.example.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OfferRepository
        extends JpaRepository<Offer, Long> {

    Optional<Offer> findByOfferId(String offerId);

    List<Offer> findByFreightType(String freightType);

    List<Offer> findByStatus(String status);

    List<Offer> findByCarrierId(String carrierId);
}
