package com.example.csvdeserializer;

import com.example.personmodel.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvDeserializerTest {

    private final CsvDeserializer deserializer = new CsvDeserializer();

    @Test
    void wellFormedFileReturnsExpectedPersons(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("people.csv"),
                "name,email,age\n" +
                "Alice,alice@example.com,30\n" +
                "Bob,bob@example.com,25\n");

        List<Person> result = deserializer.deserialize(dir);

        assertEquals(List.of(
                new Person("Alice", "alice@example.com", 30),
                new Person("Bob", "bob@example.com", 25)
        ), result);
    }

    @Test
    void fileWithMalformedRecordReturnsPartialList(@TempDir Path dir) throws IOException {
        Files.writeString(dir.resolve("mixed.csv"),
                "name,email,age\n" +
                "Alice,alice@example.com,30\n" +
                "Bad,bad@example.com,notanumber\n" +
                "Carol,carol@example.com,40\n");

        List<Person> result = deserializer.deserialize(dir);

        assertEquals(List.of(
                new Person("Alice", "alice@example.com", 30),
                new Person("Carol", "carol@example.com", 40)
        ), result);
    }
}
