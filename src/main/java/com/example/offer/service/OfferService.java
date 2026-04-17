package com.example.offer.service;

import com.example.offer.dto.OfferDTO;
import com.example.offer.kafka.KafkaProducerService;
import com.example.offer.model.Offer;
import com.example.offer.repository.OfferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final KafkaProducerService kafkaProducerService;
    public List<Offer> getAll(){
        return offerRepository.findAll();
   }
   public Offer getById(Long id){
        return offerRepository.findById(id).
                orElseThrow(()->new RuntimeException("Offer not found: "+id));
   }
    @Transactional
    public Offer create(OfferDTO dto) {
        Offer offer = new Offer();
        offer.setOfferId(dto.getOfferId());
        offer.setOrigin(dto.getOrigin());
        offer.setDestination(dto.getDestination());
        offer.setFreightType(dto.getFreightType());
        offer.setStatus(dto.getStatus());
        offer.setPrice(dto.getPrice());
        offer.setCarrierId(dto.getCarrierId());
        Offer saved = offerRepository.save(offer);
        //kafkaProducerService.publishOfferCreated(dto);
        return saved;
    }

    @Transactional
    public Offer update(Long id, OfferDTO dto) {
        Offer existing = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found: " + id));
        existing.setOfferId(dto.getOfferId());
        existing.setOrigin(dto.getOrigin());
        existing.setDestination(dto.getDestination());
        existing.setFreightType(dto.getFreightType());
        existing.setStatus(dto.getStatus());
        existing.setPrice(dto.getPrice());
        existing.setCarrierId(dto.getCarrierId());
        Offer saved = offerRepository.save(existing);
        //kafkaProducerService.publishOfferUpdated(dto);
        return saved;
    }  @Transactional
    public void delete(Long id){
        offerRepository.deleteById(id);
  }
}
