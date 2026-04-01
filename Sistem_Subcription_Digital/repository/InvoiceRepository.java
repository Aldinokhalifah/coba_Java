package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Invoice;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {

    Optional<Invoice> findById(Long id);
    List<Invoice> findBySubscriptionId(Long subscriptionId);
    Optional<Invoice> findLatestBySubscriptionId(Long subscriptionId);
    Optional<Invoice> findUnpaidBySubscription(Long subscriptionId);
    List<Invoice> findOverdueInvoices(LocalDate today);
    List<Invoice> existsBySubscriptionAndBillingDate(Long subscriptionId, LocalDate billingDate);
    void save(Invoice invoice);
}