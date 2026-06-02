package com.warung.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warung.inventory.service.ProdukService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.warung.inventory.dto.request.ProdukRequest;
import com.warung.inventory.dto.response.ApiResponse;
import com.warung.inventory.dto.response.ProdukResponse;

@RestController
@RequestMapping("/api/produk")
public class ProdukController {

    private final ProdukService produkService;

    public ProdukController(ProdukService produkService) {
        this.produkService = produkService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProduk() {
        List<ProdukResponse> produkResponses = produkService.getAllProduk();

        return ResponseEntity.ok(
                ApiResponse.<List<ProdukResponse>>builder()
                        .success(true)
                        .message("Data produk berhasil diambil")
                        .data(produkResponses)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProdukById(@PathVariable UUID id) {
        ProdukResponse produk = produkService.getProdukById(id);

        return ResponseEntity.ok(
                ApiResponse.<ProdukResponse>builder()
                        .success(true)
                        .message("Data produk berhasil diambil")
                        .data(produk)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Object> createProduk(@RequestBody @Valid ProdukRequest request) {
        ProdukResponse produk = produkService.createProduk(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ProdukResponse>builder()
                        .success(true)
                        .message("Data produk berhasil dibuat")
                        .data(produk)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduk(@PathVariable UUID id, @RequestBody @Valid ProdukRequest request) {
        ProdukResponse produkUpdated = produkService.updateProduk(id, request);

        return ResponseEntity.ok(
                ApiResponse.<ProdukResponse>builder()
                        .success(true)
                        .message("Data produk berhasil diupdate")
                        .data(produkUpdated)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduk(@PathVariable UUID id) {
        produkService.deleteProduk(id);

        return ResponseEntity.ok(
                ApiResponse.<ProdukResponse>builder()
                        .success(true)
                        .message("Data produk berhasil dihapus")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/stok-menipis")
    public ResponseEntity<Object> getStokMenipis() {
        List<ProdukResponse> stokList = produkService.getStokMenipis();

        return ResponseEntity.ok(
                ApiResponse.<List<ProdukResponse>>builder()
                        .success(true)
                        .message("Data produk berhasil diambil")
                        .data(stokList)
                        .build()
        );
    }
}
