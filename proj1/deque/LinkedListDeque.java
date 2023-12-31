package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    class Node<T> {
        private T item;
        private Node<T> prior;
        private Node<T> next;

        Node() {
            item = null;
            prior = next = null;
        }

        public Node(Node<T> prior, Node<T> next, T item) {
            this.prior = prior;
            this.next = next;
            this.item = item;
        }
    }

    class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> ptr;

        LinkedListDequeIterator() {
            ptr = head.next;
        }

        @Override
        public boolean hasNext() {
            return ptr != head;
        }

        @Override
        public T next() {
            T item = ptr.item;
            ptr = ptr.next;
            return item;
        }
    }

    private int size;
    private Node<T> head;

    //head.next 永远是first    head.piror 永远是last
    public LinkedListDeque() {
        size = 0;
        head = new Node(null, null, null);
        head.prior = head;
        head.next = head;
    }

    public LinkedListDeque(T item) {
        this();
        size = 1;
        Node<T> temp = new Node(head, head, item);
        head.next = temp;
        head.prior = temp;
    }

    public T getRecursive(int index) {
        if (index < size) {
            return getRecursive(head.next, index);
        }
        return null;
    }

    private T getRecursive(Node<T> start, int index) {
        if (index == 0) {
            return start.item;
        }
        return getRecursive(start.next, index - 1);
    }

    @Override
    public void addFirst(T item) {
             size++;
            Node<T> temp = new Node(head, head.next, item);
            head.next.prior = temp;
            head.next = temp;
        }

    @Override
    public void addLast(T item) {
             size++;
            Node<T> temp = new Node(head.prior, head, item);
            head.prior.next = temp;
            head.prior = temp;
        }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> temp = head.next;
        while (temp != head) {
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            size--;
                Node<T> temp = head.next;
                T result=temp.item;
                temp.next.prior = head;
                head.next = temp.next;
                 free(temp);
                return result;
            }
        return null;
    }
     private void free(Node<T> item) {
    item.next=null;
    item.prior=null;
    item.item=null;
        }
    @Override
    public T removeLast() {
        if (!isEmpty()) {
            size--;
                Node<T> temp = head.prior;
                T result=temp.item;
               temp.prior.next = head;
                head.prior = temp.prior;
                free(temp);
                return result;
            }
        return null;
    }

    @Override
    public T get(int index) {
        if (index < size) {
            Node<T> temp = head.next;
            int i = 0;
            while (i != index) {
                i++;
                temp = temp.next;
            }
            return temp.item;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque other = (Deque) o;
        if (size != other.size()) {
            return false;
        }
        Node p = head.next;
        for (int i = 0; i < size; i++) {
            if (!p.item.equals(other.get(i))) {
                return false;
            }
            p = p.next;
        }
        return true;
    }
// two sentinels methods

//    class Node <T>{
//        private T item;
//        private  Node<T> prior;
//        private  Node<T> next;
//        public Node(Node<T> prior,Node<T> next){
//            this.prior=prior;
//            this.next=next;
//        }
//        public Node(T item){
//            this.prior=null;
//            this.next=null;
//            this.item=item;
//        }
//    }
//
//    class LinkedListDequeIterator implements Iterator<T>{
//
//        private Node ptr;
//        LinkedListDequeIterator() {
//            ptr = sentFront.next;
//        }
//        public boolean hasNext() {
//            return (ptr != sentBack);
//        }
//        public T next() {
//            T item = (T) ptr.item;
//            ptr = ptr.next;
//            return item;
//        }
//    }
//    public LinkedListDeque(){
//     size=0;
//     sentFront=new Node<T>(null,null);
//     sentBack=new Node<T>(null,null);
//     sentFront.next=sentBack;
//     sentBack.prior=sentFront;
//    }
//    public LinkedListDeque(T item){
//        size=1;
//        sentFront=new Node<T>(null,null);
//        sentBack=new Node<T>(null,null);
//        Node<T> temp=new Node<T>(item);
//        sentFront.next=temp;
//        temp.prior=sentFront;
//        temp.next=sentBack;
//        sentBack.prior=temp;
//
//    }
//
//    public T getRecursive(int index){
//        if (index>=size)
//            return null;
//            return  getRecursive(sentFront.next,index);
//
//    }
//    private T getRecursive(Node<T> temp,int index){
//        if (index==0){
//            return temp.item;
//        }
//        else return getRecursive(temp.next,index-1);
//    }
//    //the first item is sentFront.next
//    //the last item is sentBack.piror
//    public void addFirst(T item){
//            size++;
//        if (size==1){
//            firstadd(item);
//            return;
//        }
//        Node<T> temp=new Node<T>(item);
//                sentFront.next.prior = temp;
//                temp.next = sentFront.next;
//                temp.prior = sentFront;
//                sentFront.next = temp;
//
//    }
//    private void firstadd(T item){
//        Node<T> temp=new Node<T>(item);
//        sentFront.next=temp;
//        temp.prior=sentFront;
//        temp.next=sentBack;
//        sentBack.prior=temp;
//    }
//    public void addLast(T item){
//        size++;
//        if (size==1){
//            firstadd(item);
//            return;
//        }
//        Node<T> temp=new Node<T>(item);
//        sentBack.prior.next=temp;
//        temp.prior=sentBack.prior;
//        temp.next=sentBack;
//        sentBack.prior=temp;
//    }
//    public boolean isEmpty(){
//         return sentFront.next==sentBack;
//    }
//    public int size(){
// return size;
//    }
//    public void printDeque(){
//Node<T> temp=sentFront.next;
//while (temp!=sentBack){
//    System.out.print(temp.item+" ");
//    temp=temp.next;
//}
//        System.out.println();
//    }
//    public T removeFirst(){
//        if (isEmpty()){
//            return null;
//        }
//        size--;
//        if (size==0){
//            return   onlyoneremove();
//        }
//      Node<T> temp=sentFront.next;
//        sentFront.next.next.prior=sentFront;
//      sentFront.next=sentFront.next.next;
//      temp.next=null;
//      temp.prior=null;
//      return temp.item;
//    }
//    private  T  onlyoneremove(){
//        Node<T> temp=sentFront.next;
//        sentFront.next=sentBack;
//        sentBack.prior=sentFront;
//        temp.next=null;
//        temp.prior=null;
//        return temp.item;
//    }
//
//    public T removeLast(){
//        if (isEmpty()){
//            return null;
//        }
//        size--;
//if (size==0){
//  return   onlyoneremove();
//}
//        Node<T> temp=sentBack.prior;
//        sentBack.prior.prior.next=sentBack;
//        sentBack.prior=sentBack.prior.prior;
//        temp.next=null;
//        temp.prior=null;
//        return temp.item;
//    }
//    public T get(int index){
//          if (index>=size)
//              return null;
//           int i=0;
//           Node<T> temp=sentFront.next;
//while (i!=index){
//    temp=temp.next;
//    i++;
//}
//return temp.item;
//    }
//
//    @Override
//    public Iterator<T> iterator() {
//        return new LinkedListDequeIterator();
//    }
//
//    public boolean equals(Object o){
//        if (o == null) {
//        return false;
//    }
//        if (o == this) {
//            return true;
//        }
//        if (!(o instanceof LinkedListDeque)) {
//            return false;
//        }
//        LinkedListDeque<?> temp= (LinkedListDeque<?>) o;
//        if (temp.size() != size) {
//            return false;
//        }
//        for (int i = 0; i < size; i++) {
//            if (temp.get(i) != get(i)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//    private Node<T> sentBack;
//    private   Node<T> sentFront;
//    private int  size;



}
