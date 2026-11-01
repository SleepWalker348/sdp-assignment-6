package sdp.element;

import sdp.visitor.ProjectVisitor;

public class SourceCodeFile implements ProjectElement {
    private String name;
    private int linesOfCode;
    private int cyclomaticComplexity;
    private boolean hasHardcodedCredentials;
    private boolean highRisk;

    public SourceCodeFile(String name, int linesOfCode, int cyclomaticComplexity, boolean hasHardcodedCredentials, boolean highRisk) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.hasHardcodedCredentials = hasHardcodedCredentials;
        this.highRisk = highRisk;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    public int getLinesOfCode() { return linesOfCode; }
    public int getCyclomaticComplexity() { return cyclomaticComplexity; }
    public boolean isHasHardcodedCredentials() { return hasHardcodedCredentials; }
    public boolean isHighRisk() { return highRisk; }
}