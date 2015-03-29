package me.yingrui.segment.util

trait ISerialize {

    def save(writeHandler: SerializeHandler)

    def load(readHandler: SerializeHandler)

}
