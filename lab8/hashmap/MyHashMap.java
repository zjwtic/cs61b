package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K , V> implements Map61B<K, V> {

    @Override
    public void clear() {
        size =0;
        buckets=createTable(DEFAULT_INITIAL_SIZE);
    }

    @Override
    public boolean containsKey(K key) {
       return getnode(key)!=null;
    }
private   Node getnode(K key){
        if (size==0)
            return null;
        int index=getindex(key);
    for (Node node : buckets[index]) {
        if (node.key.equals(key)){
            return node;
        }
    }
    return null;
}

    @Override
    public V get(K key) {
        Node node=getnode(key);
        return node==null?null:node.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        hasreachedmaxload();
        int index=getindex(key);
        Node node=getnode(key);
        if (node!=null){
        node.value=value;
        return;
        }
    buckets[index].add(createNode(key,value));
        size++;
    }


    private void  hasreachedmaxload(){
        if ((double)(size/buckets.length)>=loadfactor){
            resize(buckets.length*2);
        }
    }
    private void resize(int capacity){
        Collection<Node>[] resized=createTable(capacity);
        for (int i = 0; i < buckets.length; i++) {
            for (Node node : buckets[i]) {
                int index=getindex(node.key,resized);
                resized[index].add(node);
            }
        }
        buckets=resized;
    }
    private  int getindex(K key){
        return getindex(key,buckets);
    }
   private  int getindex(K key,Collection<Node>[] table){
      return Math.floorMod(key.hashCode(), table.length);
   }
    @Override
    public Set<K> keySet() {
       Set<K> set=new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            for (Node node : buckets[i]) {
                 set.add(node.key);
            }
        }
        return set;
    }

    @Override
    public V remove(K key) {
        Node node=getnode(key);
   if (node==null){
       return null;
   }
   V value=node.value;
    delete(node);
   return value;
    }
   private void delete(Node node){
       int index=getindex(node.key);
       buckets[index].remove(node);
       size--;
    }


    @Override
    public V remove(K key, V value) {
        Node node=getnode(key);
        if (node==null){
            return null;
        }
        if (!node.value.equals(value)){
            return null;
        }
        delete(node);
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    private int size;
    private final double loadfactor;
    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE,DEFAULT_MAX_LOAD_FACTOR);
    }
    public MyHashMap(int initialSize) {
        this(initialSize,DEFAULT_MAX_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size=0;
        loadfactor=maxLoad;
        buckets=createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table=new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i]=createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
// 别人的iterator 感觉过于复杂，但还是值得学习
//    public Iterator<K> iterator() {
//        return new MyHashMapIterator();
//    }
//
//private class MyHashMapIterator implements Iterator<K> {
//    private final Iterator<Node> nodeIterator = new MyHashMapNodeIterator();
//
//    public boolean hasNext() {
//        return nodeIterator.hasNext();
//    }
//
//    public K next() {
//        return nodeIterator.next().key;
//    }
//}
//
//private class MyHashMapNodeIterator implements Iterator<Node> {
//    private final Iterator<Collection<Node>> bucketsIterator = Arrays.stream(buckets).iterator();//获取buckets【0】iterator
//                                                                                                 他的next是下一个iteator（bucket【1】）
//    private Iterator<Node> currentBucketIterator;
//    private int nodesLeft = size;
//
//    public boolean hasNext() {
//        return nodesLeft > 0;
//    }
//
//    public Node next() {
//        if (currentBucketIterator == null || !currentBucketIterator.hasNext()) {
//            Collection<Node> currentBucket = bucketsIterator.next();
//            while (currentBucket.size() == 0) {
//                currentBucket = bucketsIterator.next();
//            }
//            currentBucketIterator = currentBucket.iterator();
//        }
//        nodesLeft -= 1;
//        return currentBucketIterator.next();
//    }
//}
//}