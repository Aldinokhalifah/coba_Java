package com.warung.inventory.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.warung.inventory.model.Transaksi.Jenis;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TransaksiRequest {

    @NotNull(message = "ID produk wajib diisi")
    UUID produkID;

    @NotNull(message = "Jenis transaksi wajib diisi")
    Jenis jenis;

    @NotNull(message = "Jumlah wajib diisi")
    @Positive(message = "Jumlah harus lebih besar dari nol")
    Integer jumlah;

    @Size(max = 256, message = "Keterangan maksimal 256 karakter")
    String keterangan;
}
