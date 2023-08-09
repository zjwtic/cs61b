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

    public boolean contain(String blobfilename,String bid){
        String id=get(blobfilename);
        if (id==null){
            return false;
        }
        if (!id.equals(bid)){
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
public String remove(String filename){
    return storedareas.remove(filename);
}
public boolean ismodifed(String filename ,String bid){
        return contain(filename)&&!contain(filename,bid);
    }
    public  void add(Blob blob){
            storedareas.put(blob.getFilename(),blob.getBid());
    }
    public void add(String filename,String bid){
        storedareas.put(filename,bid);
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


