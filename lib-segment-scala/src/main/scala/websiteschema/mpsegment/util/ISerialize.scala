package websiteschema.mpsegment.util;

trait ISerialize {

    def save(writeHandler: SerializeHandler)

    def load(readHandler: SerializeHandler)

}
