package websiteschema.mpsegment.dict;

public class DictionaryException extends Throwable {
    private String message;

    public DictionaryException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
