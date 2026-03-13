
---

# Custom ORM Framework in Java

## Project Overview

The **Custom ORM (Object Relational Mapping) Framework** is a Java-based framework developed to simplify the interaction between Java applications and relational databases. The framework maps Java objects to database tables and automates database operations such as **creating, reading, updating, and deleting records (CRUD operations)**.

The main purpose of this project is to demonstrate how popular ORM frameworks like **Hibernate ORM** and **Java Persistence API** internally work by implementing a simplified version using **Java Reflection, Annotations, and JDBC**.

This project helps developers understand how Java objects can be automatically mapped to relational database tables, reducing the need to write repetitive SQL queries.

---

# Objectives of the Project

The key objectives of this project are:

* To build a simplified version of an ORM framework similar to Hibernate.
* To understand how object-relational mapping works internally.
* To implement annotation-based entity mapping.
* To reduce manual SQL operations in Java applications.
* To demonstrate the use of **Java Reflection API** and **custom annotations**.

---

# Key Features

## Entity Mapping

Java classes can be mapped directly to database tables using custom annotations such as:

* `@Entity`
* `@Table`
* `@Column`
* `@Id`
* `@Ignore`

This allows developers to define database structure directly in Java classes.

---

## Automatic Table Mapping

The framework automatically maps Java class fields to database table columns by reading annotation metadata using **Java Reflection**.

Example:

```java
@Entity
@Table(name="users")
public class User {

    @Id
    private int id;

    @Column(name="username")
    private String name;

}
```

---

## CRUD Operations

The framework supports basic database operations such as:

* Insert new records
* Retrieve records from database
* Update existing records
* Delete records

These operations are handled by the **EntityManager class**.

---

## Query Builder

The project includes a custom **Query Builder** that allows building SQL queries dynamically without writing SQL manually.

Example features:

* Dynamic filtering
* Conditional queries
* Operator-based comparisons

---

## Criteria API

A custom **Criteria API** is implemented to create flexible database queries using Java objects rather than SQL statements.

This makes query construction more structured and readable.

---

# Technologies Used

### Programming Language

* Java

### Database Connectivity

* JDBC

### Build Tool

* Maven

### Database

* MySQL

### Development Environment

* IntelliJ IDEA

### Version Control

* Git and GitHub

---

# System Architecture

The framework follows a **modular architecture** with several components.

## Annotation Layer

Contains custom annotations used to map Java classes to database tables.

Examples:

* `@Entity`
* `@Table`
* `@Column`
* `@Id`

These annotations define how Java objects should be stored in the database.

---

## Core Framework Layer

The core package contains the main functionality of the ORM framework.

Main components include:

### Configuration

Handles database configuration and connection setup.

### EntityManager

Responsible for performing CRUD operations on entities.

### EntityManagerFactory

Creates instances of EntityManager and manages framework configuration.

### QueryBuilder

Builds SQL queries dynamically based on entity metadata.

---

## Mapping Layer

Responsible for mapping Java class fields to database table columns.

Important classes include:

* EntityMapper
* ColumnMapper

These classes use reflection to inspect Java classes and generate database mappings.

---

## Query Layer

Implements a flexible query system.

Key classes include:

* Criteria
* CriteriaBuilder
* Operator
* Query

This layer allows complex database queries to be built programmatically.

---

# Project Structure

```text
CustomORM
│
├── annotations
│   ├── Entity.java
│   ├── Table.java
│   ├── Column.java
│   ├── Id.java
│   └── Ignore.java
│
├── core
│   ├── Configuration.java
│   ├── EntityManager.java
│   ├── EntityManagerFactory.java
│   └── QueryBuilder.java
│
├── mapping
│   ├── EntityMapper.java
│   └── ColumnMapper.java
│
├── query
│   ├── Criteria.java
│   ├── CriteriaBuilder.java
│   ├── Operator.java
│   └── Query.java
│
├── exception
│   ├── ORMException.java
│   └── EntityNotFoundException.java
│
├── demo
│   ├── Main.java
│   └── entity
│       ├── User.java
│       └── Product.java
│
└── resources
    └── application.properties
```

---

# How to Run the Project

### Step 1 – Clone the Repository

```bash
git clone https://github.com/prathmeshsontakke24-stack/CREATED-ORM-SYSTEM-JAVA.git
```

---

### Step 2 – Open in IntelliJ IDEA

Import the project as a **Maven project**.

---

### Step 3 – Configure Database

Edit the database configuration in:

```
application.properties
```

Example:

```
db.url=jdbc:mysql://localhost:3306/customorm
db.username=root
db.password=password
```

---

### Step 4 – Create Database Tables

Create the required database tables in MySQL.

---

### Step 5 – Run the Demo

Run the file:

```
Main.java
```

This demonstrates entity mapping and database operations using the custom ORM framework.

---

# Advantages of the Framework

* Reduces repetitive SQL code
* Improves code maintainability
* Simplifies database operations
* Demonstrates internal ORM framework concepts
* Useful for learning database abstraction layers

---

# Future Improvements

Possible enhancements include:

* Lazy loading of entities
* Relationship mapping (OneToMany, ManyToOne)
* Transaction management
* Caching support
* Advanced query optimization
* Integration with Spring Boot

---

# Learning Outcomes

This project helped in understanding:

* Object Relational Mapping concepts
* Java Reflection API
* Annotation processing
* Dynamic SQL generation
* Framework architecture design
* Database abstraction techniques

---

# Author

**Prathamesh Sontakke**

Java Developer | Information Technology

---
+---------------------------+
|       Java Application    |
|  (User, Product Entities) |
+------------+--------------+
             |
             |  Uses
             v
+---------------------------+
|        Annotations        |
| @Entity  @Table  @Column  |
| @Id      @Ignore          |
+------------+--------------+
             |
             |  Reflection
             v
+---------------------------+
|        EntityMapper       |
|      ColumnMapper         |
|  (Maps Java Objects to    |
|   Database Tables)        |
+------------+--------------+
             |
             |  Query Generation
             v
+---------------------------+
|        QueryBuilder       |
|        Criteria API       |
| (Criteria, Operator,Query)|
+------------+--------------+
             |
             |  JDBC
             v
+---------------------------+
|       EntityManager       |
|  (Insert, Update, Delete, |
|        Find, Query)       |
+------------+--------------+
             |
             v
+---------------------------+
|         Database          |
|          MySQL            |
+---------------------------+

