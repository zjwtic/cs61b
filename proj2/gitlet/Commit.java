package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static gitlet.Utils.*;

/*Represents a gitlet commit object.
   TODO: It's a good idea to give a description here of what else this Class
  does at a high level.
  Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time,
   creating a new commit. The commit is said to be tracking the saved files. By default,
    each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
    it will keep versions of files exactly as they are, and not update them.
     A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
      in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
       A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
        Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal
         by the rm command (below).

The bottom line: By default a commit has the same file contents as its parent.
Files staged for addition and removal are the updates to the commit.
 Of course, the date (and likely the mesage) will also different from the parent.
   @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** parent commit*/
   private List<String> parents ;
   private Date  currentDate;
   private  String timestamp;
private TreeMap<String,String>tree ;
     private String Cid;
   public Commit(){

   }
    public  Commit(String message,List<String> parents,Date date,TreeMap<String,String>tree){
           this.message=message;
           this.parents=parents;
           this.tree=tree;
           this.currentDate=date;
           timestamp=dateToTimeStamp(date);
         Cid=sha1(timestamp,message,parents.toString(),tree.toString());
    }
    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TreeMap<String, String> getTree() {
        return tree;
    }

    public void setTree(TreeMap<String, String> tree) {
        this.tree = tree;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }
    public boolean contain(Blob blob){
       String filename=blob.getFilename();
       String commitbid=getbid(filename);
       if (commitbid==null){
           return false;
       }
       if (!commitbid.equals(blob.getBid())){
           return false;
       }
       return true;
    }
    public boolean contain(String filename ,String bid){
        String commitbid=getbid(filename);
        if (commitbid==null){
            return false;
        }
        if (!commitbid.equals(bid)){
            return false;
        }
        return true;
    }
    public boolean ismodifed(String filename ,String bid){
       return contain(filename)&&!contain(filename,bid);
    }
    public boolean contain(String filename){
      return getbid(filename)!=null;
    }
   public Set<String> getAllfile(){
       return tree.keySet();
    }
public  String getbid(String filename){
       return tree.get(filename);
}


   /* TODO: fill in the rest of this class. */
}
