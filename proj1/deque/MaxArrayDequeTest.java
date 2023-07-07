package deque;

import org.junit.Test;


import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MaxArrayDequeTest {
    @Test
    public void Test0() {
        Comparator<Integer> a=new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        };
        Comparator<Integer> b=new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        };

        MaxArrayDeque<Integer>arrayDeque=new MaxArrayDeque<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });

        arrayDeque.addFirst(1);
        arrayDeque.addFirst(2);
        arrayDeque.addFirst(3);
        arrayDeque.addFirst(4);
        arrayDeque.addFirst(5);
       assertEquals(arrayDeque.max(a),arrayDeque.max());
       assertEquals((Integer) 5,arrayDeque.max());
        arrayDeque.addLast(6);
        assertEquals((Integer) 6,arrayDeque.max());
        assertFalse(arrayDeque.max(b).equals(arrayDeque.max()));

    }

    @Test
    public void Test1() {
        Comparator<String> a=new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String>arrayDeque=new MaxArrayDeque<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        arrayDeque.addFirst("1");
        arrayDeque.addFirst("3");
        arrayDeque.addFirst("4");
        arrayDeque.addFirst("9");
        assertEquals(arrayDeque.max(a),arrayDeque.max());
        assertEquals("9",arrayDeque.max());

    }
    @Test
    public void maxWithoutComparatorTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntComparator());

        for (int i = 0; i < 5; i++) {
            mad.addLast(i);
        }

        assertEquals((Integer) 4, mad.max());
    }

    @Test
    public void maxWithComparatorTest() {
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(new StringComparator());

        mad.addLast("Java is good!");
        mad.addLast("java is good");

        assertEquals("java is good", mad.max());
        assertEquals("Java is good!", mad.max(new StringLengthComparator()));
    }

    private static class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            return i1 - i2;
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            int l1 = s1.length();
            int l2 = s2.length();

            for (int i = 0; i < Math.min(l1, l2); i++) {
                int s1Char = s1.charAt(i);
                int s2Char = s2.charAt(i);

                if (s1Char != s2Char) {
                    return s1Char - s2Char;
                }
            }

            if (l1 != l2) {
                return l1 - l2;
            }
            return 0;
        }
    }

    private static class StringLengthComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }
}