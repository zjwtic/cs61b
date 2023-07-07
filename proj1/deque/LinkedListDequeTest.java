package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double> lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);
        double a = 3.14159;
        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
        assertEquals("string", s);
        assertEquals(a, d, 0.0);
        assertEquals(true, b);


    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }

    @Test
    public void nonEmptyInstantiationTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>(1);

        assertFalse("Should not be empty", lld1.isEmpty());
        assertEquals("Should have size 1", 1, lld1.size());
    }

    @Test
    public void getTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertEquals("Should have same value", i, (int) lld1.get(i));
        }

        assertNull("Should be null when index out of bound", lld1.get(1000));
    }

    @Test
    public void getRecursiveTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertEquals("Should have same value", i, (int) lld1.getRecursive(i));
        }
    }

    @Test
    public void iteratorTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        int i = 0;
        for (int item : lld1) {
            assertEquals("Should have same value", i, item);
            i += 1;
        }
    }

    @Test
    public void equalsTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();

        lld1.addLast(0);
        lld2.addLast(0);
        assertEquals(lld1, lld2);

        lld1.addLast(1);
        assertNotEquals(lld1, lld2);

        lld2.addLast(2);
        assertNotEquals(lld1, lld2);
    }

    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();

        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                linkedListDeque.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                linkedListDeque.addLast(randVal);
            } else if (linkedListDeque.size() == 0) {
                assertTrue(linkedListDeque.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(linkedListDeque.size() > 0);
            } else if (operationNumber == 3) {
                linkedListDeque.removeFirst();
            } else if (operationNumber == 4) {
                linkedListDeque.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, linkedListDeque.size());
                linkedListDeque.get(randIndex);
            }
        }

    }
}
