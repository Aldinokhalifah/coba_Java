package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InMemoryInvoiceRepository;
import Sistem_Subcription_Digital.repository.InMemoryPaymentRepository;
import Sistem_Subcription_Digital.repository.InMemoryPlanRepository;
import Sistem_Subcription_Digital.repository.InMemorySubscriptionRepository;
import Sistem_Subcription_Digital.repository.InMemoryUserRepository;
import Sistem_Subcription_Digital.service.PaymentService;
import Sistem_Subcription_Digital.service.SubscriptionService;
import java.time.LocalDateTime;

public class MainSubscriptionServiceTest {
    public static void main(String[] args) {
        System.out.println("========== SUBSCRIPTION SERVICE TESTING ==========\n");

        InMemorySubscriptionRepository subscriptionRepository = new InMemorySubscriptionRepository();
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryPlanRepository planRepository = new InMemoryPlanRepository();
        InMemoryInvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();

        PaymentService paymentService = new PaymentService(paymentRepository, invoiceRepository, subscriptionRepository);
        SubscriptionService subscriptionService = new SubscriptionService(
                subscriptionRepository, userRepository, planRepository, invoiceRepository, paymentService, paymentRepository
        );

        // Setup data
        User user = new User(null, "Alice Johnson", "alice@email.com", LocalDateTime.now());
        userRepository.save(user);

        Plan plan = new Plan(null, "Premium Plus", "PREM", Plan.Period.MONTHLY, 199.99);
        planRepository.save(plan);

        // Test 1: Create Subscription - Valid
        System.out.println("TEST 1: Create Subscription - Valid");
        try {
            Subscription subscription = subscriptionService.createSubscription(user.getId(), plan.getId(), true);
            System.out.println("✓ Subscription created successfully");
            System.out.println("  - User: " + subscription.getUser().getName());
            System.out.println("  - Plan: " + subscription.getPlan().getName());
            System.out.println("  - Status: " + subscription.getStatus());
            System.out.println("  - Auto Renew: " + subscription.getAutoRenew());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Create Subscription - Null User ID
        System.out.println("TEST 2: Create Subscription - Null User ID (should fail)");
        try {
            subscriptionService.createSubscription(null, plan.getId(), true);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Create Subscription - Null Plan ID
        System.out.println("TEST 3: Create Subscription - Null Plan ID (should fail)");
        try {
            subscriptionService.createSubscription(user.getId(), null, true);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Create Subscription - User Not Found
        System.out.println("TEST 4: Create Subscription - User Not Found (should fail)");
        try {
            subscriptionService.createSubscription(999L, plan.getId(), true);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Create Subscription - Plan Not Found
        System.out.println("TEST 5: Create Subscription - Plan Not Found (should fail)");
        try {
            subscriptionService.createSubscription(user.getId(), 999L, true);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Create Subscription - Duplicate Active Subscription
        System.out.println("TEST 6: Create Subscription - Duplicate Active Subscription (should fail)");
        try {
            User user2 = new User(null, "Bob Smith", "bob@email.com", LocalDateTime.now());
            userRepository.save(user2);

            Subscription sub1 = subscriptionService.createSubscription(user2.getId(), plan.getId(), true);
            System.out.println("✓ First subscription created");

            Subscription sub2 = subscriptionService.createSubscription(user2.getId(), plan.getId(), false);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 7: Cancel Subscription - Valid
        System.out.println("TEST 7: Cancel Subscription - Valid");
        try {
            User user3 = new User(null, "Carol White", "carol@email.com", LocalDateTime.now());
            userRepository.save(user3);

            Subscription subscription = subscriptionService.createSubscription(user3.getId(), plan.getId(), true);
            System.out.println("✓ Subscription created with status: " + subscription.getStatus());

            subscriptionService.cancelSubscription(subscription.getId());
            System.out.println("✓ Subscription cancelled successfully");

            Subscription cancelledSub = subscriptionRepository.findById(subscription.getId()).orElse(null);
            if (cancelledSub != null) {
                System.out.println("  - New status: " + cancelledSub.getStatus());
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 8: Cancel Subscription - Invalid ID
        System.out.println("TEST 8: Cancel Subscription - Invalid ID (should fail)");
        try {
            subscriptionService.cancelSubscription(999L);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 9: Cancel Subscription - Already Cancelled
        System.out.println("TEST 9: Cancel Subscription - Already Cancelled (should fail)");
        try {
            User user4 = new User(null, "David Brown", "david@email.com", LocalDateTime.now());
            userRepository.save(user4);

            Subscription subscription = subscriptionService.createSubscription(user4.getId(), plan.getId(), true);
            subscriptionService.cancelSubscription(subscription.getId());
            System.out.println("✓ First cancellation successful");

            subscriptionService.cancelSubscription(subscription.getId());
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 10: Suspend Subscription
        System.out.println("TEST 10: Suspend Subscription");
        try {
            User user5 = new User(null, "Eve Davis", "eve@email.com", LocalDateTime.now());
            userRepository.save(user5);

            Subscription subscription = subscriptionService.createSubscription(user5.getId(), plan.getId(), true);
            System.out.println("✓ Subscription created with status: " + subscription.getStatus());

            subscription.suspend();
            subscriptionRepository.save(subscription);
            System.out.println("✓ Subscription suspended");

            Subscription suspendedSub = subscriptionRepository.findById(subscription.getId()).orElse(null);
            if (suspendedSub != null) {
                System.out.println("  - New status: " + suspendedSub.getStatus());
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 11: Get Subscription By ID
        System.out.println("TEST 11: Get Subscription By ID");
        try {
            User user6 = new User(null, "Frank Miller", "frank@email.com", LocalDateTime.now());
            userRepository.save(user6);

            Subscription subscription = subscriptionService.createSubscription(user6.getId(), plan.getId(), true);
            System.out.println("✓ Subscription created with ID: " + subscription.getId());

            var retrievedSub = subscriptionRepository.findById(subscription.getId());
            if (retrievedSub.isPresent()) {
                System.out.println("✓ Subscription retrieved: User=" + retrievedSub.get().getUser().getName());
            } else {
                System.out.println("✗ Subscription not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 12: Find Active Subscription By User and Plan
        System.out.println("TEST 12: Find Active Subscription By User and Plan");
        try {
            User user7 = new User(null, "Grace Lee", "grace@email.com", LocalDateTime.now());
            userRepository.save(user7);

            Subscription subscription = subscriptionService.createSubscription(user7.getId(), plan.getId(), true);
            System.out.println("✓ Subscription created");

            var activeSub = subscriptionRepository.findActiveByUserAndPlan(user7.getId(), plan.getId());
            if (activeSub.isPresent()) {
                System.out.println("✓ Active subscription found: " + activeSub.get().getStatus());
            } else {
                System.out.println("✗ Active subscription not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 13: Create Subscription with Auto Renew False
        System.out.println("TEST 13: Create Subscription with Auto Renew False");
        try {
            User user8 = new User(null, "Henry Ford", "henry@email.com", LocalDateTime.now());
            userRepository.save(user8);

            Subscription subscription = subscriptionService.createSubscription(user8.getId(), plan.getId(), false);
            System.out.println("✓ Subscription created with Auto Renew: " + subscription.getAutoRenew());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 14: Get Service Dependencies
        System.out.println("TEST 14: Get Service Dependencies");
        try {
            var subRepo = subscriptionService.getSubscriptionRepository();
            var userRepo = subscriptionService.getUserRepository();
            var planRepo = subscriptionService.getPlanRepository();
            var invoiceRepo = subscriptionService.getInvoiceRepository();

            System.out.println("✓ All service dependencies retrieved successfully");
            System.out.println("  - Subscription Repository: OK");
            System.out.println("  - User Repository: OK");
            System.out.println("  - Plan Repository: OK");
            System.out.println("  - Invoice Repository: OK");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("========== END OF SUBSCRIPTION SERVICE TESTING ==========");
    }
}