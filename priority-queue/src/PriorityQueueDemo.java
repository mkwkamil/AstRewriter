import java.util.Comparator;

/**
 * Proste demo kolejki priorytetowej.
 *
 * Uruchomienie:
 *   javac -d out src/*.java
 *   java -cp out PriorityQueueDemo
 */
public class PriorityQueueDemo {

    public static void main(String[] args) {
        System.out.println("=== Min-heap (domyślnie) ===");
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(1);
        minHeap.offer(9);
        minHeap.offer(3);
        minHeap.offer(7);

        System.out.println("Zawartość (kolejność w kopcu): " + minHeap);
        System.out.println("peek() = " + minHeap.peek());
        System.out.print("poll() kolejno: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        System.out.println();
        System.out.println("=== Max-heap ===");
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(1);
        maxHeap.offer(9);
        maxHeap.offer(3);
        maxHeap.offer(7);

        System.out.print("poll() kolejno: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();

        System.out.println();
        System.out.println("=== Zadania z priorytetem ===");
        PriorityQueue<Task> tasks = new PriorityQueue<>();
        tasks.offer(new Task("Napisz raport", 3));
        tasks.offer(new Task("Napraw bug produkcyjny", 1));
        tasks.offer(new Task("Code review", 2));

        while (!tasks.isEmpty()) {
            System.out.println("Następne: " + tasks.poll());
        }
    }

    /** Mniejszy numer = wyższy priorytet. */
    record Task(String name, int priority) implements Comparable<Task> {
        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.priority, other.priority);
        }

        @Override
        public String toString() {
            return "[P" + priority + "] " + name;
        }
    }
}
