package websiteschema.mpsegment.concept;

public class ConceptRepository {
    private static ConceptRepository instance = new ConceptRepository();

    public static ConceptRepository getInstance() {
        return instance;
    }

    private ConceptTree verbConceptTree;
    private ConceptTree nounConceptTree;
    private ConceptTree adjConceptTree;

    public ConceptRepository() {
        nounConceptTree = new ConceptLoader("websiteschema/mpsegment/noun-concepts.txt").getConceptTree();
        verbConceptTree = new ConceptLoader("websiteschema/mpsegment/verb-concepts.txt").getConceptTree();
        adjConceptTree = new ConceptLoader("websiteschema/mpsegment/adj-concepts.txt").getConceptTree();
    }

    public ConceptTree getNounConceptTree() {
        return nounConceptTree;
    }

    protected void setNounConceptTree(ConceptTree nounConceptTree) {
        this.nounConceptTree = nounConceptTree;
    }

    protected void setVerbConceptTree(ConceptTree verbConceptTree) {
        this.verbConceptTree = verbConceptTree;
    }

    protected void setAdjConceptTree(ConceptTree adjConceptTree) {
        this.adjConceptTree = adjConceptTree;
    }

    public ConceptTree getAdjConceptTree() {
        return adjConceptTree;
    }

    public ConceptTree getVerbConceptTree() {
        return verbConceptTree;
    }

    public Concept getConceptByName(String name) {
        if (name.startsWith("n-")) {
            return getNounConceptTree().getConceptByName(name);
        }

        if (name.startsWith("v-")) {
            return getVerbConceptTree().getConceptByName(name);
        }

        if (name.startsWith("a-")) {
            return getAdjConceptTree().getConceptByName(name);
        }
        return null;
    }
}
