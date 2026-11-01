package sdp.visitor;

import sdp.element.*;

public class TechnicalDebtVisitor implements ProjectVisitor {
    private int debtScoreMinutes = 0;

    @Override
    public void visit(SourceCodeFile file) {
        if (file.getCyclomaticComplexity() > 10) {
            debtScoreMinutes += (file.getCyclomaticComplexity() - 10) * 30;
        }
        if (file.getLinesOfCode() > 300) {
            debtScoreMinutes += 120;
        }
    }

    @Override
    public void visit(TestFile file) {
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (!file.isSecure()) {
            debtScoreMinutes += 60;
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (var dep : file.getDependencies()) {
            if (dep.isVulnerable()) {
                debtScoreMinutes += 180;
            }
        }
    }

    @Override
    public void visit(Directory directory) {}

    public int getDebtScoreMinutes() { return debtScoreMinutes; }
    public String getFormattedDebt() {
        int hours = debtScoreMinutes / 60;
        int mins = debtScoreMinutes % 60;
        return hours + " hours " + mins + " minutes";
    }
}