package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.repository.InMemoryInvoiceRepository;
import Sistem_Subcription_Digital.repository.InMemoryPaymentRepository;
import Sistem_Subcription_Digital.repository.InMemoryPlanRepository;
import Sistem_Subcription_Digital.repository.InMemorySubscriptionRepository;
import Sistem_Subcription_Digital.service.PlanService;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MainPlanServiceTest {
    public static void main(String[] args) {
        System.out.println("========== PLAN SERVICE TESTING ==========\n");

        InMemoryPlanRepository planRepository = new InMemoryPlanRepository();
        InMemoryInvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();
        InMemorySubscriptionRepository subscriptionRepository = new InMemorySubscriptionRepository();

        PlanService planService = new PlanService(invoiceRepository, paymentRepository, subscriptionRepository, planRepository);

        // Test 1: Create Plan - Valid
        System.out.println("TEST 1: Create Plan - Valid");
        try {
            Plan plan = new Plan(null, "Premium", "PREMIUM", Plan.Period.MONTHLY, 99.99);
            Plan createdPlan = planService.createPlan(plan);
            System.out.println("✓ Plan created successfully: " + createdPlan.getName() + " - Rp" + createdPlan.getPricePeriod());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Create Plan - Null Plan
        System.out.println("TEST 2: Create Plan - Null Plan (should fail)");
        try {
            Plan plan = planService.createPlan(null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Create Plan - Blank Name
        System.out.println("TEST 3: Create Plan - Blank Name (should fail)");
        try {
            Plan plan = new Plan(null, "   ", "CODE", Plan.Period.MONTHLY, 50.0);
            Plan createdPlan = planService.createPlan(plan);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Create Plan - Duplicate Code
        System.out.println("TEST 4: Create Plan - Duplicate Code (should fail)");
        try {
            Plan plan1 = new Plan(null, "Standard", "STD", Plan.Period.MONTHLY, 49.99);
            planService.createPlan(plan1);
            System.out.println("✓ First plan created with code: STD");

            Plan plan2 = new Plan(null, "Standard Plus", "STD", Plan.Period.MONTHLY, 59.99);
            planService.createPlan(plan2);
            System.out.println("✗ Should have thrown exception for duplicate code");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Create Plan - Invalid Price (0)
        System.out.println("TEST 5: Create Plan - Invalid Price (0) (should fail)");
        try {
            Plan plan = new Plan(null, "Free", "FREE", Plan.Period.MONTHLY, 0);
            planService.createPlan(plan);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Create Plan - Null Period
        System.out.println("TEST 6: Create Plan - Null Period (should fail)");
        try {
            Plan plan = new Plan(null, "Test Plan", "TEST", null, 100.0);
            planService.createPlan(plan);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 7: Get Plan By ID - Valid
        System.out.println("TEST 7: Get Plan By ID - Valid");
        try {
            Plan plan = new Plan(null, "Basic", "BASIC", Plan.Period.MONTHLY, 29.99);
            Plan createdPlan = planService.createPlan(plan);
            System.out.println("✓ Plan created with ID: " + createdPlan.getId());

            var retrievedPlan = planService.getPlanById(createdPlan.getId());
            if (retrievedPlan.isPresent()) {
                System.out.println("✓ Plan retrieved: " + retrievedPlan.get().getName());
            } else {
                System.out.println("✗ Plan not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 8: Get Plan By ID - Null ID
        System.out.println("TEST 8: Get Plan By ID - Null ID (should fail)");
        try {
            var plan = planService.getPlanById(null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 9: Get Plan By Code - Valid
        System.out.println("TEST 9: Get Plan By Code - Valid");
        try {
            Plan plan = new Plan(null, "Enterprise", "ENT", Plan.Period.YEARLY, 999.99);
            Plan createdPlan = planService.createPlan(plan);
            System.out.println("✓ Plan created with code: " + createdPlan.getCode());

            var retrievedPlan = planService.getPlanByCode("ENT");
            if (retrievedPlan.isPresent()) {
                System.out.println("✓ Plan found by code: " + retrievedPlan.get().getName());
            } else {
                System.out.println("✗ Plan not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 10: Update Plan - Valid Name Change
        System.out.println("TEST 10: Update Plan - Valid Name Change");
        try {
            Plan plan = new Plan(null, "Old Name", "UPDATE", Plan.Period.MONTHLY, 75.0);
            Plan createdPlan = planService.createPlan(plan);
            Long planId = createdPlan.getId();
            System.out.println("✓ Plan created: " + createdPlan.getName());

            Plan patchPlan = new Plan(null, "New Name", "UPDATE", Plan.Period.MONTHLY, 75.0);
            Plan updatedPlan = planService.updatePlan(planId, patchPlan);
            System.out.println("✓ Plan updated to: " + updatedPlan.getName());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 11: Update Plan - Cannot Change Code
        System.out.println("TEST 11: Update Plan - Cannot Change Code (should fail)");
        try {
            Plan plan = new Plan(null, "No Change Code", "NCC", Plan.Period.MONTHLY, 85.0);
            Plan createdPlan = planService.createPlan(plan);

            Plan patchPlan = new Plan(null, "Same Name", "DIFFERENT_CODE", Plan.Period.MONTHLY, 85.0);
            planService.updatePlan(createdPlan.getId(), patchPlan);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 12: Update Plan - Null ID
        System.out.println("TEST 12: Update Plan - Null ID (should fail)");
        try {
            Plan patch = new Plan(null, "Test", "TEST", Plan.Period.MONTHLY, 50.0);
            planService.updatePlan(null, patch);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 13: Update Plan - Null Patch
        System.out.println("TEST 13: Update Plan - Null Patch (should fail)");
        try {
            Plan plan = new Plan(null, "Exist", "EXIST", Plan.Period.MONTHLY, 50.0);
            Plan createdPlan = planService.createPlan(plan);
            planService.updatePlan(createdPlan.getId(), null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 14: Change Plan Price - Valid
        System.out.println("TEST 14: Change Plan Price - Valid");
        try {
            Plan plan = new Plan(null, "Price Change", "PC", Plan.Period.MONTHLY, 100.0);
            Plan createdPlan = planService.createPlan(plan);
            System.out.println("✓ Plan created with price: Rp" + createdPlan.getPricePeriod());

            LocalDate futureDate = LocalDate.now().plusDays(30);
            Plan updatedPlan = planService.changePlanPrice(createdPlan.getId(), new BigDecimal("150.0"), futureDate);
            System.out.println("✓ Plan price changed to: Rp" + updatedPlan.getPricePeriod());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 15: Change Plan Price - Same Price
        System.out.println("TEST 15: Change Plan Price - Same Price (should fail)");
        try {
            Plan plan = new Plan(null, "Same Price", "SP", Plan.Period.MONTHLY, 120.0);
            Plan createdPlan = planService.createPlan(plan);

            planService.changePlanPrice(createdPlan.getId(), new BigDecimal("120.0"), LocalDate.now().plusDays(7));
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 16: Change Plan Price - Negative Price
        System.out.println("TEST 16: Change Plan Price - Negative Price (should fail)");
        try {
            Plan plan = new Plan(null, "Neg Price", "NP", Plan.Period.MONTHLY, 80.0);
            Plan createdPlan = planService.createPlan(plan);

            planService.changePlanPrice(createdPlan.getId(), new BigDecimal("-50.0"), LocalDate.now().plusDays(7));
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 17: Change Plan Price - Past Date
        System.out.println("TEST 17: Change Plan Price - Past Date (should fail)");
        try {
            Plan plan = new Plan(null, "Past Date", "PD", Plan.Period.MONTHLY, 90.0);
            Plan createdPlan = planService.createPlan(plan);

            planService.changePlanPrice(createdPlan.getId(), new BigDecimal("110.0"), LocalDate.now().minusDays(1));
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        System.out.println("========== END OF PLAN SERVICE TESTING ==========");
    }
}