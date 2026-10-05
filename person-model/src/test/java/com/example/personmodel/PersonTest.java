package com.example.personmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void constructorAndGetters() {
        Person p = new Person("Alice", "alice@example.com", 30);
        assertEquals("Alice", p.getName());
        assertEquals("alice@example.com", p.getEmail());
        assertEquals(30, p.getAge());
    }

    @Test
    void equalPersonsAreEqual() {
        Person a = new Person("Alice", "alice@example.com", 30);
        Person b = new Person("Alice", "alice@example.com", 30);
        assertEquals(a, b);
    }

    @Test
    void differentPersonsAreNotEqual() {
        Person a = new Person("Alice", "alice@example.com", 30);
        Person b = new Person("Bob", "bob@example.com", 25);
        assertNotEquals(a, b);
    }

    @Test
    void equalPersonsHaveSameHashCode() {
        Person a = new Person("Alice", "alice@example.com", 30);
        Person b = new Person("Alice", "alice@example.com", 30);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsReflexive() {
        Person p = new Person("Alice", "alice@example.com", 30);
        assertEquals(p, p);
    }

    @Test
    void equalsSymmetric() {
        Person a = new Person("Alice", "alice@example.com", 30);
        Person b = new Person("Alice", "alice@example.com", 30);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void notEqualToNull() {
        Person p = new Person("Alice", "alice@example.com", 30);
        assertNotEquals(null, p);
    }

    @Test
    void toStringFormat() {
        Person p = new Person("Alice", "alice@example.com", 30);
        assertEquals("Person{name='Alice', email='alice@example.com', age=30}", p.toString());
    }
}
