package websiteschema.mpsegment.dict;

class DictionaryException(message:String) extends Throwable {

    override def getMessage() : String = {
        return message;
    }
}
