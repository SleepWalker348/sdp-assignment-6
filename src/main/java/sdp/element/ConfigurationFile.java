package sdp.element;

import sdp.visitor.ProjectVisitor;

public class ConfigurationFile implements ProjectElement {
    private String name;
    private int linesOfCode;
    private boolean secure;
    private boolean containsCredentials;

    public ConfigurationFile(String name, int linesOfCode, boolean secure, boolean containsCredentials) {
        this.name = name;
        this.linesOfCode = linesOfCode;
        this.secure = secure;
        this.containsCredentials = containsCredentials;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    public int getLinesOfCode() { return linesOfCode; }
    public boolean isSecure() { return secure; }
    public boolean isContainsCredentials() { return containsCredentials; }
}