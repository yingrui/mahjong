package me.yingrui.segment.hmm

class TrieNodeQuickSort extends TrieNodeSortor {

  private def compare(x: Trie, y: Trie): Int = x.key - y.key

  override def sort(values: Array[Trie]): Array[Trie] = {
    if (values == null || values.length == 0) {
      return values
    }
    var number = values.length
    quicksort(values, 0, number - 1)
    return values
  }

  private def quicksort(numbers: Array[Trie], low: Int, high: Int) {
    var i = low
    var j = high
    // Get the pivot element from the middle of the list
    var pivot = numbers((low + high) / 2)

    // Divide into two lists
    while (i <= j) {
      // If the current value from the left list is smaller then the pivot
      // element then get the next element from the left list
      var c = compare(numbers(i), pivot)
      while (c < 0) {
        i += 1
        c = compare(numbers(i), pivot)
      }
      // If the current value from the right list is larger then the pivot
      // element then get the next element from the right list
      c = compare(numbers(j), pivot)
      while (c > 0) {
        j -= 1
        c = compare(numbers(j), pivot)
      }

      // If we have found a values in the left list which is larger then
      // the pivot element and if we have found a value in the right list
      // which is smaller then the pivot element then we exchange the
      // values.
      // As we are done we can increase i and j
      if (i <= j) {
        exchange(numbers, i, j)
        i += 1
        j -= 1
      }
    }
    // Recursion
    if (low < j) {
      quicksort(numbers, low, j)
    }
    if (i < high) {
      quicksort(numbers, i, high)
    }
  }

  private def exchange(numbers: Array[Trie], i: Int, j: Int) {
    var temp = numbers(i)
    numbers(i) = numbers(j)
    numbers(j) = temp
  }
}
