package websiteschema.mpsegment.concept;

import java.util.ArrayList;
import java.util.List;

public class Concept {

    public static final Concept UNKNOWN = new Concept(0, "N/A");

    private long id;
    private String name;
    private List<Concept> children;
    private Concept parent;

    public Concept(long id, String name) {
        this.id = id;
        this.name = name;
        children = new ArrayList<Concept>();
    }

    public List<Concept> getChildren() {
        return children;
    }

    public Concept getParent() {
        return parent;
    }

    public List<Concept> getSiblings() {
        return getParent().getChildren();
    }

    public void addChild(Concept child) {
        child.setParent(this);
        children.add(child);
    }

    private void setParent(Concept parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
