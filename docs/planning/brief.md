# Brief: CSV Parser Java Dabbler Learning Exercise

## Purpose

This solution demonstrates the Dabbler AI Orchestration platform through a practical learning exercise. The primary goal is to understand how Dabbler manages and orchestrates AI-driven development sessions and project structure.

## Who Uses It

This is a learning/sample application. It is not intended for production use.

## What It Must Do

The console application must:
1. Read CSV files from a designated folder
2. Deserialize CSV data into Person objects
3. Persist Person objects to an H2 database
4. Query and display all records from the database in the console

## Scope

- **In scope:** CSV reading, deserialization to objects, H2 database persistence, console display
- **Out of scope:** 
  - Production deployment architecture
  - Release/packaging for distribution
  - REST API or web interface
  - Advanced error recovery or validation beyond basic parsing
  - Performance optimization for large datasets

## Success Criteria

1. A console application runs and successfully reads CSV files from a folder
2. CSV data is correctly deserialized into Person objects
3. Person objects are persisted to an H2 database
4. All records in the database can be queried and displayed in the console
5. The solution is organized into four distinct Maven modules as specified
6. The Dabbler orchestration workflow completes successfully with all sessions
