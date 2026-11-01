package sdp;

import sdp.element.*;
import sdp.visitor.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Fintech Project Structure...");

        Directory fintechProject = new Directory("fintech-project");

        Directory srcDir = new Directory("src");
        srcDir.addElement(new SourceCodeFile("PaymentService.java", 150, 8, false, false));
        srcDir.addElement(new SourceCodeFile("UserService.java", 220, 14, true, true)); // has hardcoded creds & high risk

        Directory testsDir = new Directory("tests");
        testsDir.addElement(new TestFile("PaymentServiceTest.java", 90, 10, 10));

        Directory configDir = new Directory("config");
        configDir.addElement(new ConfigurationFile("application.properties", 35, false, true));

        List<DependencyFile.Dependency> deps = List.of(
                new DependencyFile.Dependency("spring-boot-starter-web", "3.2.0", false, false),
                new DependencyFile.Dependency("log4j-core", "2.14.0", true, false),
                new DependencyFile.Dependency("unauthorized-lib", "1.0", false, true)
        );
        DependencyFile pomFile = new DependencyFile("pom.xml", deps);

        fintechProject.addElement(srcDir);
        fintechProject.addElement(testsDir);
        fintechProject.addElement(configDir);
        fintechProject.addElement(pomFile);

        System.out.println("\n1. Running Project Statistics Visitor");
        ProjectStatisticsVisitor statsVisitor = new ProjectStatisticsVisitor();
        fintechProject.accept(statsVisitor);
        System.out.println("Total Files: " + statsVisitor.getTotalFiles());
        System.out.println("Total LOC: " + statsVisitor.getTotalLinesOfCode());
        System.out.println("Average Complexity: " + statsVisitor.getAverageComplexity());
        System.out.println("Test/Source Ratio: " + statsVisitor.getTestToSourceRatio());

        System.out.println("\n2. Running Security Audit Visitor");
        SecurityAuditVisitor securityVisitor = new SecurityAuditVisitor();
        fintechProject.accept(securityVisitor);
        securityVisitor.getSecurityIssues().forEach(System.out::println);

        System.out.println("\n3. Running Compliance Visitor");
        ComplianceVisitor complianceVisitor = new ComplianceVisitor();
        fintechProject.accept(complianceVisitor);
        complianceVisitor.getViolations().forEach(System.out::println);

        System.out.println("\n4. Running Technical Debt Visitor");
        TechnicalDebtVisitor debtVisitor = new TechnicalDebtVisitor();
        fintechProject.accept(debtVisitor);
        System.out.println("Estimated Technical Debt: " + debtVisitor.getFormattedDebt());

        System.out.println("\n5. Running Quality Gate Visitor");
        QualityGateVisitor qualityGateVisitor = new QualityGateVisitor();
        qualityGateVisitor.evaluate(fintechProject);
        qualityGateVisitor.printReport();
    }
}