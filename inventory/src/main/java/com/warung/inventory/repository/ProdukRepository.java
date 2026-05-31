package com.warung.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

import com.warung.inventory.model.Produk;
import java.util.List;

public interface ProdukRepository extends JpaRepository<Produk, UUID>{
    
    @Query("SELECT p FROM Produk p WHERE p.stokSaatIni < p.stokMinimum")
    List<Produk> findStokMenipis();
}
