package com.warung.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ProdukRequest {
    @NotBlank(message="Nama wajib diisi")
    @Size(max = 256, message = "Nama maksimal 50 karakter")
    String nama;

    @NotBlank(message="Satuan wajib diisi")
    @Size(max = 16, message = "Satuan maksimal 50 karakter")
    String satuan;

    @NotNull(message="Harga Beli wajib diisi")
    @Min(0)
    Long hargaBeli;

    @NotNull(message="Harga Jual wajib diisi")
    @Min(0)
    Long hargaJual;

    @NotNull(message="Stock minimum wajib diisi")
    @Min(0)
    Integer stokMinimum;
}
