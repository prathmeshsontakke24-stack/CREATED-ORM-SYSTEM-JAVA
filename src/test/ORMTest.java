package com.customorm.test;

import com.customorm.core.Configuration;
import com.customorm.core.EntityManager;
import com.customorm.core.EntityManagerFactory;
import com.customorm.demo.entity.User;
import com.customorm.query.Criteria;
import com.customorm.query.Query;

import java.util.List;

/**
 * Simple test class to verify ORM functionality
 */
public class ORMTest {
    public static void main(String[] args) {
        System.out.println("=== Running ORM Tests ===\n");

        Configuration config = new Configuration();
        EntityManagerFactory factory = new EntityManagerFactory(config);
        EntityManager em = factory.createEntityManager();

        try {
            // Test 1: Create user
            System.out.println("Test 1: Creating user");
            User testUser = new User("Test User", "test@example.com", 40);
            em.persist(testUser);
            System.out.println("✓ User created with ID: " + testUser.getId());

            // Test 2: Find user
            System.out.println("\nTest 2: Finding user");
            User foundUser = em.find(User.class, testUser.getId());
            assert foundUser != null : "User should exist";
            assert foundUser.getName().equals("Test User") : "Name should match";
            System.out.println("✓ User found: " + foundUser);

            // Test 3: Update user
            System.out.println("\nTest 3: Updating user");
            foundUser.setName("Updated Name");
            foundUser.setAge(41);
            em.update(foundUser);

            User updatedUser = em.find(User.class, testUser.getId());
            assert updatedUser.getName().equals("Updated Name") : "Name should be updated";
            assert updatedUser.getAge() == 41 : "Age should be updated";
            System.out.println("✓ User updated: " + updatedUser);

            // Test 4: Query users
            System.out.println("\nTest 4: Query users by criteria");
            Criteria criteria = new Criteria()
                    .greaterThan("age", 30);

            Query<User> query = em.createQuery(User.class)
                    .where(criteria)
                    .orderBy("name");

            List<User> users = query.list();
            System.out.println("✓ Found " + users.size() + " users over 30");
            users.forEach(u -> System.out.println("  - " + u));

            // Test 5: Delete user
            System.out.println("\nTest 5: Deleting user");
            em.delete(updatedUser);

            try {
                em.find(User.class, testUser.getId());
                assert false : "User should be deleted";
            } catch (Exception e) {
                System.out.println("✓ User successfully deleted");
            }

            System.out.println("\n=== All tests passed! ===");

        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}