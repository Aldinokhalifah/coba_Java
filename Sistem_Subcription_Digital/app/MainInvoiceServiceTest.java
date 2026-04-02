package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InMemoryInvoiceRepository;
import Sistem_Subcription_Digital.repository.InMemoryPaymentRepository;
import Sistem_Subcription_Digital.repository.InMemorySubscriptionRepository;
import Sistem_Subcription_Digital.service.InvoiceService;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainInvoiceServiceTest {
    public static void main(String[] args) {
        System.out.println("========== INVOICE SERVICE TESTING ==========\n");

        InMemoryInvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();
        InMemorySubscriptionRepository subscriptionRepository = new InMemorySubscriptionRepository();

        InvoiceService invoiceService = new InvoiceService(invoiceRepository, paymentRepository, subscriptionRepository);

        // Setup data
        User user = new User(1L, "Jane Smith", "jane@email.com", LocalDateTime.now());
        Plan plan = new Plan(1L, "Standard", "STD", Plan.Period.MONTHLY, 50.0);

        // Test 1: Create Initial Invoice - Valid
        System.out.println("TEST 1: Create Initial Invoice - Valid");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            subscription.setIdForRepository(1L);
            subscriptionRepository.save(subscription);

            Invoice invoice = invoiceService.createInitialInvoice(subscription);
            System.out.println("✓ Initial invoice created successfully");
            System.out.println("  - Amount: Rp" + invoice.getAmount());
            System.out.println("  - Status: " + invoice.getStatus());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Create Initial Invoice - Null Subscription
        System.out.println("TEST 2: Create Initial Invoice - Null Subscription (should fail)");
        try {
            invoiceService.createInitialInvoice(null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Create Initial Invoice - Inactive Subscription
        System.out.println("TEST 3: Create Initial Invoice - Inactive Subscription (should fail)");
        try {
            Subscription inactiveSub = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            inactiveSub.setIdForRepository(2L);
            inactiveSub.cancel();
            subscriptionRepository.save(inactiveSub);

            invoiceService.createInitialInvoice(inactiveSub);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Create Initial Invoice - Null Plan
        System.out.println("TEST 4: Create Initial Invoice - Null Plan (should fail)");
        try {
            Subscription subNoPlan = new Subscription(1L, user, null, LocalDateTime.now(), LocalDateTime.now(), 
                    LocalDateTime.now().plusMonths(1), Subscription.SubscriptionStatus.ACTIVE, true);
            subscriptionRepository.save(subNoPlan);

            invoiceService.createInitialInvoice(subNoPlan);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Create Initial Invoice - Duplicate Invoice
        System.out.println("TEST 5: Create Initial Invoice - Duplicate Invoice (should fail)");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            subscription.setIdForRepository(3L);
            subscriptionRepository.save(subscription);

            Invoice inv1 = invoiceService.createInitialInvoice(subscription);
            System.out.println("✓ First invoice created");

            Invoice inv2 = invoiceService.createInitialInvoice(subscription);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Create Renewal Invoice - Valid
        System.out.println("TEST 6: Create Renewal Invoice - Valid");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now().minusMonths(1), plan.getPeriod());
            subscription.setIdForRepository(4L);
            subscriptionRepository.save(subscription);

            // Create initial invoice first
            Invoice initialInvoice = invoiceService.createInitialInvoice(subscription);
            initialInvoice.markPaid(LocalDateTime.now());
            invoiceRepository.save(initialInvoice);

            LocalDate billingDate = LocalDate.now();
            Invoice renewalInvoice = invoiceService.createRenewalInvoice(subscription, billingDate);
            System.out.println("✓ Renewal invoice created successfully");
            System.out.println("  - Amount: Rp" + renewalInvoice.getAmount());
            System.out.println("  - Billing Date: " + billingDate);
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 7: Create Renewal Invoice - Null Subscription
        System.out.println("TEST 7: Create Renewal Invoice - Null Subscription (should fail)");
        try {
            invoiceService.createRenewalInvoice(null, LocalDate.now());
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 8: Create Renewal Invoice - Null Billing Date
        System.out.println("TEST 8: Create Renewal Invoice - Null Billing Date (should fail)");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            subscription.setIdForRepository(5L);
            subscriptionRepository.save(subscription);

            invoiceService.createRenewalInvoice(subscription, null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 9: Create Renewal Invoice - Billing Date in Future
        System.out.println("TEST 9: Create Renewal Invoice - Billing Date in Future (should fail)");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            subscription.setIdForRepository(6L);
            subscriptionRepository.save(subscription);

            LocalDate futureDate = LocalDate.now().plusDays(5);
            invoiceService.createRenewalInvoice(subscription, futureDate);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 10: Create Renewal Invoice - Billing Date Before Start Date
        System.out.println("TEST 10: Create Renewal Invoice - Billing Date Before Start Date (should fail)");
        try {
            LocalDateTime futureStart = LocalDateTime.now().plusDays(10);
            Subscription subscription = new Subscription(
                    1L, user, plan, LocalDateTime.now(), futureStart, futureStart.plusMonths(1),
                    Subscription.SubscriptionStatus.ACTIVE, true
            );
            subscription.setIdForRepository(7L);
            subscriptionRepository.save(subscription);

            LocalDate pastBillingDate = LocalDate.now();
            invoiceService.createRenewalInvoice(subscription, pastBillingDate);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 11: Create Renewal Invoice - Duplicate Invoice
        System.out.println("TEST 11: Create Renewal Invoice - Duplicate Invoice (should fail)");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now().minusMonths(2), plan.getPeriod());
            subscription.setIdForRepository(8L);
            subscriptionRepository.save(subscription);

            // Create initial invoice
            Invoice initialInvoice = invoiceService.createInitialInvoice(subscription);
            initialInvoice.markPaid(LocalDateTime.now());
            invoiceRepository.save(initialInvoice);

            LocalDate billingDate = LocalDate.now();
            Invoice renewal1 = invoiceService.createRenewalInvoice(subscription, billingDate);
            System.out.println("✓ First renewal invoice created");

            Invoice renewal2 = invoiceService.createRenewalInvoice(subscription, billingDate);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 12: Create Renewal Invoice - Previous Invoice Unpaid
        System.out.println("TEST 12: Create Renewal Invoice - Previous Invoice Unpaid (should fail)");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now().minusMonths(2), plan.getPeriod());
            subscription.setIdForRepository(9L);
            subscriptionRepository.save(subscription);

            // Create initial invoice (leave unpaid)
            Invoice initialInvoice = invoiceService.createInitialInvoice(subscription);

            LocalDate billingDate = LocalDate.now();
            Invoice renewal = invoiceService.createRenewalInvoice(subscription, billingDate);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 13: Mark Invoice as Paid
        System.out.println("TEST 13: Mark Invoice as Paid");
        try {
            Subscription subscription = Subscription.create(user, plan, LocalDateTime.now(), plan.getPeriod());
            subscription.setIdForRepository(10L);
            subscriptionRepository.save(subscription);

            Invoice invoice = invoiceService.createInitialInvoice(subscription);
            System.out.println("✓ Invoice created with status: " + invoice.getStatus());

            invoice.markPaid(LocalDateTime.now());
            invoiceRepository.save(invoice);
            System.out.println("✓ Invoice marked as paid: " + invoice.getStatus());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("========== END OF INVOICE SERVICE TESTING ==========");
    }
}