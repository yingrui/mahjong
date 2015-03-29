package me.yingrui.segment.pinyin

import collection.mutable.ListBuffer

class Window[T](size: Int) {

    private val datas = ListBuffer[T]()

    def clear() {
        datas.clear()
    }

    def add(data: T) {
        datas += (data)
        if (datas.size > size) {
            datas.remove(0)
        }
    }

    def toArray() = datas.toList
}
