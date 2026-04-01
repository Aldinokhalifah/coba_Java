package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findById(Long id);
    List<Payment> findByInvoiceId(Long invoiceId);
    List<Payment> findPendingByInvoiceId(Long invoiceId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    void save(Payment payment);
}
