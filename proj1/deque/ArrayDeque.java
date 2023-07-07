package deque;
import java.util.Iterator;

public class ArrayDeque <T> implements Deque<T> ,Iterable<T>{
    class ArrayDequeIterator implements Iterator<T>{
        private int ptr;
        ArrayDequeIterator(){
            ptr=front;
        }
        @Override
        public boolean hasNext() {
         return ptr!=(last+1)%items.length;
        }
        @Override
        public T next() {
            T item=items[ptr];
           ptr=(ptr+1)%items.length;
           return item;
        }
    }
    int front;
    int last;
    int size;
    T []items;
    ArrayDeque(){
        items=(T[])new Object[8];
        front=last=-1;
        size=0;
    }
    ArrayDeque(T item){
        this();
        items[0]=item;
        size=1;
        front=last=0;
    }
    public T getRecursive(int index) {
        return getRecursive(front,index);
    }
   private T getRecursive(int start,int index) {
        if (index==0)
            return items[start];
       return getRecursive((start+1)%items.length,index-1);
    }

    @Override
    public void addFirst(T item) {
        if (isEmpty()){
            items[0]=item;
            size=1;
            front=last=0;
            return;
        }
         checksize();
            front = (front - 1 + items.length) % items.length;
            items[front] = item;
            size++;
        }
    private  void resize(int occupation){
        T []temp=(T[]) new Object[occupation];
//        if (front<last){
//
//            for (int i = 0; i < items.length&&i<=last; i++) {
//                temp[i]=items[i];
//            }
//        }
//        else {
//            for (int i = 0; i <=last; i++) {
//                temp[i]=items[i];
//            }
//            int length=items.length-1;
//            for (int i = occupation-1; ; i--) {
//                temp[i]=items[length];
//                if (length==front){
//                    front=i;
//                    break;
//                }
//                length--;
//            }
//        }
        int x=0;
        for (int i=front;i!=last;i=(i+1)%items.length){
            temp[x++]=items[i];

        }
        temp[x]=items[last];
        front=0;
        last=x;
        items=temp;
    }
private void   checksize(){
         if(size==items.length-1) {
             resize(items.length * 4);
         }
         else if (size<items.length/4&&items.length>8){
             resize(items.length /4);
         }
}
    @Override
    public void addLast(T item) {
        if (isEmpty()){
            items[0]=item;
            size=1;
            front=last=0;
            return;
        }
        checksize();
            last = (last + 1) % items.length;
            items[last] = item;
            size++;
        }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int i=front;
     while (size!=0){
         System.out.print(items[i]+" ");
        if (i==last){
            break;
        }
         i=(i+1)%items.length;
     }
        System.out.println();
    }

    private void clean(){
        items=(T[])new Object[items.length];
        front=last=-1;
        size=0;
    }
    @Override
    public T removeFirst() {
        if (isEmpty())
        return null;
        if (size==1)
        {  T temp=items[front];
           clean();
           return temp;
        }
        checksize();
        T temp=items[front];
        items[front]=null;
        front=(front+1)%items.length;
        size--;
        return temp;

    }

    @Override
    public T removeLast() {
        if (isEmpty())
            return null;
        if (size==1)
        {  T temp=items[0];
            clean();
            return temp;
        }
        checksize();
        T temp=items[last];
        items[last]=null;
       last=(last-1+items.length)%items.length;
        size--;
        return temp;
    }

    @Override
    public T get(int index) {
        if (size==0){
            return null;
        }
        if (index>=size){
            return null;
        }
        return items[(front+index)%items.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    public boolean equals(Object o) {
        if (o==null)
            return false;
        if (o==this)
            return true;
        if (o instanceof ArrayDeque){
            ArrayDeque<T>temp=(ArrayDeque<T>) o;
            if (size==temp.size()) {
                for (int i = 0; i < size; i++) {
                    if (get(i)!=temp.get(i))
                        return false;
                }
                return true;
            }

        }
        return false;
    }
}
