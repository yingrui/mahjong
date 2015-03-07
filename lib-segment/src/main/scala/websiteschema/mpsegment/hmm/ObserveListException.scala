package websiteschema.mpsegment.hmm

class ObserveListException(detail: String) extends Exception {

    override def getMessage() : String = {
        return detail
    }

    override def toString() : String = {
        return "Observe List Exception : " + detail
    }
}
