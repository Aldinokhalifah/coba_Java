package com.warung.inventory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.warung.inventory.dto.request.TransaksiRequest;
import com.warung.inventory.dto.response.TransaksiResponse;
import com.warung.inventory.exception.InsufficientStockException;
import com.warung.inventory.exception.ResourceNotFoundException;
import com.warung.inventory.model.Produk;
import com.warung.inventory.model.Transaksi;
import com.warung.inventory.repository.ProdukRepository;
import com.warung.inventory.repository.TransaksiRepository;

@Service
public class TransaksiService {

    private final ProdukRepository produkRepository;
    private final TransaksiRepository transaksiRepository;

    public TransaksiService(ProdukRepository produkRepository, TransaksiRepository transaksiRepository) {
        this.produkRepository = produkRepository;
        this.transaksiRepository = transaksiRepository;
    }

    private TransaksiResponse toResponse(Transaksi transaksi) {
        return TransaksiResponse.builder()
                .id(transaksi.getId())
                .jenis(transaksi.getJenis())
                .jumlah(transaksi.getJumlah())
                .keterangan(transaksi.getKeterangan())
                .namaProduk(transaksi.getProduk().getNama())
                .tanggal(transaksi.getTanggal())
                .produkId(transaksi.getProduk().getId())
                .build();
    }

    public List<TransaksiResponse> getAllTransaksi() {
        List<Transaksi> transaksis = transaksiRepository.findAll();

        List<TransaksiResponse> responses = transaksis.stream().map(this::toResponse).collect(Collectors.toList());

        return responses;
    }

    public List<TransaksiResponse> getTransaksiByProdukId(UUID id) {
        List<Transaksi> transaksis = transaksiRepository.findByProdukId(id);

        List<TransaksiResponse> responses = transaksis.stream().map(this::toResponse).collect(Collectors.toList());

        return responses;
    }

    public TransaksiResponse createTransaksi(TransaksiRequest request) {
        Produk produk = produkRepository.findById(request.getProdukID()).orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));

        if (request.getJenis() == Transaksi.Jenis.KELUAR) {
            boolean cekStok = produk.getStokSaatIni() < request.getJumlah();

            if (cekStok) {
                throw new InsufficientStockException("Stok tidak mencukupi");
            }
            produk.setStokSaatIni(produk.getStokSaatIni() - request.getJumlah());
        } else {
            produk.setStokSaatIni(produk.getStokSaatIni() + request.getJumlah());
        }

        produkRepository.save(produk);

        Transaksi transaksi = Transaksi.builder()
            .jenis(request.getJenis())
            .jumlah(request.getJumlah())
            .keterangan(request.getKeterangan())
            .produk(produk)
            .tanggal(LocalDateTime.now())
            .build();

        transaksiRepository.save(transaksi);

        return toResponse(transaksi);
    }
}
