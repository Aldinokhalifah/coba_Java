package com.warung.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import  java.util.UUID;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produk {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 256)
    private String nama;

    @Column(nullable = false, length = 16)
    private String satuan;

    @Column(nullable = false)
    private Long hargaBeli;

    @Column(nullable = false)
    private Long hargaJual;

    @Column(nullable=false)
    @Builder.Default
    private Integer stokSaatIni = 0;

    @Column(nullable=false)
    private Integer stokMinimum;
}
