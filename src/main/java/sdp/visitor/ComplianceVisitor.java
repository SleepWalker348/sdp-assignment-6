package sdp.visitor;

import sdp.element.*;
import java.util.ArrayList;
import java.util.List;

public class ComplianceVisitor implements ProjectVisitor {
    private List<String> violations = new ArrayList<>();
    private boolean hasTestsInModule = false;

    @Override
    public void visit(SourceCodeFile file) {
        if (file.isHasHardcodedCredentials()) {
            violations.add("Compliance Rule Violation: Exposed credentials in " + file.getName());
        }
    }

    @Override
    public void visit(TestFile file) {
        hasTestsInModule = true;
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.isContainsCredentials()) {
            violations.add("Compliance Rule Violation: Credentials stored in config " + file.getName());
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (var dep : file.getDependencies()) {
            if (dep.getVersion() == null || dep.getVersion().isBlank() || dep.getVersion().equals("RELEASE")) {
                violations.add("Compliance Rule Violation: Unspecified or dynamic version for dependency " + dep.getName());
            }
            if (dep.isProhibited()) {
                violations.add("Compliance Rule Violation: Prohibited dependency used: " + dep.getName());
            }
        }
    }

    @Override
    public void visit(Directory directory) {}

    public List<String> getViolations() { return violations; }
}