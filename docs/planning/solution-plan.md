# Solution Plan: CSV Parser Java Dabbler Learning Exercise

## Objective

Build a Java Maven console application that demonstrates the Dabbler AI Orchestration workflow by implementing a CSV-to-database pipeline with clear module separation. The goal is to learn Dabbler's session-based development model while creating a functioning sample application.

## Production Split

This is a learning/sample application with no production deployment requirement. The application is a single console process that performs all operations sequentially: read CSV → deserialize → persist → display.

## Handoff Artifacts

**Build command:** `mvn package -Dhandoff.dir={output}`

**Artifact:** Executable JAR file produced by the Maven assembly plugin, runnable with:
```
java -jar csv-parser-1.0.0-jar-with-dependencies.jar <csv-folder-path>
```

## Modules

### 1. Person Model Library (`person-model`)
**Responsibility:** Define the Person data model and any related domain objects.

**Contract:**
- **Input:** None (library module)
- **Output:** Serializable Person class with fields matching CSV structure (name, email, age, etc.)
- **Failure modes:** Invalid class definitions caught at compile time

**Dependencies:** None (leaf module)

**Rationale:** Separates the domain model from implementation details, allowing reuse across modules and clarity in data contracts.

### 2. CSV Deserializer Module (`csv-deserializer`)
**Responsibility:** Read CSV files from disk and convert them into Person objects.

**Contract:**
- **Input:** Path to a folder containing CSV files
- **Output:** List of Person objects successfully deserialized from valid CSV records
- **Failure modes:** File not found, malformed CSV, type conversion errors (returns partial results with error logging)

**Dependencies:** `person-model` (at `${project.version}`)

**Rationale:** Isolates CSV parsing logic from database and UI concerns, making it testable and reusable.

### 3. Database Module (`csv-database`)
**Responsibility:** Persist Person objects to and query Person objects from an H2 database.

**Contract:**
- **Input:** Initialized H2 database connection, list of Person objects
- **Output:** Records persisted; query results returned as Person object lists
- **Failure modes:** Database connection failure, SQL errors (logged and propagated)

**Dependencies:** `person-model` (at `${project.version}`)

**Rationale:** Centralizes all database logic, making it possible to swap database implementations or testing strategies without affecting other modules.

### 4. Console Application (`csv-parser-app`)
**Responsibility:** Orchestrate the CSV reading, database persistence, and display workflow.

**Contract:**
- **Input:** Command-line argument with path to CSV folder
- **Output:** Console display of all Person records in the database
- **Failure modes:** Invalid folder path, upstream errors (exits with error message)

**Dependencies:** `person-model`, `csv-deserializer`, `csv-database` (all at `${project.version}`)

**Rationale:** Entry point that coordinates the workflow; keeps business logic separated from orchestration.

## Dependency Graph

```
csv-parser-app (console app)
  ├─ person-model
  ├─ csv-deserializer
  │   └─ person-model
  └─ csv-database
      └─ person-model
```

Only `csv-parser-app` depends on multiple modules; each module is independently testable.

## Phases and Feature Areas

### Phase 1: Skeleton (Session 2)
- Create Maven parent POM with four modules listed
- Create individual module POMs for all four modules
- Set up build configuration and test suite in `dabbler.yaml`
- Add architecture test to verify dependency rules

### Phase 2: Core Domain (Session 3)
- Define Person model class with appropriate fields and serialization
- Create unit tests for Person class

### Phase 3: CSV Deserialization (Session 4)
- Implement CSV file reading from folder
- Implement CSV-to-Person deserialization
- Handle basic error cases
- Create integration tests

### Phase 4: Database Layer (Session 5)
- Set up H2 database with Maven dependency
- Create database schema and initialization
- Implement persistence (INSERT) and querying (SELECT) operations
- Create database integration tests

### Phase 5: Console Application (Session 6)
- Implement console application entry point
- Wire up CSV reading → persistence → querying → display flow
- Test end-to-end functionality
- Create integration tests

### Phase 6: Polish and Release Verification (Session 7)
- Ensure all modules build and integrate correctly
- Verify Dabbler workflow completion
- Document the final solution
- Final test run and validation

## Deferred Decisions

- **CSV Column Mapping:** Will be determined in Session 4 when CSV deserializer is implemented based on typical CSV structure
- **H2 Database File Location:** Will be determined in Session 5 based on application requirements (in-memory vs. file-based)
- **Console Output Format:** Will be refined in Session 6 based on readability and usability testing
