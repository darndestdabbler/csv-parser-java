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

**Challenge:** (1) The module should exist as a separate Maven module. Four consumers share the Person type; inlining it into any one of them forces the others to depend on that module or duplicate the class — both worse than a leaf library. (2) The contract is a compile-time assertion: the class either compiles with the required fields and implements Serializable, or it does not. A unit test can additionally assert constructor, equals, hashCode, and toString behavior. The contract is concrete and provable. (3) person-model has no dependencies, so the direction cannot hide a changing decision. Nothing about the field set is expected to shift in a way that would require a different direction.

### 2. CSV Deserializer Module (`csv-deserializer`)
**Responsibility:** Read CSV files from disk and convert them into Person objects.

**Contract:**
- **Input:** Path to a folder containing CSV files
- **Output:** List of Person objects successfully deserialized from valid CSV records
- **Failure modes:** File not found, malformed CSV, type conversion errors (returns partial results with error logging)

**Dependencies:** `person-model` (at `${project.version}`)

**Rationale:** Isolates CSV parsing logic from database and UI concerns, making it testable and reusable.

**Challenge:** (1) The module should exist. CSV parsing rules are independent of both persistence and display; a separate module lets the parsing logic be unit-tested without a database and without inflating any other module's dependency footprint. (2) The contract is testable: given a folder containing valid CSV files, assert the returned List<Person> matches expected records; given a folder containing a malformed record, assert a partial list is returned and no exception escapes. The input state, output state, and observable failure mode are all concrete. (3) The only likely internal change is the CSV library (OpenCSV, Apache Commons CSV, Jackson CSV). That choice is entirely inside csv-deserializer; person-model is stable. The dependency direction does not hide the decision most likely to change.

### 3. Database Module (`csv-database`)
**Responsibility:** Persist Person objects to and query Person objects from an H2 database.

**Contract:**
- **Input:** Initialized H2 database connection, list of Person objects
- **Output:** Records persisted; query results returned as Person object lists
- **Failure modes:** Database connection failure, SQL errors (logged and propagated)

**Dependencies:** `person-model` (at `${project.version}`)

**Rationale:** Centralizes all database logic, making it possible to swap database implementations or testing strategies without affecting other modules.

**Challenge:** (1) The module should exist. Confining all JDBC/H2 code here means swapping the persistence mechanism (H2 in-memory, H2 file, another DB, a mock) requires changes in one place only. (2) The contract is testable with in-memory H2: insert N Person objects, call selectAll(), assert count and field equality. The input state (initialized connection, list of persons), output state (rows in DB; returned list on query), and failure mode (SQLException propagated after logging) are all concrete. (3) The likely change is the database implementation. That stays entirely inside csv-database; person-model never changes shape for this reason. The direction is correct.

### 4. Console Application (`csv-parser-app`)
**Responsibility:** Orchestrate the CSV reading, database persistence, and display workflow.

**Contract:**
- **Input:** Command-line argument with path to CSV folder
- **Output:** Console display of all Person records in the database
- **Failure modes:** Invalid folder path, upstream errors (exits with error message)

**Dependencies:** `person-model`, `csv-deserializer`, `csv-database` (all at `${project.version}`)

**Rationale:** Entry point that coordinates the workflow; keeps business logic separated from orchestration.

**Challenge:** (1) The module should exist as a separate Maven module. Without it, the executable JAR concerns (Main class, assembly plugin configuration) would leak into one of the library modules, making those harder to test in isolation. (2) The contract is testable end-to-end: given a temp folder containing valid CSV files, invoke Main, capture stdout, assert one header line and one data line per record in the expected format. The input state, output, and failure behavior (exit with error message on bad path) are concrete. (3) csv-parser-app depends on all three modules; no module depends on it. This is the correct direction. The one wiring decision (direct calls vs. a DI framework) stays inside csv-parser-app and does not affect any other module.

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

## Resolved Deferred Decisions

**(a) CSV Column Mapping**

Person fields and their corresponding CSV header names (case-insensitive match):

| Java field | Type   | CSV header |
|------------|--------|------------|
| `name`     | String | `name`     |
| `email`    | String | `email`    |
| `age`      | int    | `age`      |

A record missing any of these headers, or whose `age` value is not a valid integer, is treated as malformed: it is skipped and an error is logged at WARN level. Valid records in the same file are still returned.

**(b) H2 Database Location**

Tests use an in-memory H2 database (`jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`). The application uses a file-based H2 database in the current working directory (`jdbc:h2:./csv-parser`), producing `csv-parser.mv.db` on first run.

**(c) Console Output Format**

One header line followed by one data line per Person, fields separated by ` | `:

```
name | email | age
Alice | alice@example.com | 30
Bob | bob@example.com | 25
```

Records are printed in the order returned by the database SELECT (insertion order).
