package gitlet;

import java.io.Serializable;

import java.util.Set;
import java.util.TreeMap;

public class Stage implements Serializable {

        private TreeMap<String,String> storedareas=new TreeMap<>();
public boolean contain(Blob blob){
    String id=get(blob.getFilename());
    if (id==null){
        return false;
    }
    if (!id.equals(blob.getBid())){
        return false;
    }
    return true;
}
    public boolean contain(String  filename){
        String id=get(filename);
        return id!=null;
    }
public  String remove(Blob blob){
   return storedareas.remove(blob.getFilename());
}
    public  void add(Blob blob){
            storedareas.put(blob.getFilename(),blob.getBid());
    }
    public boolean isEmpty(){
    return storedareas.isEmpty();
    }
    public String get(String filename){
        return storedareas.get(filename);
    }
    public Set<String> KeySet(){
        return storedareas.keySet();
    }

    public TreeMap<String, String> getStoredareas() {
        return storedareas;
    }

    public void setStoredareas(TreeMap<String, String> storedareas) {
        this.storedareas = storedareas;
    }

}


