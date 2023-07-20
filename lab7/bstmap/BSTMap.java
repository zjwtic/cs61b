package bstmap;


import java.net.IDN;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K ,V>{
private  int size;
private Entry tree;
private class Entry {
    K key;
    V value;
    Entry left;
    Entry right;
   public Entry(K key ,V value ,Entry left,Entry right){
        this.key=key;
        this.value=value;
        this.left=left;
        this.right=right;
    }
}
private class BSTIterator implements Iterator<K>{
       private Object[]items;
         Entry ptr;
         int num,total;
         BSTIterator(){
             ptr=tree;
             num=0;
             total=0;
            items=new Object[size];
            init();

         }
         private void init(){
            init(ptr);
         }
         private void init(Entry entry){
             if (entry==null){
                 return;
             }
             init(entry.left);
             items[num]=entry.key;
             num+=1;
             init(entry.right);
         }

    @Override
    public boolean hasNext() {
        return total!=num;
    }

    @Override
    public K next() {
             K item=(K)items[total];
             total+=1;
             return item;
    }
}
    @Override
    public void clear() {
      tree=null;
      size=0;
    }

    @Override
    public boolean containsKey(K key) {
    return containsKey(tree,key);
    }
    private boolean containsKey(Entry entry,K key) {
        if (entry==null)
            return false;
        if (key.compareTo(entry.key)<0){
            return containsKey(entry.left,key);
        }
        else if (key.compareTo(entry.key)>0){
            return containsKey(entry.right,key);
        }
        return true;
    }

    @Override
    public V get(K key) {
      return get(tree,key);
    }
    private V get(Entry entry,K key){
        if (entry==null)
            return null;
        if (key.compareTo(entry.key)<0){
            return get(entry.left,key);
        }
        else if (key.compareTo(entry.key)>0){
           return get(entry.right,key);
        }
        return entry.value;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
      tree=put(tree,key,value);
    }
    private  Entry  put(Entry entry,K key,V value){
    if (entry==null){
        size+=1;
        return new Entry(key,value,null,null);
    }
       if (key.compareTo(entry.key)==0){
               entry.value=value;
               return entry;
           }
       else if (key.compareTo(entry.key)<0){
          entry.left= put(entry.left,key,value);
        }
        else if (key.compareTo(entry.key)>0){
           entry.right=put(entry.right,key,value);
        }
        return entry;
    }


    @Override
    public Set<K> keySet() {
        Set<K>set=new TreeSet<>();
        set=keySet(set,tree);
        return set;
    }
   private Set<K> keySet(Set<K> set,Entry entry){
            if (entry==null){
                return null;
            }
            keySet(set,entry.left);
            set.add(entry.key);
            keySet(set,entry.right);
            return set;
   }

    @Override
    public V remove(K key) {
        if (!containsKey(key))
            return null;
        V item=get(key);
       tree= remove(tree,key);
         size-=1;
         return item;
    }
    private Entry remove(Entry entry,K key){
        if (key.compareTo(entry.key)==0){
            if (entry.right==null){
               return entry.left;
            }
           else if (entry.left==null){
                return entry.right;
            }
            Entry mix=getmix(entry.right);
            Entry temp=entry;
            entry=entry.right;
            if (entry!=mix) {
                while (entry.left != mix) {
                    entry = entry.left;
                }
                entry.left = mix.right;
                mix.left = temp.left;
                mix.right = temp.right;
            }
            else {
                mix.left = temp.left;
            }
            return mix;

        }
        else if (key.compareTo(entry.key)<0){
            entry.left= remove(entry.left,key);
        }
        else if (key.compareTo(entry.key)>0){
            entry.right=remove(entry.right,key);
        }
        return entry;
    }
    private Entry getmix(Entry temp){
             Entry entry=temp;
              while (entry.left!=null){
                 entry= entry.left;
              }
              return entry;
    }
    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)||!get(key).equals(value))
            return null;
        V item=value;
       tree= remove(tree,key);
        size-=1;
        return item;
    }

    @Override
    public Iterator<K> iterator() {
       return new BSTIterator();
    }

    public void printInOrder(){
           printInOrder(tree);
        System.out.println();
    }
    private void printInOrder(Entry entry){
    if (entry==null){
        return;
    }
    printInOrder(entry.left);
    System.out.print(entry.key+" ");
    printInOrder(entry.right);
    }
}
