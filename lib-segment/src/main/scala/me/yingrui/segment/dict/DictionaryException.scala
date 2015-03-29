package me.yingrui.segment.dict

class DictionaryException(message:String) extends Throwable {

    override def getMessage() : String = {
        return message
    }
}
