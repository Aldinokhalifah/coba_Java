package Sistem_Subcription_Digital.repository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import Sistem_Subcription_Digital.model.Invoice;

public interface InvoiceRepository {

    Optional<Invoice> findById(Long id);
    List<Invoice> findBySubscriptionId(Long subscriptionId);
    Optional<Invoice> findUnpaidBySubscription(Long subscriptionId);
    List<Invoice> findOverdueInvoices(LocalDate today);
    void save(Invoice invoice);
}

