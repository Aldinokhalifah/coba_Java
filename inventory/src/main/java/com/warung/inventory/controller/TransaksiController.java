package com.warung.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warung.inventory.service.TransaksiService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.warung.inventory.dto.request.TransaksiRequest;
import com.warung.inventory.dto.response.ApiResponse;
import com.warung.inventory.dto.response.TransaksiResponse;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {
    private final TransaksiService transaksiService;

    public TransaksiController(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTransaksi() {
        List<TransaksiResponse> transaksiResponses = transaksiService.getAllTransaksi();

        return ResponseEntity.ok(
                ApiResponse.<List<TransaksiResponse>>builder()
                        .success(true)
                        .message("Data transaksi berhasil diambil")
                        .data(transaksiResponses)
                        .build()
        );
    }


    @GetMapping("/produk/{produkId}")
    public ResponseEntity<Object> getTransaksiByProdukId(@PathVariable UUID produkId) {
        List<TransaksiResponse> transaksiByProdukIdList = transaksiService.getTransaksiByProdukId(produkId);

        return ResponseEntity.ok(
                ApiResponse.<List<TransaksiResponse>>builder()
                        .success(true)
                        .message("Data transaksi berhasil diambil")
                        .data(transaksiByProdukIdList)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Object> createTransaksi(@RequestBody  @Valid TransaksiRequest request) {
        TransaksiResponse transaksi = transaksiService.createTransaksi(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<TransaksiResponse>builder()
                        .success(true)
                        .message("Data transaksi berhasil dibuat")
                        .data(transaksi)
                        .build()
        );
    }
}
