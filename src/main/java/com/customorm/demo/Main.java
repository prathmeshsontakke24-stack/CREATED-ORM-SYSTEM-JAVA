package com.customorm.demo;

import com.customorm.core.Configuration;
import com.customorm.core.EntityManager;
import com.customorm.core.EntityManagerFactory;
import com.customorm.demo.entity.Product;
import com.customorm.demo.entity.User;
import com.customorm.query.Criteria;
import com.customorm.query.Operator;

import java.math.BigDecimal;
import java.util.List;

/**
 * Main demo application to test our ORM
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== Custom ORM Framework Demo ===\n");

        // Initialize ORM
        Configuration config = new Configuration();
        EntityManagerFactory factory = new EntityManagerFactory(config);
        EntityManager em = factory.createEntityManager();

        try {

            demoUserOperations(em);
            demoProductOperations(em);
            demoQueryOperations(em);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void demoUserOperations(EntityManager em) {

        System.out.println("\n--- User Operations ---");

        // CREATE
        System.out.println("\n1. Creating new users:");

        String email1 = "alice" + System.currentTimeMillis() + "@example.com";
        String email2 = "bob" + System.currentTimeMillis() + "@example.com";

        User user1 = new User("Alice Johnson", email1, 28);
        User user2 = new User("Bob Williams", email2, 32);

        em.persist(user1);
        em.persist(user2);

        System.out.println("   Created: " + user1);
        System.out.println("   Created: " + user2);

        // READ
        System.out.println("\n2. Finding user by ID:");

        User foundUser = em.find(User.class, user1.getId());
        System.out.println("   Found: " + foundUser);

        // UPDATE
        System.out.println("\n3. Updating user:");

        foundUser.setAge(29);

        String updatedEmail = "alice.j" + System.currentTimeMillis() + "@example.com";
        foundUser.setEmail(updatedEmail);

        em.update(foundUser);

        System.out.println("   Updated: " + em.find(User.class, user1.getId()));

        // READ ALL
        System.out.println("\n4. All users in database:");

        List<User> allUsers = em.findAll(User.class);
        allUsers.forEach(user -> System.out.println("   " + user));
    }

    private static void demoProductOperations(EntityManager em) {

        System.out.println("\n--- Product Operations ---");

        // CREATE
        System.out.println("\n1. Creating new products:");

        // Use the constructor with 4 parameters (name, Double, Integer, Boolean)
        Product product1 = new Product("Smartphone", new BigDecimal("599.99").doubleValue(), 15, true);
        Product product2 = new Product("Tablet", new BigDecimal("399.99").doubleValue(), 8, true);
        Product product3 = new Product("Headphones", new BigDecimal("89.99").doubleValue(), 25, true);

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        System.out.println("   Created: " + product1);
        System.out.println("   Created: " + product2);
        System.out.println("   Created: " + product3);

        // DELETE
        System.out.println("\n2. Deleting product:");

        em.delete(product2);
        System.out.println("   Deleted: " + product2.getProductName());

        // VERIFY
        System.out.println("\n3. Remaining products:");

        List<Product> remainingProducts = em.findAll(Product.class);
        remainingProducts.forEach(p -> System.out.println("   " + p));
    }

    private static void demoQueryOperations(EntityManager em) {

        System.out.println("\n--- Query Operations with Criteria API ---");

        // Users older than 25
        System.out.println("\n1. Users older than 25:");

        Criteria criteria = new Criteria()
                .add("age", Operator.GT, 25);

        List<User> users = em.createQuery(User.class)
                .where(criteria)
                .list();

        for (User user : users) {
            System.out.println("   " + user);
        }

        // Products price > 100
        System.out.println("\n2. Products with price > 100:");

        Criteria priceCriteria = new Criteria()
                .add("price", Operator.GT, 100.0); // Use Double for comparison

        List<Product> products = em.createQuery(Product.class)
                .where(priceCriteria)
                .list();

        for (Product p : products) {
            System.out.println("   " + p);
        }
    }
}