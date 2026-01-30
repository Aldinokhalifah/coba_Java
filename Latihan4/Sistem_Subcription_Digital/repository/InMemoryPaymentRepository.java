package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Payment;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryPaymentRepository implements PaymentRepository {
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, Payment> storage = new ConcurrentHashMap<>();
    
    @Override
    public Optional<Payment> findById(Long id) {
        if(id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Payment> findByInvoiceId(Long invoiceId) {
        if(invoiceId == null) {
            return List.of();
        }

        return storage.values().stream().filter(s -> s.getInvoice() != null && Objects.equals(s.getInvoice().getId(), invoiceId)).collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        if(status == null) {
            return List.of();
        }

        return storage.values().stream()
        .filter(p -> p.getStatus() != null && p.getStatus() == status)
        .collect(Collectors.toList());
    }

    @Override
    public void save(Payment payment) {
        if(payment == null) {
            throw new IllegalArgumentException("Payment is null");
        }

        if(payment.getId() == null) {
            Long id = idSeq.incrementAndGet();

            payment.setIdForRepository(id);
            storage.put(id, payment);
        } else {
            storage.put(payment.getId(), payment);
        }
    }

    @Override
    public List<Payment> findPendingByInvoiceId(Long invoiceId) {
        if(invoiceId == null) {
            return List.of();
        }

        return storage
        .values().stream()
        .filter(s -> s.getInvoice() != null && Objects.equals(s.getInvoice().getId(), invoiceId))
        .filter(s -> s.getStatus() == Payment.PaymentStatus.PENDING)
        .collect(Collectors.toList());
    }
}
