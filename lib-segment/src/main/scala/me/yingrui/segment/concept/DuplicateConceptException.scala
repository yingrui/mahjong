package me.yingrui.segment.concept

class DuplicateConceptException(concept: Concept) extends Exception {

    private val existedConcept : Concept = concept

    override def getMessage() : String = {
        return "A concept '" + existedConcept.getName() + "' has id " + existedConcept.getId() + " already existed."
    }
}
