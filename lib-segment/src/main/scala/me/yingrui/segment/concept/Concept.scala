package me.yingrui.segment.concept

class Concept(id: Long, name: String) {

    private var description : String = null
    private var children = List[Concept]()
    private var parent: Concept = null

    def getChildren() : List[Concept] = {
        return children
    }

    def getParent() : Concept = {
        return parent
    }

    def getSiblings() : List[Concept] = {
        return getParent().getChildren()
    }

    def addChild(child: Concept) {
        child.setParent(this)
        children = child :: children
    }

    private def setParent(parent: Concept) {
        this.parent = parent
    }

    def getName() : String = {
        return name
    }

    def getId() : Long = {
        return id
    }

    def getDescription() : String = {
        return description
    }

    def setDescription(description: String) {
        this.description = description
    }
}

object Concept {
  val UNKNOWN = new Concept(0, "N/A")
}
