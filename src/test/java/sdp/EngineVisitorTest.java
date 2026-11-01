package sdp;

import sdp.element.*;
import sdp.visitor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EngineVisitorTest {

    private Directory root;

    @BeforeEach
    void setUp() {
        root = new Directory("root");
        root.addElement(new SourceCodeFile("Auth.java", 100, 5, false, false));
        root.addElement(new TestFile("AuthTest.java", 50, 5, 5));
        root.addElement(new ConfigurationFile("app.properties", 20, true, false));
        root.addElement(new DependencyFile("pom.xml", List.of(
                new DependencyFile.Dependency("junit", "5.9.0", false, false)
        )));
    }

    @Test
    void testProjectStatisticsVisitor() {
        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();
        root.accept(visitor);

        assertEquals(4, visitor.getTotalFiles());
        assertEquals(1, visitor.getSourceFilesCount());
        assertEquals(1, visitor.getTestFilesCount());
        assertEquals(170, visitor.getTotalLinesOfCode());
        assertEquals(5.0, visitor.getAverageComplexity());
        assertEquals(1.0, visitor.getTestToSourceRatio());
    }

    @Test
    void testSecurityAuditVisitorDetectsIssues() {
        Directory insecureRoot = new Directory("insecure");
        insecureRoot.addElement(new SourceCodeFile("Hack.java", 80, 12, true, true));
        insecureRoot.addElement(new ConfigurationFile("secrets.env", 10, false, true));
        insecureRoot.addElement(new DependencyFile("pom.xml", List.of(
                new DependencyFile.Dependency("log4j", "2.0", true, false)
        )));

        SecurityAuditVisitor visitor = new SecurityAuditVisitor();
        insecureRoot.accept(visitor);

        assertEquals(3, visitor.getCriticalIssuesCount());
        assertFalse(visitor.getSecurityIssues().isEmpty());
    }

    @Test
    void testSecurityAuditVisitorCleanProject() {
        SecurityAuditVisitor visitor = new SecurityAuditVisitor();
        root.accept(visitor);

        assertEquals(0, visitor.getCriticalIssuesCount());
        assertTrue(visitor.getSecurityIssues().isEmpty());
    }

    @Test
    void testQualityGatePass() {
        QualityGateVisitor qg = new QualityGateVisitor();
        qg.evaluate(root);
        assertTrue(qg.isOverallPass());
    }

    @Test
    void testQualityGateFail() {
        Directory badRoot = new Directory("bad");
        badRoot.addElement(new SourceCodeFile("GodClass.java", 500, 25, true, true));
        badRoot.addElement(new DependencyFile("pom.xml", List.of(
                new DependencyFile.Dependency("bad-lib", "1.0", true, false)
        )));

        QualityGateVisitor qg = new QualityGateVisitor();
        qg.evaluate(badRoot);
        assertFalse(qg.isOverallPass());
    }

    @Test
    void testComplianceVisitorViolations() {
        Directory compRoot = new Directory("comp");
        compRoot.addElement(new DependencyFile("pom.xml", List.of(
                new DependencyFile.Dependency("prohibited-lib", "RELEASE", false, true)
        )));

        ComplianceVisitor visitor = new ComplianceVisitor();
        compRoot.accept(visitor);

        assertFalse(visitor.getViolations().isEmpty());
    }

    @Test
    void testTechnicalDebtVisitor() {
        Directory debtRoot = new Directory("debt");
        debtRoot.addElement(new SourceCodeFile("Complex.java", 350, 15, false, false));

        TechnicalDebtVisitor visitor = new TechnicalDebtVisitor();
        debtRoot.accept(visitor);

        assertTrue(visitor.getDebtScoreMinutes() > 0);
        assertNotNull(visitor.getFormattedDebt());
    }

    @Test
    void testNestedDirectories() {
        Directory outer = new Directory("outer");
        Directory inner = new Directory("inner");
        inner.addElement(new SourceCodeFile("Nested.java", 40, 2, false, false));
        outer.addElement(inner);

        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();
        outer.accept(visitor);

        assertEquals(1, visitor.getTotalFiles());
        assertEquals(40, visitor.getTotalLinesOfCode());
    }

    @Test
    void testEmptyDirectory() {
        Directory emptyDir = new Directory("empty");
        ProjectStatisticsVisitor visitor = new ProjectStatisticsVisitor();
        emptyDir.accept(visitor);

        assertEquals(0, visitor.getTotalFiles());
        assertEquals(0.0, visitor.getAverageComplexity());
    }

    @Test
    void testSourceCodeFileGetters() {
        SourceCodeFile file = new SourceCodeFile("Test.java", 100, 4, false, false);
        assertEquals("Test.java", file.getName());
        assertEquals(100, file.getLinesOfCode());
        assertEquals(4, file.getCyclomaticComplexity());
        assertFalse(file.isHasHardcodedCredentials());
        assertFalse(file.isHighRisk());
    }

    @Test
    void testConfigurationFileGetters() {
        ConfigurationFile cfg = new ConfigurationFile("cfg.properties", 15, true, false);
        assertEquals("cfg.properties", cfg.getName());
        assertEquals(15, cfg.getLinesOfCode());
        assertTrue(cfg.isSecure());
        assertFalse(cfg.isContainsCredentials());
    }

    @Test
    void testTestFileGetters() {
        TestFile tf = new TestFile("MyTest.java", 60, 4, 4);
        assertEquals("MyTest.java", tf.getName());
        assertEquals(60, tf.getLinesOfCode());
        assertEquals(4, tf.getTestCount());
        assertEquals(4, tf.getPassedTests());
    }
}