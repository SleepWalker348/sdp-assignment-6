package sdp.element;

import sdp.visitor.ProjectVisitor;

public interface ProjectElement {
    void accept(ProjectVisitor visitor);
    String getName();
}