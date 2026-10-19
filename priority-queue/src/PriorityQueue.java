import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Kolejka priorytetowa oparta na kopcu binarnym (binary heap).
 * Domyślnie działa jako min-heap: najmniejszy element ma najwyższy priorytet.
 * Zachowanie można zmienić, przekazując własny {@link Comparator}.
 *
 * @param <E> typ elementów w kolejce
 */
public class PriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    /** Min-heap dla elementów {@link Comparable}. */
    public PriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    /** Kolejka z własnym komparatorem (np. max-heap: {@code Comparator.reverseOrder()}). */
    public PriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public PriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public PriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Pojemność początkowa musi być >= 1");
        }
        this.heap = new Object[initialCapacity];
        this.comparator = comparator;
    }

    /** Dodaje element do kolejki. Amortyzowany czas: O(log n). */
    public void offer(E element) {
        if (element == null) {
            throw new NullPointerException("Element nie może być null");
        }
        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size);
        size++;
    }

    /** Alias dla {@link #offer(Object)}. */
    public void add(E element) {
        offer(element);
    }

    /**
     * Usuwa i zwraca element o najwyższym priorytecie.
     * Czas: O(log n).
     *
     * @throws NoSuchElementException gdy kolejka jest pusta
     */
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            throw new NoSuchElementException("Kolejka jest pusta");
        }
        E result = (E) heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    /**
     * Zwraca element o najwyższym priorytecie bez usuwania.
     * Czas: O(1).
     *
     * @throws NoSuchElementException gdy kolejka jest pusta
     */
    @SuppressWarnings("unchecked")
    public E peek() {
        if (size == 0) {
            throw new NoSuchElementException("Kolejka jest pusta");
        }
        return (E) heap[0];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(heap, 0, size, null);
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= heap.length) {
            return;
        }
        int newCapacity = Math.max(heap.length * 2, minCapacity);
        heap = Arrays.copyOf(heap, newCapacity);
    }

    /** Przywraca własność kopca po wstawieniu na końcu tablicy. */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(index, parent) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    /** Przywraca własność kopca po usunięciu korzenia. */
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && compare(left, smallest) < 0) {
                smallest = left;
            }
            if (right < size && compare(right, smallest) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(int i, int j) {
        E a = (E) heap[i];
        E b = (E) heap[j];
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        Object tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        return sb.append(']').toString();
    }
}
