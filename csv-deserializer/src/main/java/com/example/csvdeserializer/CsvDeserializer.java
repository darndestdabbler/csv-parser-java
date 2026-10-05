package com.example.csvdeserializer;

import com.example.personmodel.Person;
import com.opencsv.CSVReaderHeaderAware;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CsvDeserializer {

    private static final Logger LOG = Logger.getLogger(CsvDeserializer.class.getName());

    public List<Person> deserialize(Path folder) throws IOException {
        List<Person> result = new ArrayList<>();
        try (Stream<Path> files = Files.list(folder)) {
            for (Path file : (Iterable<Path>) files.filter(p -> p.toString().toLowerCase().endsWith(".csv"))::iterator) {
                result.addAll(readFile(file));
            }
        }
        return result;
    }

    private List<Person> readFile(Path file) throws IOException {
        List<Person> result = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(file);
             CSVReaderHeaderAware csv = new CSVReaderHeaderAware(reader)) {
            Map<String, String> row;
            while ((row = csv.readMap()) != null) {
                Person person = toPerson(row);
                if (person != null) {
                    result.add(person);
                }
            }
        } catch (com.opencsv.exceptions.CsvValidationException e) {
            LOG.warning("Skipping file " + file + ": " + e.getMessage());
        }
        return result;
    }

    private Person toPerson(Map<String, String> row) {
        String name = findValue(row, "name");
        String email = findValue(row, "email");
        String ageStr = findValue(row, "age");

        if (name == null || email == null || ageStr == null) {
            LOG.warning("Skipping record with missing column: " + row);
            return null;
        }

        try {
            int age = Integer.parseInt(ageStr.trim());
            return new Person(name, email, age);
        } catch (NumberFormatException e) {
            LOG.warning("Skipping record with non-integer age '" + ageStr + "': " + row);
            return null;
        }
    }

    private String findValue(Map<String, String> row, String key) {
        for (Map.Entry<String, String> entry : row.entrySet()) {
            if (entry.getKey().trim().equalsIgnoreCase(key)) {
                String value = entry.getValue();
                return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
            }
        }
        return null;
    }
}
