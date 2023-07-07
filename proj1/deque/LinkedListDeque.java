package deque;

import java.util.Iterator;
public class LinkedListDeque <T>implements Deque<T>  ,Iterable<T>{
    class Node <T>{
        private T item;
        private  Node<T> prior;
        private  Node<T> next;
        Node() {
            item = null;
            prior = next = null;
        }
        public Node(Node<T> prior,Node<T> next,T item){
            this.prior=prior;
            this.next=next;
            this.item=item;
        }
    }
     class  LinkedListDequeIterator implements Iterator<T>{
     private    Node<T> ptr;

         LinkedListDequeIterator(){
             ptr=head.next;
         }
         @Override
         public boolean hasNext() {
             return ptr!=null&&ptr!=head;
         }

         @Override
         public T next() {
            T item =  ptr.item;
            ptr = ptr.next;
            return item;
//
         }
     }
    private int size;
    private Node <T>head;
    //head.next 永远是first    head.piror 永远是last
    public LinkedListDeque(){
        size=0;
        head=new Node(null,null,null);
        head.prior=head;
        head.next=head;
    }
    public LinkedListDeque(T item){
        size=1;
        head=new Node(null,null,null);
        Node<T> temp=new Node(head,null,item);
        head.next=temp;
        head.prior=temp;
    }
    public T getRecursive(int index) {
        if (index<size){
            return getRecursive(head.next,index);
        }
        return null;
    }
    private T getRecursive(Node<T>start,int index) {
        if (index==0){
            return start.item;
        }
        return getRecursive(start.next,index-1);
    }

    @Override
    public void addFirst(T item) {
        size++;
        if (size==1){
            Node<T> temp=new Node(head,null,item);
            head.next=temp;
            head.prior=temp;
        }
        else {
            Node<T> temp = new Node(head, head.next, item);
            head.next.prior = temp;
            head.next = temp;
        }
    }

    @Override
    public void addLast(T item) {
        size++;
        if (size==1){
            Node<T> temp=new Node(head,null,item);
            head.next=temp;
            head.prior=temp;
        }
        else {
            Node<T> temp = new Node(head.prior, null, item);
            head.prior.next=temp;
           head.prior=temp;
        }
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> temp = head.next;
while (temp!=null){
    System.out.print(temp.item+" ");
    temp=temp.next;
}
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            size--;
            if (size == 0) {
                Node<T> temp = head.next;
                head = new Node<T>(null, null, null);
                head.prior = head;
                head.next = head;
                return temp.item;
            } else {
                Node<T> temp = head.next;
          head.next.next.prior=head;
                head.next=head.next.next;
          temp.prior=null;
          temp.next=null;
                return temp.item;
            }
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            size--;
            if (size == 0) {
                Node<T> temp = head.next;
                head = new Node<T>(null, null, null);
                head.prior = head;
                head.next = head;
                return temp.item;
            } else {
                Node<T> temp = head.prior;
               head.prior.prior.next=null;
              head.prior=head.prior.prior;
                temp.prior=null;
                temp.next=null;
                return temp.item;
            }
        }
        return null;
    }

    @Override
    public T get(int index) {
        if (index<size){
            Node<T> temp = head.next;
            int i=0;
            while (i!=index){
                i++;
                temp=temp.next;
            }
            return temp.item;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator() ;
    }
    public boolean equals(Object o) {
    if (o==null)
        return false;
    if (o==this)
        return true;
    if (o instanceof LinkedListDeque){
        LinkedListDeque<T>temp=(LinkedListDeque<T>) o;
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
