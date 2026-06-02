package com.warung.inventory.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.warung.inventory.dto.request.ProdukRequest;
import com.warung.inventory.dto.response.ProdukResponse;
import com.warung.inventory.exception.ResourceNotFoundException;
import com.warung.inventory.model.Produk;
import com.warung.inventory.repository.ProdukRepository;

@Service
public class ProdukService {

    private final ProdukRepository produkRepository;

    public ProdukService(ProdukRepository produkRepository) {
        this.produkRepository = produkRepository;
    }

    private ProdukResponse toResponse(Produk produk) {
        return ProdukResponse.builder()
                .id(produk.getId())
                .nama(produk.getNama())
                .satuan(produk.getSatuan())
                .hargaBeli(produk.getHargaBeli())
                .hargaJual(produk.getHargaJual())
                .stokSaatIni(produk.getStokSaatIni())
                .stokMinimum(produk.getStokMinimum())
                .build();
    }

    public List<ProdukResponse> getAllProduk() {
        List<Produk> produks = produkRepository.findAll();

        List<ProdukResponse> responses = produks.stream().map(this::toResponse).collect(Collectors.toList());

        return responses;
    }

    public ProdukResponse getProdukById(UUID id) {
        Produk produk = produkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));
        return toResponse(produk);
    }

    public ProdukResponse createProduk(ProdukRequest request) {
        Produk produk = Produk.builder()
                .nama(request.getNama())
                .satuan(request.getSatuan())
                .hargaBeli(request.getHargaBeli())
                .hargaJual(request.getHargaJual())
                .stokMinimum(request.getStokMinimum())
                .build();

        Produk saved = produkRepository.save(produk);

        return toResponse(saved);
    }

    public ProdukResponse updateProduk(UUID id, ProdukRequest request) {
        Produk produk = produkRepository.findById(id).orElseThrow(() -> new IllegalStateException("Produk tidak ditemukan"));

        produk.setNama(request.getNama());
        produk.setHargaBeli(request.getHargaBeli());
        produk.setHargaJual(request.getHargaJual());
        produk.setSatuan(request.getSatuan());
        produk.setStokMinimum(request.getStokMinimum());

        Produk saved = produkRepository.save(produk);

        return toResponse(saved);
    }

    public void deleteProduk(UUID id) {
        Produk produk = produkRepository.findById(id).orElseThrow(() -> new IllegalStateException("Produk tidak ditemukan"));

        produkRepository.delete(produk);
    }

    public List<ProdukResponse> getStokMenipis() {
        List<Produk> stokProdukMenipis = produkRepository.findStokMenipis();

        List<ProdukResponse> responses = stokProdukMenipis.stream().map(this::toResponse).collect(Collectors.toList());

        return responses;
    }
}
