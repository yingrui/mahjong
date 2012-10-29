/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.Comparator;

/**
 *
 * @author ray
 */
public class TrieNodeQuickSort implements TrieNodeSortor {

    Comparator<Trie> comparator = null;
    
    public TrieNodeQuickSort() {
        comparator = new Comparator<Trie>() {

            @Override
            public int compare(Trie o1, Trie o2) {
                return o1.key - o2.key;
            }
        };
    }

    @Override
    public Trie[] sort(Trie[] values) {
        if (values == null || values.length == 0) {
            return values;
        }
        int number = values.length;
        quicksort(values, 0, number - 1);
        return values;
    }

    private void quicksort(Trie[] numbers, int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        Trie pivot = numbers[(low + high) / 2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            int c = comparator.compare(numbers[i], pivot);
            while (c < 0) {
                i++;
                c = comparator.compare(numbers[i], pivot);
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            c = comparator.compare(numbers[j], pivot);
            while (c > 0) {
                j--;
                c = comparator.compare(numbers[j], pivot);
            }

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(numbers, i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j) {
            quicksort(numbers, low, j);
        }
        if (i < high) {
            quicksort(numbers, i, high);
        }
    }

    private void exchange(Trie[] numbers, int i, int j) {
        Trie temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }
}
