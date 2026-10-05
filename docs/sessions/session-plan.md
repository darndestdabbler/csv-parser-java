# Session plan

> **Purpose:** the numbered sessions this repository runs, in order. The
> first two set the project up; everything after them is the work.
> **Workflow:** Full

---

## Sessions

### Session 1: Author or import the solution plan

1. Register.
2. The brief is the prompt this session was started with, or
   `docs/planning/brief.md`: what the solution is — its purpose, who uses
   it, what it must do and what is out of scope, what success looks like,
   and whether a plan or notes already exist. With neither, ask the person
   who started the session for it and write their answer to that path
   before any work. The plan's substance is theirs: do not search
   neighbouring directories for one, and do not draft one from the folder
   name. Before any module is proposed, ask them how production is split:
   what runs separately in production, and which part may talk to the
   database? Offer the default -- an application tier that calls an API
   tier, and only the API tier talks to the database. Then create — or
   import — `docs/planning/solution-plan.md`: the
   objective a reader can act on; the *Production split* they answered;
   the *Handoff artifacts*, what each separately running part is handed
   over as, with the standard command for its form -- for .NET an IIS site
   package (`dotnet publish`) or a Windows Service (a Worker Service
   published for a service host), for Java a runnable jar or a war (`mvn
   package` writing into `{output}`, which `packaging.pack` requires --
   say through a property the POM's jar or war plugin takes as its
   output directory, `-Dhandoff.dir={output}`) or an image (Spring
   Boot's `build-image`), for the database a
   SQL migration script (`dotnet ef migrations script --idempotent`); the
   real form is produced later, and a tutorial produces the easiest one to
   test; the modules, with what each is
   responsible for, the contract each exposes (what must be true going
   in, what is guaranteed coming out, how it fails), the dependency
   direction, the reason for each cut and the cuts deferred — one module
   is a fine answer, and most small solutions are one; and the phases or
   feature areas with their key deliverables, each scoped to a handful of
   focused AI sessions. A repository that already has
   `docs/planning/project-plan.md` amends that file instead.
3. Where the plan has several modules, say in it how each is a project:
   a `.csproj` listed in the root solution file for .NET, or a Maven module
   listed in the parent `pom.xml`, each reaching a sibling by project
   reference -- a `<ProjectReference>`, or a dependency at
   `${project.version}`. The first numbered session, the skeleton, writes
   the projects.
4. Cross-provider verification.
5. Full test suite, recorded as the run of record.
6. Close-out.

This session writes the plan and nothing else: it creates, edits or
deletes no code, project, build file, test, `dabbler.yaml` suite or
packaging.

**Creates:** `docs/planning/solution-plan.md`. A later revision is just
another plan session that amends the same file.

### Session 2: Challenge the plan, then break it into numbered sessions

1. Register.
2. Read `docs/planning/solution-plan.md` (or `docs/planning/project-plan.md`
   where that is the file this repository keeps its plan in) and challenge
   its cuts: for each
   module, whether it should exist, whether its contract is a promise
   something can prove, and whether the dependency direction hides the
   decisions most likely to change; decide each deferred cut or say why it
   stays deferred. Record the answers in the plan's rationale.
3. Break the plan into numbered sessions appended to this file. Each
   session is a focused unit of
   work one AI coding session can complete: one
   `### Session <N>: <title>` heading, and its steps as a top-level
   ordered list. Step 1 registers the session; the last steps are
   cross-provider verification, the complete suite once against the
   verified tree, and close-out; the middle steps are the work. Never
   write a step that says "run the tests" without saying which run it
   means. Order sessions so earlier ones unblock later ones — a module
   before the modules that depend on it — and keep at most ~3 work steps
   per session.

   The first numbered session is the skeleton. For .NET it writes the
   solution file and every project the plan names, with their
   references; for Maven or Gradle, the parent build and every module.
   Either way it declares the test suite in `dabbler.yaml` under
   `testing.suites`, even before any tests exist, with `expensive: true`
   where its run is the run of record and a hang limit in its command --
   `dotnet test --blame-hang-timeout 5m`, or Surefire's
   `forkedProcessTimeoutInSeconds` -- so every later run inherits it,
   and adds the architecture test the plan requires -- where only the
   API tier may reach the database, that rule. Later sessions fill the
   modules in and never re-create what the skeleton made.

   Packaging and releases are sessions of their own. Where the plan
   names *Handoff artifacts*, plan one packaging session -- early, with
   project files only, or later -- that writes the `packaging:` block in
   `dabbler.yaml` and does nothing else. A release is a session with no
   steps, headed `### Session <N>: Release <version> (release:
   <version>)`; propose its version -- patch, minor or major by what the
   sessions it releases change -- for the person to approve with the plan.
4. Cross-provider verification.
5. Full test suite, recorded as the run of record.
6. Close-out.

This session writes the plan and nothing else: it creates, edits or
deletes no code, project, build file, test, `dabbler.yaml` suite or
packaging.

**Creates:** the numbered session list the rest of this repository runs,
starting with the skeleton session.

> Do NOT hand-author `sessions.json`. The first `session start`
> bootstraps it from this plan — state files are the writers' job.

---

### Session 3: Skeleton

1. Register.
2. Write the parent `pom.xml` (`groupId=com.example`, `artifactId=csv-parser-java`, `version=1.0.0-SNAPSHOT`, `packaging=pom`) listing `person-model`, `csv-deserializer`, `csv-database`, and `csv-parser-app` as modules; write each module's `pom.xml` with the correct inter-module `<dependency>` entries at `${project.version}` — `person-model` has none; `csv-deserializer` depends on `person-model`; `csv-database` depends on `person-model`; `csv-parser-app` depends on all three.
3. Add the ArchUnit dependency (`com.tngtech.archunit:archunit-junit5`) to `csv-parser-app/pom.xml`; write `csv-parser-app/src/test/java/com/example/csvparserapp/ArchitectureTest.java` asserting that no class residing outside the package `com.example.csvparserapp` (and its sub-packages) depends on any class in the package `com.example.csvdatabase` (and its sub-packages).
4. Declare the testing suite in `dabbler.yaml` under `testing.suites`: `name: maven`, `command: mvn test -DforkedProcessTimeoutInSeconds=120`, `expensive: true`, `covers: ['.']`, `test_roots: [person-model/src/test/java, csv-deserializer/src/test/java, csv-database/src/test/java, csv-parser-app/src/test/java]`.
5. Cross-provider verification.
6. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
7. Close-out.

**Creates:** `pom.xml`, `person-model/pom.xml`, `csv-deserializer/pom.xml`, `csv-database/pom.xml`, `csv-parser-app/pom.xml`, `csv-parser-app/src/test/java/com/example/csvparserapp/ArchitectureTest.java`, `dabbler.yaml`.

---

### Session 4: Person Model

1. Register.
2. Write `person-model/src/main/java/com/example/personmodel/Person.java` with fields `String name`, `String email`, and `int age`; an all-args constructor; a getter for each field; `equals`, `hashCode`, and `toString`; and `implements Serializable`. Write `person-model/src/test/java/com/example/personmodel/PersonTest.java` with tests covering the constructor, all getters, the `equals`/`hashCode` contract, and the `toString` format.
3. Cross-provider verification.
4. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
5. Close-out.

**Creates:** `person-model/src/main/java/com/example/personmodel/Person.java`, `person-model/src/test/java/com/example/personmodel/PersonTest.java`.

---

### Session 5: CSV Deserializer

1. Register.
2. Add the OpenCSV dependency (`com.opencsv:opencsv`) to `csv-deserializer/pom.xml`; write `csv-deserializer/src/main/java/com/example/csvdeserializer/CsvDeserializer.java` with a method `List<Person> deserialize(Path folder)` that reads every `.csv` file in the folder, maps each data record to a `Person` by column header name (case-insensitive, columns: `name`, `email`, `age`), and skips any record with a missing column or a non-integer `age` field — logging each skip at WARN level and continuing.
3. Write `csv-deserializer/src/test/java/com/example/csvdeserializer/CsvDeserializerTest.java` using real temp-directory CSV fixtures: one test asserts that a well-formed file returns the expected `List<Person>`; one test asserts that a file containing a malformed record returns a partial list without throwing an exception.
4. Cross-provider verification.
5. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
6. Close-out.

**Creates:** `csv-deserializer/src/main/java/com/example/csvdeserializer/CsvDeserializer.java`, `csv-deserializer/src/test/java/com/example/csvdeserializer/CsvDeserializerTest.java`.

---

### Session 6: Database Layer

1. Register.
2. Add the H2 dependency (`com.h2database:h2`, scope `compile`) to `csv-database/pom.xml`; write `csv-database/src/main/java/com/example/csvdatabase/PersonRepository.java` with two methods: `void insert(Connection conn, List<Person> persons)` — creates the `person` table if it does not exist (schema: `id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name VARCHAR, email VARCHAR, age INT`), then batch-inserts all records — and `List<Person> selectAll(Connection conn)` — executes `SELECT name, email, age FROM person ORDER BY id` and returns every row as a `Person`.
3. Write `csv-database/src/test/java/com/example/csvdatabase/PersonRepositoryTest.java` against an in-memory H2 connection (`jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`): insert two `Person` objects, call `selectAll`, and assert that both are returned with the correct field values.
4. Cross-provider verification.
5. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
6. Close-out.

**Creates:** `csv-database/src/main/java/com/example/csvdatabase/PersonRepository.java`, `csv-database/src/test/java/com/example/csvdatabase/PersonRepositoryTest.java`.

---

### Session 7: Console Application

1. Register.
2. Write `csv-parser-app/src/main/java/com/example/csvparserapp/Main.java` with a `main(String[] args)` method that: validates the first CLI argument as an existing folder path; accepts an optional second argument as the JDBC URL (defaulting to `jdbc:h2:./csv-parser` when absent); uses `CsvDeserializer.deserialize` to read persons from the folder; opens an H2 connection with the resolved JDBC URL; calls `PersonRepository.insert` then `PersonRepository.selectAll`; and prints a header line `name | email | age` followed by one data line per person in the format `<name> | <email> | <age>`. On any error, print a descriptive message to stderr and exit with code 1.
3. Write `csv-parser-app/src/test/java/com/example/csvparserapp/AppIntegrationTest.java` covering the full pipeline: create a temp folder containing a CSV file with exactly two valid records; pass a unique temp-file JDBC URL (e.g. `jdbc:h2:file:{TEMP_DIR}/test-db;DB_CLOSE_DELAY=-1`) as the second argument so no external database state can leak in; capture stdout by redirecting `System.out`; invoke `Main.main` with the folder path and temp DB URL; assert the captured output is exactly one header line followed by exactly two data lines — no more, no less.
4. Cross-provider verification.
5. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
6. Close-out.

**Creates:** `csv-parser-app/src/main/java/com/example/csvparserapp/Main.java`, `csv-parser-app/src/test/java/com/example/csvparserapp/AppIntegrationTest.java`.

---

### Session 8: Packaging

1. Register.
2. Add the `maven-assembly-plugin` to `csv-parser-app/pom.xml`, bound to the `package` phase, producing an executable fat JAR named `csv-parser-1.0.0-jar-with-dependencies.jar` in the directory given by `-Dhandoff.dir`; add the `packaging:` block to `dabbler.yaml` with `pack.argv: ["mvn", "package", "-Dhandoff.dir={output}"]`.
3. Verify the handoff artifact: run `mvn package -Dhandoff.dir=<temp-dir>`; assert that `<temp-dir>/csv-parser-1.0.0-jar-with-dependencies.jar` exists; create a minimal CSV fixture in a second temp folder and run `java -jar <temp-dir>/csv-parser-1.0.0-jar-with-dependencies.jar <fixture-folder> jdbc:h2:mem:verify;DB_CLOSE_DELAY=-1`; assert the output contains the expected header line and data line.
4. Cross-provider verification.
5. Full test suite: `mvn test -DforkedProcessTimeoutInSeconds=120`.
6. Close-out.

This session touches `csv-parser-app/pom.xml` and `dabbler.yaml` only; it creates no application code or tests.

**Modifies:** `csv-parser-app/pom.xml`, `dabbler.yaml`.

---

### Session 9: Release 1.0.0 (release: 1.0.0)
