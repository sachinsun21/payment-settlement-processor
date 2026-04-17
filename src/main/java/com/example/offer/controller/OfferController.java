package com.example.offer.controller;

import com.example.offer.aws.S3Service;
import com.example.offer.dto.OfferDTO;
import com.example.offer.model.Offer;
import com.example.offer.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final OfferService service;
    @Autowired
    private S3Service s3Service;

    @PostMapping("/offer/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file) throws Exception {

        String result = s3Service.uploadFile(
                file.getOriginalFilename(),
                file.getBytes()
        );
        return ResponseEntity.ok(result);
    }
    @GetMapping("/offer/download/{fileName}")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable String fileName) throws Exception {

        byte[] fileContent = s3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=\"" + fileName + "\"")
                .body(fileContent);
    }

    @GetMapping("/offer")
    public ResponseEntity<List<Offer>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<Offer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/offer")
    public ResponseEntity<Offer> create(
            @Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PutMapping("/offer/{id}")
    public ResponseEntity<Offer> update(
            @PathVariable Long id,
            @Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/offer/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}