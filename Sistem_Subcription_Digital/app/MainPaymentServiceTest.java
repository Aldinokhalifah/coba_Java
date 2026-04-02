package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InMemoryInvoiceRepository;
import Sistem_Subcription_Digital.repository.InMemoryPaymentRepository;
import Sistem_Subcription_Digital.repository.InMemorySubscriptionRepository;
import Sistem_Subcription_Digital.service.PaymentService;
import java.time.LocalDateTime;

public class MainPaymentServiceTest {
    public static void main(String[] args) {
        System.out.println("========== PAYMENT SERVICE TESTING ==========\n");

        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();
        InMemoryInvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        InMemorySubscriptionRepository subscriptionRepository = new InMemorySubscriptionRepository();

        PaymentService paymentService = new PaymentService(paymentRepository, invoiceRepository, subscriptionRepository);

        // Setup data
        User user = new User(1L, "John Doe", "john@email.com", LocalDateTime.now());
        Plan plan = new Plan(1L, "Premium", "PREMIUM", Plan.Period.MONTHLY, 100.0);
        Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
        subscription.setIdForRepository(1L);
        subscriptionRepository.save(subscription);

        Invoice invoice = new Invoice(
                null,
                subscription,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                100.0,
                Invoice.InvoiceStatus.PENDING,
                null,
                0,
                "Test Invoice"
        );
        invoiceRepository.save(invoice);

        // Test 1: Attempt Payment - Valid
        System.out.println("TEST 1: Attempt Payment - Valid");
        try {
            Payment payment = paymentService.attemptPayment(invoice, Payment.PaymentMethod.MANUAL);
            System.out.println("✓ Payment attempt created successfully");
            System.out.println("  - Status: " + payment.getStatus());
            System.out.println("  - Amount: Rp" + payment.getAmount());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Attempt Payment - Null Invoice
        System.out.println("TEST 2: Attempt Payment - Null Invoice (should fail)");
        try {
            paymentService.attemptPayment(null, Payment.PaymentMethod.MANUAL);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Attempt Payment - Null Method
        System.out.println("TEST 3: Attempt Payment - Null Method (should fail)");
        try {
            paymentService.attemptPayment(invoice, null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Attempt Payment - Invoice Already Paid
        System.out.println("TEST 4: Attempt Payment - Invoice Already Paid (should fail)");
        try {
            Invoice paidInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    50.0,
                    Invoice.InvoiceStatus.PAID,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(paidInvoice);

            paymentService.attemptPayment(paidInvoice, Payment.PaymentMethod.MANUAL);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Attempt Payment - Invoice Expired
        System.out.println("TEST 5: Attempt Payment - Invoice Expired (should fail)");
        try {
            Invoice expiredInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    60.0,
                    Invoice.InvoiceStatus.EXPIRED,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(expiredInvoice);

            paymentService.attemptPayment(expiredInvoice, Payment.PaymentMethod.MANUAL);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Attempt Payment - Invalid Amount
        System.out.println("TEST 6: Attempt Payment - Invalid Amount (should fail)");
        try {
            Invoice invalidInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    0,
                    Invoice.InvoiceStatus.PENDING,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(invalidInvoice);

            paymentService.attemptPayment(invalidInvoice, Payment.PaymentMethod.MANUAL);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 7: Attempt Payment - Duplicate Payment
        System.out.println("TEST 7: Attempt Payment - Duplicate Payment (should fail)");
        try {
            Invoice dupInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    75.0,
                    Invoice.InvoiceStatus.PENDING,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(dupInvoice);

            Payment payment1 = paymentService.attemptPayment(dupInvoice, Payment.PaymentMethod.MANUAL);
            System.out.println("✓ First payment created: " + payment1.getStatus());

            Payment payment2 = paymentService.attemptPayment(dupInvoice, Payment.PaymentMethod.CARD);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 8: Process Payment For Invoice - Valid
        System.out.println("TEST 8: Process Payment For Invoice - Valid");
        try {
            Invoice processInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    150.0,
                    Invoice.InvoiceStatus.PENDING,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(processInvoice);

            Payment payment = paymentService.processPaymentForInvoice(processInvoice.getId(), Payment.PaymentMethod.CARD);
            System.out.println("✓ Payment processed successfully");
            System.out.println("  - Status: " + payment.getStatus());
            System.out.println("  - Amount: Rp" + payment.getAmount());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 9: Process Payment For Invoice - Null Method
        System.out.println("TEST 9: Process Payment For Invoice - Null Method (should fail)");
        try {
            Invoice testInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    80.0,
                    Invoice.InvoiceStatus.PENDING,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(testInvoice);

            paymentService.processPaymentForInvoice(testInvoice.getId(), null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 10: Process Payment For Invoice - Invoice Not Pending
        System.out.println("TEST 10: Process Payment For Invoice - Invoice Not Pending (should fail)");
        try {
            Invoice notPendingInvoice = new Invoice(
                    null,
                    subscription,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(7),
                    90.0,
                    Invoice.InvoiceStatus.PAID,
                    null,
                    0,
                    ""
            );
            invoiceRepository.save(notPendingInvoice);

            paymentService.processPaymentForInvoice(notPendingInvoice.getId(), Payment.PaymentMethod.MANUAL);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 11: Process Payment - Invalid ID
        System.out.println("TEST 11: Process Payment - Invalid ID (should fail)");
        try {
            paymentService.processPayment(999L);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 12: Get Payment Repository
        System.out.println("TEST 12: Get Payment Repository");
        try {
            var repo = paymentService.getPaymentRepository();
            System.out.println("✓ Payment repository retrieved successfully");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("========== END OF PAYMENT SERVICE TESTING ==========");
    }
}