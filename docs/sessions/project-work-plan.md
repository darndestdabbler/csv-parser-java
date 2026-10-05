# Project work plan — sessions

**Written by `dabbler` as a fold of `activity-log.json`.**
Hand edits are overwritten by the next append. The record is the log;
this page is one view of it.

---

## Sessions

The numbered sessions are declared from `session-plan.md`; each one's task is what its own plan step declared.

| # | Session | Releasable | Declared |
| ---: | --- | --- | --- |
| 1 | Author or import the solution plan | no | 2026-10-05 |
| 2 | Challenge the plan, then break it into numbered sessions | no | 2026-10-05 |
| 3 | Skeleton | no | 2026-10-05 |
| 4 | Person Model | no | 2026-10-05 |
| 5 | CSV Deserializer | — | not declared |
| 6 | Database Layer | — | not declared |
| 7 | Console Application | — | not declared |
| 8 | Packaging | — | not declared |
| 9 | Release 1.0.0 (release: 1.0.0) | — | not declared |

### Session 1 — Author or import the solution plan

**Releasable: no.**

Session 1 plans the CSV Parser Java Dabbler learning exercise. It gathers requirements from the user, documents the project brief and objectives, defines the four-module Maven architecture (person-model, csv-deserializer, csv-database, csv-parser-app), specifies module responsibilities and contracts, establishes the dependency graph, and outlines six phases of development work that will be executed in subsequent sessions. The planning is purely documentary and creates no code, build files, or project structure yet—only planning artifacts that guide future work.

**Amended after acceptance:**

- 2026-10-05 — step 'write-brief': its checks: Use git ls-files for cross-platform file existence check instead of POSIX test command (claude-code (anthropic, claude-haiku-4-5-20251001))
- 2026-10-05 — step 'write-solution-plan': its checks: Use git ls-files for cross-platform file existence check (claude-code (anthropic, claude-haiku-4-5-20251001))
- 2026-10-05 — step 'verify-planning-docs': its checks: Use git ls-files for cross-platform file existence check (claude-code (anthropic, claude-haiku-4-5-20251001))

### Session 2 — Challenge the plan, then break it into numbered sessions

**Releasable: no.**

Session 2 reviews and challenges the four-module solution plan, resolves the three deferred decisions, and records rationale in docs/planning/solution-plan.md; then appends numbered sessions 3 through 9 to docs/sessions/session-plan.md, covering the Maven skeleton, Person model, CSV deserializer, database layer, console application, packaging, and release 1.0.0.

### Session 3 — Skeleton

**Releasable: no.**

Set up the Maven project skeleton for csv-parser-java: write a parent pom.xml (groupId=com.example, artifactId=csv-parser-java, version=1.0.0-SNAPSHOT, packaging=pom) that lists person-model, csv-deserializer, csv-database, and csv-parser-app as modules; write each module's pom.xml with the correct inter-module dependency entries at ${project.version} (person-model has none; csv-deserializer and csv-database each depend on person-model; csv-parser-app depends on all three), and include the ArchUnit dependency (com.tngtech.archunit:archunit-junit5) in csv-parser-app/pom.xml; write an ArchitectureTest.java in csv-parser-app asserting that no class outside com.example.csvparserapp depends on any class in com.example.csvdatabase; and declare the Maven testing suite in dabbler.yaml.

### Session 4 — Person Model

**Releasable: no.**

Create the Person model class and its tests for the person-model Maven module. Person.java will have String name, String email, and int age fields, an all-args constructor, a getter for each field, equals/hashCode, toString, and implements Serializable. PersonTest.java will test the constructor, all getters, the equals/hashCode contract, and the toString format.
