package com.warung.inventory.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdukResponse {
    private UUID id;
    private String nama;
    private String satuan;
    private Long hargaBeli;
    private Long hargaJual;
    private Integer stokSaatIni = 0;
    private Integer stokMinimum;

    public boolean isStokMenipis() {
        return stokSaatIni < stokMinimum;
    }
}
