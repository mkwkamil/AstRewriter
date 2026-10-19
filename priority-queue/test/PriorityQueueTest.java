import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Lekkie testy bez JUnit — uruchamiane przez main().
 *
 *   javac -d out src/*.java test/*.java
 *   java -cp out PriorityQueueTest
 */
public class PriorityQueueTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        testMinHeapOrder();
        testMaxHeapOrder();
        testPeekDoesNotRemove();
        testEmptyThrows();
        testNullRejected();
        testGrowth();
        testClear();
        testCustomComparable();

        System.out.println();
        System.out.println("Wynik: " + passed + " OK, " + failed + " FAIL");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testMinHeapOrder() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(4);
        pq.offer(1);
        pq.offer(3);
        pq.offer(2);
        assertEquals(1, pq.poll(), "min-heap poll #1");
        assertEquals(2, pq.poll(), "min-heap poll #2");
        assertEquals(3, pq.poll(), "min-heap poll #3");
        assertEquals(4, pq.poll(), "min-heap poll #4");
        assertTrue(pq.isEmpty(), "min-heap empty");
    }

    private static void testMaxHeapOrder() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        pq.offer(4);
        pq.offer(1);
        pq.offer(3);
        pq.offer(2);
        assertEquals(4, pq.poll(), "max-heap poll #1");
        assertEquals(3, pq.poll(), "max-heap poll #2");
        assertEquals(2, pq.poll(), "max-heap poll #3");
        assertEquals(1, pq.poll(), "max-heap poll #4");
    }

    private static void testPeekDoesNotRemove() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(10);
        pq.offer(5);
        assertEquals(5, pq.peek(), "peek value");
        assertEquals(2, pq.size(), "peek size unchanged");
        assertEquals(5, pq.poll(), "poll after peek");
    }

    private static void testEmptyThrows() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertThrows(NoSuchElementException.class, pq::poll, "poll empty");
        assertThrows(NoSuchElementException.class, pq::peek, "peek empty");
    }

    private static void testNullRejected() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertThrows(NullPointerException.class, () -> pq.offer(null), "null offer");
    }

    private static void testGrowth() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(2);
        for (int i = 100; i >= 0; i--) {
            pq.offer(i);
        }
        assertEquals(101, pq.size(), "growth size");
        assertEquals(0, pq.poll(), "growth min");
    }

    private static void testClear() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(1);
        pq.offer(2);
        pq.clear();
        assertTrue(pq.isEmpty(), "clear empty");
        assertEquals(0, pq.size(), "clear size");
    }

    private static void testCustomComparable() {
        record Person(String name, int age) implements Comparable<Person> {
            @Override
            public int compareTo(Person o) {
                return Integer.compare(age, o.age);
            }
        }

        PriorityQueue<Person> pq = new PriorityQueue<>();
        pq.offer(new Person("Anna", 30));
        pq.offer(new Person("Bartek", 20));
        pq.offer(new Person("Celina", 25));
        assertEquals("Bartek", pq.poll().name(), "youngest first");
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            pass(name);
        } else {
            fail(name + " — oczekiwano " + expected + ", otrzymano " + actual);
        }
    }

    private static void assertTrue(boolean condition, String name) {
        if (condition) {
            pass(name);
        } else {
            fail(name + " — oczekiwano true");
        }
    }

    private static void assertThrows(Class<? extends Throwable> type, Runnable action, String name) {
        try {
            action.run();
            fail(name + " — oczekiwano " + type.getSimpleName());
        } catch (Throwable t) {
            if (type.isInstance(t)) {
                pass(name);
            } else {
                fail(name + " — oczekiwano " + type.getSimpleName() + ", złapano " + t);
            }
        }
    }

    private static void pass(String name) {
        passed++;
        System.out.println("[OK] " + name);
    }

    private static void fail(String name) {
        failed++;
        System.out.println("[FAIL] " + name);
    }
}
