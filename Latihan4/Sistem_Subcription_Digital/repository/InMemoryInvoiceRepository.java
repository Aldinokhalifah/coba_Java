package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Invoice;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryInvoiceRepository implements InvoiceRepository {
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, Invoice> storage = new ConcurrentHashMap<>();
    
    @Override
    public Optional<Invoice> findById(Long id) {
        if(id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Invoice> findBySubscriptionId(Long subscriptionId) {
        if(subscriptionId ==  null) {
            return List.of();
        }

        return storage.values().stream().filter(s -> s.getSubscription() != null && Objects.equals(s.getSubscription().getId(), subscriptionId)).collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> findUnpaidBySubscription(Long subscriptionId) {
        if(subscriptionId ==  null) {
            return Optional.empty();
        }

        return storage.values().stream()
        .filter(s -> s.getSubscription() != null && Objects.equals(s.getSubscription().getId(), subscriptionId))
        .filter(s -> s.getStatus() != Invoice.InvoiceStatus.PAID)
        .sorted((s1, s2) -> s1.getInvoiceDate().compareTo(s2.getInvoiceDate()))
        .findFirst();
    }

    @Override
    public List<Invoice> findOverdueInvoices(LocalDate today) {
        if(today == null) {
            return List.of();
        }

        return  storage.values().stream()
        .filter(s -> s.getDueDate() != null)
        .filter(s -> s.getStatus() != Invoice.InvoiceStatus.PAID && s.getStatus() != Invoice.InvoiceStatus.EXPIRED)
        .filter(s -> s.getDueDate().toLocalDate().isBefore(today) || s.getDueDate().toLocalDate().isEqual(today))
        .collect(Collectors.toList());
    }

    @Override
    public void save(Invoice invoice) {
        if(invoice == null) {
            throw new IllegalArgumentException("Invoice is null");
        }

        if(invoice.getId() == null) {
            Long id = idSeq.incrementAndGet();

            invoice.setIdForRepository(id);
            storage.put(id, invoice);
        } else {
            storage.put(invoice.getId(), invoice);
        }
    }
}
