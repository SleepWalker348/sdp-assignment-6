package sdp.visitor;

import sdp.element.*;
import java.util.ArrayList;
import java.util.List;

public class SecurityAuditVisitor implements ProjectVisitor {
    private List<String> securityIssues = new ArrayList<>();
    private int criticalIssuesCount = 0;

    @Override
    public void visit(SourceCodeFile file) {
        if (file.isHasHardcodedCredentials()) {
            securityIssues.add("[CRITICAL] Hardcoded credentials in source file: " + file.getName());
            criticalIssuesCount++;
        }
        if (file.isHighRisk()) {
            securityIssues.add("[WARNING] High-risk source file detected: " + file.getName());
        }
    }

    @Override
    public void visit(TestFile file) {}

    @Override
    public void visit(ConfigurationFile file) {
        if (file.isContainsCredentials()) {
            securityIssues.add("[CRITICAL] Exposed credentials in configuration: " + file.getName());
            criticalIssuesCount++;
        }
        if (!file.isSecure()) {
            securityIssues.add("[WARNING] Insecure configuration settings in: " + file.getName());
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (var dep : file.getDependencies()) {
            if (dep.isVulnerable()) {
                securityIssues.add("[CRITICAL] Vulnerable dependency found: " + dep.getName() + " (" + dep.getVersion() + ")");
                criticalIssuesCount++;
            }
        }
    }

    @Override
    public void visit(Directory directory) {}

    public List<String> getSecurityIssues() { return securityIssues; }
    public int getCriticalIssuesCount() { return criticalIssuesCount; }
}