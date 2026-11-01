package sdp.element;

import sdp.visitor.ProjectVisitor;
import java.util.List;

public class DependencyFile implements ProjectElement {
    private String name;
    private List<Dependency> dependencies;

    public static class Dependency {
        private String name;
        private String version;
        private boolean vulnerable;
        private boolean prohibited;

        public Dependency(String name, String version, boolean vulnerable, boolean prohibited) {
            this.name = name;
            this.version = version;
            this.vulnerable = vulnerable;
            this.prohibited = prohibited;
        }

        public String getName() { return name; }
        public String getVersion() { return version; }
        public boolean isVulnerable() { return vulnerable; }
        public boolean isProhibited() { return prohibited; }
    }

    public DependencyFile(String name, List<Dependency> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    public List<Dependency> getDependencies() { return dependencies; }
}