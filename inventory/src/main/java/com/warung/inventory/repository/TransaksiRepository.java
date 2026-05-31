package com.warung.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

import com.warung.inventory.model.Transaksi;

public interface  TransaksiRepository extends JpaRepository<Transaksi, UUID>{
    
    public List<Transaksi> findByProdukId(UUID produkId);
}
