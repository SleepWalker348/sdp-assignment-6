package sdp.element;

import sdp.visitor.ProjectVisitor;

public class TestFile implements ProjectElement {
    private String name;
    private int linesOfCode;
    private int testCount;
    private int passedTests;

    public TestFile(String name, int linesOfCode, int testCount, int passedTests) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.testCount = testCount;
        this.passedTests = passedTests;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    public int getLinesOfCode() { return linesOfCode; }
    public int getTestCount() { return testCount; }
    public int getPassedTests() { return passedTests; }
}