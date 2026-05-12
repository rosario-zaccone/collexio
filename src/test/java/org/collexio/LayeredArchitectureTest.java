package org.collexio;


import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "org.collexio")
class LayeredArchitectureTest {

    @ArchTest
    static final ArchRule layered_architecture_is_respected =
            layeredArchitecture()
                    .consideringAllDependencies()

                    .layer("Bootstrap").definedBy("..bootstrap..")
                    .layer("Presentation").definedBy("..presentation..")
                    .layer("Business").definedBy("..business..")
                    .layer("Persistence").definedBy("..persistence..")

                    .whereLayer("Bootstrap").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Presentation").mayOnlyBeAccessedByLayers("Bootstrap")
                    .whereLayer("Business").mayOnlyBeAccessedByLayers("Presentation", "Bootstrap")
                    .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Business", "Presentation", "Bootstrap");

}
