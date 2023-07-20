package bstmap;


import java.util.Iterator;
import java.util.LinkedList;
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
   public Entry(K key ,V value ){
        this.key=key;
        this.value=value;
    }
}

//greater iterator
private class BSTIterator implements Iterator<K>{
     private LinkedList<Entry> list;
       public   BSTIterator(){
             list=new LinkedList<>();
        list.addLast(tree);
         }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public K next() {
           Entry temp=list.removeFirst();
           list.addLast(temp.left);
           list.addLast(temp.right);
           return temp.key;
    }
}
//my iterator
//private class BSTIterator implements Iterator<K>{
//       private Object[]items;
//         Entry ptr;
//         int num,total;
//         BSTIterator(){
//             ptr=tree;
//             num=0;
//             total=0;
//            items=new Object[size];
//            init();
//
//         }
//         private void init(){
//            init(ptr);
//         }
//         private void init(Entry entry){
//             if (entry==null){
//                 return;
//             }
//             init(entry.left);
//             items[num]=entry.key;
//             num+=1;
//             init(entry.right);
//         }
//
//    @Override
//    public boolean hasNext() {
//        return total!=num;
//    }
//
//    @Override
//    public K next() {
//             K item=(K)items[total];
//             total+=1;
//             return item;
//    }
//}
    @Override
    public void clear() {
      tree=null;
      size=0;
    }

    @Override
    public boolean containsKey(K key) {
    return getentry(tree,key)!=null;
    }

    @Override
    public V get(K key) {
    Entry temp=getentry(tree,key);
      return temp==null?null:temp.value;
    }
    private Entry getentry(Entry entry,K key){
        if (entry==null)
            return null;
        int cmp=key.compareTo(entry.key);
        if (cmp<0){
            return getentry(entry.left,key);
        }
        else if (cmp>0){
           return getentry(entry.right,key);
        }
        else {
            return entry;
        }
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
        return new Entry(key,value);
    }
        int cmp=key.compareTo(entry.key);
       if (cmp==0){
               entry.value=value;
           }
       else if (cmp<0){
          entry.left= put(entry.left,key,value);
        }
        else if (cmp>0){
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
            set.add(entry.key);
            keySet(set,entry.left);
            keySet(set,entry.right);
            return set;
   }
//greater remove
        private Entry getmix(Entry temp){
           if (temp.left==null){
               return temp;
           }
       return getmix(temp.left);
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
        @Override
    public V remove(K key, V value) {
        if (!containsKey(key)||!get(key).equals(value))
            return null;
        V item=value;
       tree= remove(tree,key);
        size-=1;
        return item;
    }
        private Entry remove(Entry entry,K key){
         if (entry==null){
          return null;
          }
            int cmp=key.compareTo(entry.key);
         if (cmp<0){
                entry.left= remove(entry.left,key);
            }
         else   if (cmp>0){
                entry.right=remove(entry.right,key);
            }
        else {
            if (entry.right==null){
               return entry.left;
            }
           if (entry.left==null){
                return entry.right;
            }
           Entry originentry=entry;
            entry=getmix(entry.right);
         entry.left=originentry.left;
        entry.right=remove(originentry.right,entry.key);
        }

        return entry;
    }

        //my remove
//    @Override
//    public V remove(K key) {
//        if (!containsKey(key))
//            return null;
//        V item=get(key);
//       tree= remove(tree,key);
//         size-=1;
//         return item;
//    }
//    private Entry remove(Entry entry,K key){
//        if (key.compareTo(entry.key)==0){
//            if (entry.right==null){
//               return entry.left;
//            }
//           else if (entry.left==null){
//                return entry.right;
//            }
//            Entry mix=getmix(entry.right);
//            Entry temp=entry;
//            entry=entry.right;
//            if (entry!=mix) {
//                while (entry.left != mix) {
//                    entry = entry.left;
//                }
//                entry.left = mix.right;
//                mix.left = temp.left;
//                mix.right = temp.right;
//            }
//            else {
//                mix.left = temp.left;
//            }
//            return mix;
//
//        }
//        else if (key.compareTo(entry.key)<0){
//            entry.left= remove(entry.left,key);
//        }
//        else if (key.compareTo(entry.key)>0){
//            entry.right=remove(entry.right,key);
//        }
//        return entry;
//    }
//    private Entry getmix(Entry temp){
//             Entry entry=temp;
//              while (entry.left!=null){
//                 entry= entry.left;
//              }
//              return entry;
//    }
//    @Override
//    public V remove(K key, V value) {
//        if (!containsKey(key)||!get(key).equals(value))
//            return null;
//        V item=value;
//       tree= remove(tree,key);
//        size-=1;
//        return item;
//    }

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
    System.out.print(entry.key+" ");
    printInOrder(entry.left);
    printInOrder(entry.right);
    }
}
