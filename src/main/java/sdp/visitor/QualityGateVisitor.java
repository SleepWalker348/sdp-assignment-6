package sdp.visitor;

import sdp.element.ProjectElement;

public class QualityGateVisitor implements ProjectVisitor {
    private ProjectStatisticsVisitor statsVisitor = new ProjectStatisticsVisitor();
    private SecurityAuditVisitor securityVisitor = new SecurityAuditVisitor();

    private boolean complexityPass = false;
    private boolean testRatioPass = false;
    private boolean securityPass = false;
    private boolean overallPass = false;

    public void evaluate(ProjectElement root) {
        root.accept(statsVisitor);
        root.accept(securityVisitor);

        complexityPass = statsVisitor.getAverageComplexity() <= 10.0;
        testRatioPass = statsVisitor.getTestToSourceRatio() >= 0.40;
        securityPass = securityVisitor.getCriticalIssuesCount() == 0;

        overallPass = complexityPass && testRatioPass && securityPass;
    }

    @Override public void visit(sdp.element.SourceCodeFile file) {}
    @Override public void visit(sdp.element.TestFile file) {}
    @Override public void visit(sdp.element.ConfigurationFile file) {}
    @Override public void visit(sdp.element.DependencyFile file) {}
    @Override public void visit(sdp.element.Directory directory) {}

    public void printReport() {
        System.out.println("QUALITY GATE REPORT");
        System.out.println("Security: " + (securityPass ? "PASS" : "FAIL") + " (Critical Issues: " + securityVisitor.getCriticalIssuesCount() + ")");
        System.out.println("Complexity: " + (complexityPass ? "PASS" : "FAIL") + " (Avg: " + String.format("%.2f", statsVisitor.getAverageComplexity()) + ")");
        System.out.println("Tests: " + (testRatioPass ? "PASS" : "FAIL") + " (Ratio: " + String.format("%.2f", statsVisitor.getTestToSourceRatio()) + ")");
        System.out.println("FINAL RESULT: " + (overallPass ? "PASSED" : "FAILED"));
    }

    public boolean isOverallPass() { return overallPass; }
}