package websiteschema.mpsegment.concept;

public class DuplicateConceptException extends Exception {

    private Concept existedConcept;

    public DuplicateConceptException(Concept concept) {
        existedConcept = concept;
    }

    @Override
    public String getMessage() {
        return "A concept '" + existedConcept.getName() + "' has id " + existedConcept.getId() + " already existed.";
    }
}
