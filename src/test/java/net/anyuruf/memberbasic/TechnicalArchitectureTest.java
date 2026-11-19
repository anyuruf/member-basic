package net.anyuruf.memberbasic;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "net.anyuruf.memberbasic",  importOptions = DoNotIncludeTests.class)
public class TechnicalArchitectureTest {

    private static final String BASE_PACKAGE = "net.anyuruf.memberbasic";
    private static final String CORE_PACKAGE = BASE_PACKAGE + ".domain";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE + ".infrastructure";

    @ArchTest
    public static ArchRule HEXAGONAL_ARCHITECTURE =
            Architectures.onionArchitecture()
                .withOptionalLayers(true)
                .domainModels(CORE_PACKAGE + ".member.model..")
                .applicationServices(CORE_PACKAGE + ".member.service..",
                		CORE_PACKAGE + ".member.spi..",  
                		CORE_PACKAGE + ".member.api.."	
                		)
                .adapter("persistence", ADAPTER_PACKAGE + ".member.spi..")
                .adapter("controller", ADAPTER_PACKAGE + ".member.api..");
}

