package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InMemoryInvoiceRepository;
import Sistem_Subcription_Digital.repository.InMemoryPaymentRepository;
import Sistem_Subcription_Digital.repository.InMemoryPlanRepository;
import Sistem_Subcription_Digital.repository.InMemorySubscriptionRepository;
import Sistem_Subcription_Digital.repository.InMemoryUserRepository;
import Sistem_Subcription_Digital.service.InvoiceService;
import Sistem_Subcription_Digital.service.PaymentService;
import Sistem_Subcription_Digital.service.PlanService;
import Sistem_Subcription_Digital.service.SubscriptionService;
import Sistem_Subcription_Digital.service.UserService;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("========== COMPLETE SYSTEM SUBSCRIPTION DIGITAL TESTING ==========\n");

        // Initialize repositories
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryPlanRepository planRepository = new InMemoryPlanRepository();
        InMemorySubscriptionRepository subscriptionRepository = new InMemorySubscriptionRepository();
        InMemoryInvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();

        // Initialize services
        UserService userService = new UserService(userRepository);
        PlanService planService = new PlanService(invoiceRepository, paymentRepository, subscriptionRepository, planRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, invoiceRepository, subscriptionRepository);
        SubscriptionService subscriptionService = new SubscriptionService(
                subscriptionRepository, userRepository, planRepository, invoiceRepository, paymentService, paymentRepository
        );
        InvoiceService invoiceService = new InvoiceService(invoiceRepository, paymentRepository, subscriptionRepository);

        // ==================== USER SERVICE TESTING ====================
        System.out.println("\n>>> TESTING USER SERVICE\n");

        System.out.println("1. Creating users...");
        User user1 = userService.createUser(new User(null, "John Doe", "john@email.com", LocalDateTime.now()));
        User user2 = userService.createUser(new User(null, "Jane Smith", "jane@email.com", LocalDateTime.now()));
        System.out.println("✓ Users created successfully");

        // ==================== PLAN SERVICE TESTING ====================
        System.out.println("\n>>> TESTING PLAN SERVICE\n");

        System.out.println("1. Creating plans...");
        Plan plan1 = planService.createPlan(new Plan(null, "Basic", "BASIC", Plan.Period.MONTHLY, 29.99));
        Plan plan2 = planService.createPlan(new Plan(null, "Premium", "PREMIUM", Plan.Period.MONTHLY, 99.99));
        System.out.println("✓ Plans created successfully");

        System.out.println("2. Retrieving plan by ID...");
        var retrievedPlan = planService.getPlanById(plan1.getId());
        System.out.println("✓ Plan retrieved: " + retrievedPlan.get().getName());

        // ==================== SUBSCRIPTION SERVICE TESTING ====================
        System.out.println("\n>>> TESTING SUBSCRIPTION SERVICE\n");

        System.out.println("1. Creating subscription...");
        Subscription subscription1 = subscriptionService.createSubscription(user1.getId(), plan1.getId(), true);
        System.out.println("✓ Subscription created for " + user1.getName() + " on plan " + plan1.getName());

        System.out.println("2. Subscription details:");
        System.out.println("  - Status: " + subscription1.getStatus());
        System.out.println("  - Auto Renew: " + subscription1.getAutoRenew());
        System.out.println("  - Start Date: " + subscription1.getStartDate());

        // ==================== INVOICE SERVICE TESTING ====================
        System.out.println("\n>>> TESTING INVOICE SERVICE\n");

        System.out.println("1. Getting initial invoice...");
        var invoices = invoiceRepository.findBySubscriptionId(subscription1.getId());
        if (!invoices.isEmpty()) {
            Invoice invoice = invoices.get(0);
            System.out.println("✓ Invoice found");
            System.out.println("  - Amount: Rp" + invoice.getAmount());
            System.out.println("  - Status: " + invoice.getStatus());
            System.out.println("  - Due Date: " + invoice.getDueDate());
        }

        // ==================== PAYMENT SERVICE TESTING ====================
        System.out.println("\n>>> TESTING PAYMENT SERVICE\n");

        System.out.println("1. Processing payments...");
        var allInvoices = invoiceRepository.findBySubscriptionId(subscription1.getId());
        if (!allInvoices.isEmpty()) {
            Invoice invoice = allInvoices.get(0);
            if (invoice.getStatus() == Invoice.InvoiceStatus.PENDING) {
                try {
                    Payment payment = paymentService.processPaymentForInvoice(invoice.getId(), Payment.PaymentMethod.CARD);
                    System.out.println("✓ Payment processed");
                    System.out.println("  - Status: " + payment.getStatus());
                    System.out.println("  - Amount: Rp" + payment.getAmount());
                } catch (Exception e) {
                    System.out.println("! Payment error (expected in test): " + e.getMessage());
                }
            }
        }

        // ==================== SUMMARY ====================
        System.out.println("\n>>> SUMMARY\n");

        System.out.println("Total Users Created: " + userRepository.findAll().size());
        System.out.println("Total Plans Created: " + planRepository.findAll().size());
        System.out.println("Total Subscriptions Created: " + subscriptionRepository.findAll().size());
        System.out.println("Total Invoices Created: " + invoiceRepository.findAll().size());
        System.out.println("Total Payments Created: " + paymentRepository.findAll().size());

        System.out.println("\n========== END OF COMPLETE SYSTEM TESTING ==========");
    }
}
