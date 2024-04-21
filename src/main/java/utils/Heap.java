package utils;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Heap<T> {
    private final List<T> heap;
    private final Comparator<T> comparator;
    
    public Heap(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }
    
    public void add(T element) {
        heap.add(element);
        bubbleUp(heap.size() - 1);
    }
    
    public boolean contains(T element) {
        return heap.contains(element);
    }
    
    public T peek() {
        if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
        return heap.get(0);
    }
    
    public T remove() {
        if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
        
        T removedElement = heap.get(0);
        int lastIndex = heap.size() - 1;
        heap.set(0, heap.get(lastIndex));
        heap.remove(lastIndex);
        if (!heap.isEmpty()) {
            bubbleDown(0);
        }
        
        return removedElement;
    }
    
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (comparator.compare(heap.get(index), heap.get(parentIndex)) > 0) {
                break;
            }
            
            swap(index, parentIndex);
            index = parentIndex;
        }
    }
    
    private void bubbleDown(int index) {
        int lastIndex = heap.size() - 1;
        while (index < lastIndex) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int largestChildIndex = index;
            
            if (
                leftChildIndex <= lastIndex &&
                    comparator.compare(
                        heap.get(leftChildIndex),
                        heap.get(largestChildIndex)
                    ) <= 0
            ) {
                largestChildIndex = leftChildIndex;
            }
            
            if (
                rightChildIndex <= lastIndex &&
                    comparator.compare(
                        heap.get(rightChildIndex),
                        heap.get(largestChildIndex)
                    ) <= 0
            ) {
                largestChildIndex = rightChildIndex;
            }
            
            if (largestChildIndex == index) break;
            
            swap(index, largestChildIndex);
            index = largestChildIndex;
        }
    }
    
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    public int size() {
        return heap.size();
    }
}
