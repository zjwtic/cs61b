package deque;
import java.util.Iterator;

public class ArrayDeque <T> implements Deque<T> ,Iterable<T>{
    protected T[] items;
    protected int size;
    protected int nextFirst;
    protected int nextLast;

    /**
     * Creates an empty linked list deque
     */
    //nextfirst的后一个永远是第一个     nextlast的前一个永远是最后一个
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }
    public ArrayDeque(T item) {
        this();
        size = 1;
        items[1]=item;
        nextLast = 2;
    }

    protected int addOne(int index) {
        return (index + 1) % items.length;
    }
    protected int minusOne(int index) {
        return (index + items.length - 1) % items.length;
    }

    protected void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];
        int index = addOne(nextFirst);
        for (int i = 0; i < size; i++) {
            resized[i] = items[index];
            index = addOne(index);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = resized;
    }
    protected void checksize() {
        if (size == items.length) {
            resize(size * 2);
        }else {
            int len = items.length;
            if (len >= 16 && size < len / 4) {
                resize(len / 4);
            }
        }
    }
    /**
     * Adds an item of type T to the front of the deque.
     * You can assume that item is never null.
     */
    @Override
    public void addFirst(T item) {
        checksize();
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * You can assume that item is never null.
     */
    @Override
    public void addLast(T item) {
        checksize();
        items[nextLast] = item;
        nextLast = addOne(nextLast);
        size += 1;
    }

    /**
     * Returns the number of items in the deque.
     * @return
     */
    @Override
    public int size() {
        return size;
    }
    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        int index = addOne(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[index] + " ");
            index = addOne(index);
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        checksize();
        nextFirst = addOne(nextFirst);
        T item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        return item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        checksize();
        nextLast = minusOne(nextLast);
        T item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return item;
    }
    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * Must not alter the deque!
     */
    @Override
    public T get(int index) {
//        if (size <= index || index < 0) {
//            return null;
//        }
        return items[(nextFirst + 1 + index) % items.length];
    }

    /**
     * The Deque objects we’ll make are iterable (i.e. Iterable<T>)
     * so we must provide this method to return an iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    protected class ArrayDequeIterator implements Iterator<T> {
        private int ptr;
        ArrayDequeIterator() {
            ptr = addOne(nextFirst);
        }
        public boolean hasNext() {
            return ptr != nextLast;
        }
        public T next() {
            T item =  items[ptr];
            ptr = addOne(ptr);
            return item;
        }
    }

    /**
     * Returns whether or not the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents
     * (as goverened by the generic T’s equals method) in the same order.
     * (ADDED 2/12: You’ll need to use the instance of keywords for this.)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque other = (Deque) o;
        if (size != other.size()) {
            return false;
        }

        int index = addOne(nextFirst);
        for (int i = 0; i < size; i++) {
            if (!(items[index].equals(other.get(i)))) {
                return false;
            }
            index = addOne(index);
        }
        return true;
    }
    //second code
//    class ArrayDequeIterator implements Iterator<T>{
//        private int ptr;
//        ArrayDequeIterator(){
//            ptr=front;
//        }
//        @Override
//        public boolean hasNext() {
//         return ptr!=(last+1)%items.length;
//        }
//        @Override
//        public T next() {
//            T item=items[ptr];
//           ptr=(ptr+1)%items.length;
//           return item;
//        }
//    }
//   private int front;
//   private int last;
//   private int size;
//  private   T []items;
//    @SuppressWarnings("unchecked")
//   public ArrayDeque(){
//        items=(T[])new Object[8];
//        front=last=-1;
//        size=0;
//    }
//  public ArrayDeque(T item){
//        this();
//        items[0]=item;
//        size=1;
//        front=last=0;
//    }
//    public T getRecursive(int index) {
//        return getRecursive(front,index);
//    }
//   private T getRecursive(int start,int index) {
//        if (index==0)
//            return items[start];
//       return getRecursive((start+1)%items.length,index-1);
//    }
//
//    @Override
//    public void addFirst(T item) {
//        if (isEmpty()){
//            items[0]=item;
//            size=1;
//            front=last=0;
//            return;
//        }
//         checksize();
//            front = (front - 1 + items.length) % items.length;
//            items[front] = item;
//            size++;
//        }
//    @SuppressWarnings("unchecked")
//    private  void resize(int occupation){
//        T []temp=(T[]) new Object[occupation];
////        if (front<last){
////
////            for (int i = 0; i < items.length&&i<=last; i++) {
////                temp[i]=items[i];
////            }
////        }
////        else {
////            for (int i = 0; i <=last; i++) {
////                temp[i]=items[i];
////            }
////            int length=items.length-1;
////            for (int i = occupation-1; ; i--) {
////                temp[i]=items[length];
////                if (length==front){
////                    front=i;
////                    break;
////                }
////                length--;
////            }
////        }
//        int x=0;
//        for (int i=front;i!=last;i=(i+1)%items.length){
//            temp[x++]=items[i];
//
//        }
//        temp[x]=items[last];
//        front=0;
//        last=x;
//        items=temp;
//    }
//private void   checksize(){
//         if(size==items.length-1) {
//             resize(items.length * 4);
//         }
//         else if (size<items.length/4&&items.length>8){
//             resize(items.length /4);
//         }
//}
//    @Override
//    public void addLast(T item) {
//        if (isEmpty()){
//            items[0]=item;
//            size=1;
//            front=last=0;
//            return;
//        }
//        checksize();
//            last = (last + 1) % items.length;
//            items[last] = item;
//            size++;
//        }
//
//
//    @Override
//    public int size() {
//        return size;
//    }
//
//    @Override
//    public void printDeque() {
//        int i=front;
//     while (size!=0){
//         System.out.print(items[i]+" ");
//        if (i==last){
//            break;
//        }
//         i=(i+1)%items.length;
//     }
//        System.out.println();
//    }
//    @SuppressWarnings("unchecked")
//    private void clean(){
//        items=(T[])new Object[items.length];
//        front=last=-1;
//        size=0;
//    }
//    @Override
//    public T removeFirst() {
//        if (isEmpty())
//        return null;
//        if (size==1)
//        {  T temp=items[front];
//           clean();
//           return temp;
//        }
//        checksize();
//        T temp=items[front];
//        items[front]=null;
//        front=(front+1)%items.length;
//        size--;
//        return temp;
//
//    }
//
//    @Override
//    public T removeLast() {
//        if (isEmpty())
//            return null;
//        if (size==1)
//        {  T temp=items[0];
//            clean();
//            return temp;
//        }
//        checksize();
//        T temp=items[last];
//        items[last]=null;
//       last=(last-1+items.length)%items.length;
//        size--;
//        return temp;
//    }
//
//    @Override
//    public T get(int index) {
//        if (size==0){
//            return null;
//        }
//        if (index>=size){
//            return null;
//        }
//        return items[(front+index)%items.length];
//    }
//
//    @Override
//    public Iterator<T> iterator() {
//        return new ArrayDequeIterator();
//    }
//    public boolean equals(Object o) {
//        if (o==null)
//            return false;
//        if (o==this)
//            return true;
//        if (o instanceof ArrayDeque){
//            @SuppressWarnings("unchecked")
//            ArrayDeque<T>temp=(ArrayDeque<T>) o;
//            if (size==temp.size()) {
//                for (int i = 0; i < size; i++) {
//                    if (get(i)!=temp.get(i))
//                        return false;
//                }
//                return true;
//            }
//
//        }
//        return false;
//    }
}
