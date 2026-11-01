package sdp.visitor;

import sdp.element.*;

public class ProjectStatisticsVisitor implements ProjectVisitor {
    private int totalFiles = 0;
    private int totalLinesOfCode = 0;
    private int testFilesCount = 0;
    private int sourceFilesCount = 0;
    private int totalDependencies = 0;
    private int totalComplexitySum = 0;

    @Override
    public void visit(SourceCodeFile file) {
        totalFiles++;
        sourceFilesCount++;
        totalLinesOfCode += file.getLinesOfCode();
        totalComplexitySum += file.getCyclomaticComplexity();
    }

    @Override
    public void visit(TestFile file) {
        totalFiles++;
        testFilesCount++;
        totalLinesOfCode += file.getLinesOfCode();
    }

    @Override
    public void visit(ConfigurationFile file) {
        totalFiles++;
        totalLinesOfCode += file.getLinesOfCode();
    }

    @Override
    public void visit(DependencyFile file) {
        totalFiles++;
        totalDependencies += file.getDependencies().size();
    }

    @Override
    public void visit(Directory directory) {}

    public double getAverageComplexity() {
        return sourceFilesCount == 0 ? 0.0 : (double) totalComplexitySum / sourceFilesCount;
    }

    public double getTestToSourceRatio() {
        return sourceFilesCount == 0 ? 0.0 : (double) testFilesCount / sourceFilesCount;
    }

    public int getTotalFiles() { return totalFiles; }
    public int getTotalLinesOfCode() { return totalLinesOfCode; }
    public int getTestFilesCount() { return testFilesCount; }
    public int getSourceFilesCount() { return sourceFilesCount; }
    public int getTotalDependencies() { return totalDependencies; }
}