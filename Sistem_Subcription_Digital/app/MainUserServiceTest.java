package Sistem_Subcription_Digital.app;

import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InMemoryUserRepository;
import Sistem_Subcription_Digital.service.UserService;
import java.time.LocalDateTime;

public class MainUserServiceTest {
    public static void main(String[] args) {
        System.out.println("========== USER SERVICE TESTING ==========\n");

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        // Test 1: Create User - Valid
        System.out.println("TEST 1: Create User - Valid");
        try {
            User user = userService.createUser(
                    new User(null, "John Doe", "john@email.com", LocalDateTime.now())
            );
            System.out.println("✓ User created successfully: " + user.getName() + " - " + user.getEmail());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 2: Create User - Null Name
        System.out.println("TEST 2: Create User - Null Name (should fail)");
        try {
            User user = userService.createUser(
                    new User(null, null, "test@email.com", LocalDateTime.now())
            );
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 3: Create User - Blank Name
        System.out.println("TEST 3: Create User - Blank Name (should fail)");
        try {
            User user = userService.createUser(
                    new User(null, "   ", "test@email.com", LocalDateTime.now())
            );
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Create User - Invalid Email
        System.out.println("TEST 4: Create User - Invalid Email (should fail)");
        try {
            User user = userService.createUser(
                    new User(null, "Jane Doe", "invalid-email", LocalDateTime.now())
            );
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 5: Create User - Duplicate Email
        System.out.println("TEST 5: Create User - Duplicate Email (should fail)");
        try {
            User user1 = userService.createUser(
                    new User(null, "User One", "duplicate@email.com", LocalDateTime.now())
            );
            System.out.println("✓ First user created: " + user1.getName());

            User user2 = userService.createUser(
                    new User(null, "User Two", "duplicate@email.com", LocalDateTime.now())
            );
            System.out.println("✗ Should have thrown an exception for duplicate email");
        } catch (IllegalStateException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 6: Get User By ID - Valid
        System.out.println("TEST 6: Get User By ID - Valid");
        try {
            User user = userService.createUser(
                    new User(null, "Bob Smith", "bob@email.com", LocalDateTime.now())
            );
            System.out.println("✓ User created with ID: " + user.getId());

            User retrievedUser = userService.getUserById(user.getId()).orElse(null);
            if (retrievedUser != null) {
                System.out.println("✓ User retrieved: " + retrievedUser.getName());
            } else {
                System.out.println("✗ User not found");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 7: Get User By ID - Not Found
        System.out.println("TEST 7: Get User By ID - Not Found");
        try {
            var result = userService.getUserById(999L);
            if (result.isEmpty()) {
                System.out.println("✓ User not found as expected (empty optional)");
            } else {
                System.out.println("✗ User should not exist");
            }
        } catch (Exception e) {
            System.out.println("✓ Exception caught: " + e.getMessage());
        }
        System.out.println();

        // Test 8: Get User By ID - Null ID
        System.out.println("TEST 8: Get User By ID - Null ID (should fail)");
        try {
            var result = userService.getUserById(null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 9: Get User By Email - Valid
        System.out.println("TEST 9: Get User By Email - Valid");
        try {
            User user = userService.createUser(
                    new User(null, "Alice Cooper", "alice@email.com", LocalDateTime.now())
            );
            System.out.println("✓ User created: " + user.getEmail());

            User retrievedUser = userService.getUserByEmail("alice@email.com").orElse(null);
            if (retrievedUser != null) {
                System.out.println("✓ User found by email: " + retrievedUser.getName());
            } else {
                System.out.println("✗ User not found by email");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 10: Get User By Email - Not Found
        System.out.println("TEST 10: Get User By Email - Not Found");
        try {
            var result = userService.getUserByEmail("nonexistent@email.com");
            if (result.isEmpty()) {
                System.out.println("✓ User not found as expected");
            } else {
                System.out.println("✗ User should not exist");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        // Test 11: Get User By Email - Null Email
        System.out.println("TEST 11: Get User By Email - Null Email (should fail)");
        try {
            var result = userService.getUserByEmail(null);
            System.out.println("✗ Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception caught as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Wrong exception: " + e.getMessage());
        }
        System.out.println();

        // Test 12: Get All Users
        System.out.println("TEST 12: Get All Users");
        try {
            userService.createUser(new User(null, "User A", "usera@email.com", LocalDateTime.now()));
            userService.createUser(new User(null, "User B", "userb@email.com", LocalDateTime.now()));
            userService.createUser(new User(null, "User C", "userc@email.com", LocalDateTime.now()));

            var allUsers = userService.getAllUsers();
            System.out.println("✓ Total users in repository: " + allUsers.size());
            allUsers.forEach(u -> System.out.println("  - " + u.getName() + " (" + u.getEmail() + ")"));
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("========== END OF USER SERVICE TESTING ==========");
    }
}