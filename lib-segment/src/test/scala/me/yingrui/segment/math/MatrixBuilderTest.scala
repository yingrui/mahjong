package me.yingrui.segment.math

import org.scalatest.{FunSpec, Matchers}

class MatrixBuilderTest extends FunSpec with Matchers {

  describe("A Matrix Builder") {
    val matrixBuilders = Array[MatrixBuilder](new DenseMatrixBuilder(), new NDMatrixBuilder())

    it("should create vector given double array") {
      matrixBuilders.foreach(builder => {
        val vector = builder.vector(Array(1D, 2D, 3D, 4D))
        vector.row shouldBe 1
        vector.col shouldBe 4
        vector(0, 0) should be (1D)
        vector(0, 1) should be (2D)
        vector(0, 2) should be (3D)
        vector(0, 3) should be (4D)
      })
    }

    it("should create matrix given row, col and double array") {
      matrixBuilders.foreach(builder => {
        val m = builder.apply(2, 2, Array(1D, 2D, 3D, 4D))
        m.row shouldBe 2
        m.col shouldBe 2
        m.flatten should be (Array(1D, 2D, 3D, 4D))
      })
    }

    it("should create matrix given row, col and boolean array") {
      matrixBuilders.foreach(builder => {
        val m = builder.applyBoolean(2, 2, Array(true, true, false, false))
        m.row shouldBe 2
        m.col shouldBe 2
        m.flatten should be (Array(1D, 1D, -1D, -1D))
      })
    }

    it("should create matrix given row, col") {
      matrixBuilders.foreach(builder => {
        val m = builder.apply(2, 2)
        m.row shouldBe 2
        m.col shouldBe 2
        m.flatten should be (Array(0D, 0D, 0D, 0D))
      })
    }

    it("should create matrix given size and identity is false") {
      matrixBuilders.foreach(builder => {
        val m = builder.apply(2, false)
        m.row shouldBe 2
        m.col shouldBe 2
        m.flatten should be (Array(0D, 0D, 0D, 0D))
      })
    }

    it("should create matrix given size and identity is true") {
      matrixBuilders.foreach(builder => {
        val m = builder.apply(2, true)
        m.row shouldBe 2
        m.col shouldBe 2
        m(0, 0) should be (1D)
        m(0, 1) should be (0D)
        m(1, 0) should be (0D)
        m(1, 1) should be (1D)
      })
    }

    it("should create matrix given array[array[double]]") {
      matrixBuilders.foreach(builder => {
        val m = builder.apply(Array(Array(1D, 2D), Array(3D, 4D)))
        m.row shouldBe 2
        m.col shouldBe 2
        m(0, 0) should be (1D)
        m(0, 1) should be (2D)
        m(1, 0) should be (3D)
        m(1, 1) should be (4D)
      })
    }

    it("should create matrix with random elements given row, col, max, min") {
      matrixBuilders.foreach(builder => {
        val m = builder.randomize(2, 2, 1D, -1D)
        m.row shouldBe 2
        m.col shouldBe 2
        m(0, 0) should be (0D +- 1D)
        m(0, 1) should be (0D +- 1D)
        m(1, 0) should be (0D +- 1D)
        m(1, 1) should be (0D +- 1D)
      })
    }

    it("should create matrix with random elements given row, col") {
      matrixBuilders.foreach(builder => {
        val m = builder.randomize(2, 2, 1D, -1D)
        m.row shouldBe 2
        m.col shouldBe 2
        m(0, 0) should be (0D +- 1D)
        m(0, 1) should be (0D +- 1D)
        m(1, 0) should be (0D +- 1D)
        m(1, 1) should be (0D +- 1D)
      })
    }

  }


}
