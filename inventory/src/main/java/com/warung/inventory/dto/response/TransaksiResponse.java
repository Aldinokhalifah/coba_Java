package com.warung.inventory.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warung.inventory.model.Transaksi.Jenis;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransaksiResponse {
    private UUID id;
    private LocalDateTime tanggal;
    private Jenis jenis;
    private Integer jumlah;
    private String keterangan;
    private UUID produkId;
    private String namaProduk;
}
