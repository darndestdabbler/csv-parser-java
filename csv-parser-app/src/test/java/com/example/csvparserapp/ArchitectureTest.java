package com.example.csvparserapp;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    @Test
    void only_csvparserapp_may_depend_on_csvdatabase() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.example");

        ArchRule rule = noClasses()
                .that().resideOutsideOfPackages("com.example.csvparserapp..")
                .should().dependOnClassesThat().resideInAPackage("com.example.csvdatabase..")
                .allowEmptyShould(true);

        rule.check(classes);
    }
}
