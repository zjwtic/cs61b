package gitlet;

import java.io.File;
import java.util.*;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * stored areas
     */
    public static final File stageaddareas = join(GITLET_DIR, "addareas"); //store the lastest commit has not add ones
    public static final File stagermareas = join(GITLET_DIR, "rmareas"); //store the lastest commit has not rm ones
    /**
     * commit areas
     */
    public static final File objects = join(GITLET_DIR, "objects");
    public static final File commitarea = join(objects, "Commits");
    /**
     * blob areas
     */
    public static final File blobarea = join(objects, "Blobs");
    /**
     * head master pointer
     **/
    public static final File head = join(GITLET_DIR, "HEAD");
    public static final File refs = join(GITLET_DIR, "refs");
    public static final File heads = join(refs, "heads");
    public static final File master = join(heads, "master");
    public static Stage addareas;
    public static Stage rmareas;
    public static TreeSet<Blob> workingdictory;
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }

        GITLET_DIR.mkdir();
        objects.mkdir();
        commitarea.mkdir();
        blobarea.mkdir();
        refs.mkdir();
        heads.mkdir();
        writeObject(stageaddareas,new Stage());
        writeObject(stagermareas,new Stage());
        writeContents(head,"refs\\heads\\master");
        Commit commititem = new Commit("initial commit", new ArrayList<>(), new Date(0), new TreeMap<>());
        commititem.commit();
        writeContents(master,commititem.getCid());
    }

    /*
    Adds a copy of the file as it currently exists to the staging area (see the description of the commit command).
    For this reason, adding a file is also called staging the file for addition.
    Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
    The staging area should be somewhere in .gitlet.
    If the current working version of the file is identical to the version in the current commit,
     do not stage it to be added, and remove it from the staging area if it is already there (as can happen when a file is changed,
     added, and then changed back to it’s original version).
     The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     */
//simply put  we need add something that lastest commit didnt have
    public static void add(String filename) {
        File addfile=join(CWD,filename);
        if (!addfile.isFile()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob newblob=new Blob(filename);
// check If the current working version of the file is identical to the version in the current commit,
//     do not stage it to be added, and remove it from the staging area if it is already there
        Commit currentcommit = getheadpoint();
        if (currentcommit.contain(newblob)) {
            rmareas=readObject(stagermareas,Stage.class);
            if (rmareas.contain(newblob)){
                rmareas.remove(newblob);
                writeObject(stagermareas,rmareas);
            }
            return;
        }
        addareas=readObject(stageaddareas,Stage.class);
            if (addareas.contain(newblob)) {
                return;
            }
         addareas.add(newblob);
        writeObject(stageaddareas,addareas);
        writeObject(join(blobarea,newblob.getBid()),newblob);  //update blob
    }
//Unstage the file if it is currently staged for addition.
// If the file is tracked in the current commit,
// stage it for removal and remove the file from the working directory if the user
// has not already done so (do not remove it unless it is tracked in the current commit)
    public static void rm(String filename) {
        File removefile=join(CWD,filename);
        if (!removefile.isFile()) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        Blob newblob=new Blob(filename);
        addareas=readObject(stageaddareas,Stage.class);
        if (addareas.contain(newblob)){                            //weng jian cun zai yu add qu zhong
            addareas.remove(newblob);
            writeObject(stageaddareas,addareas);
             return;
        }
        Commit currentcommit = getheadpoint();           //weng jian zai dang qian commit zhong
        if (currentcommit.contain(newblob)) {
            rmareas=readObject(stagermareas,Stage.class);
           rmareas.add(newblob);
           writeObject(stagermareas,rmareas);
         restrictedDelete(newblob.getFile());
            return;
        }

        System.out.println("No reason to remove the file.");
        System.exit(0);


    }
//Starting at the current head commit, display information about each commit
// backwards along the commit tree until the initial commit, following the first parent commit links,
// ignoring any second parents found in merge commits.
// (In regular Git, this is what you get with git log --first-parent).
// This set of commit nodes is called the commit’s history.
// For every node in this history, the information it should display is the commit id,
// the time the commit was made, and the commit message. Here is an example of the exact format it should follow:
    public static void log() {
        String current=getcurrentcommit();
        Commit first=getheadpoint();
        while (!first.getParents().isEmpty()){
            System.out.println("===\ncommit "+first.getCid());
            if (first.getParents().size()>=2){
                String parent1=first.getParents().get(0).substring(0,7);
                String parent2=first.getParents().get(1).substring(0,7);
                System.out.println("Merge: "+parent1+" "+parent2);
            }
            System.out.println("Date: "+first.getTimestamp()+"\n" +first.getMessage()+"\n");
           current=first.getParents().get(0);
           first=getcommit(current);
        }
        System.out.println("===\ncommit "+first.getCid());
        System.out.println("Date: "+first.getTimestamp()+"\n" +first.getMessage()+"\n");

    }
    public static void globalog(){
        List<String> commitList = plainFilenamesIn(commitarea);
        for (String filename : commitList) {
            Commit commit=getcommit(filename);
            System.out.println("===\ncommit "+commit.getCid());
            if (commit.getParents().size()>=2){
                String parent1=commit.getParents().get(0).substring(0,7);
                String parent2=commit.getParents().get(1).substring(0,7);
                System.out.println("Merge: "+parent1+" "+parent2);
            }
            System.out.println("Date: "+commit.getTimestamp()+"\n" +commit.getMessage()+"\n");
        }
    }
    //Prints out the ids of all commits that have the given commit message,
    // one per line. If there are multiple such commits, it prints the ids out on separate lines.
    // The commit message is a single operand; to indicate a multiword message,
    // put the operand in quotation marks, as for the commit command below. H
    // int: the hint for this command is the same as the one for global-log
    public static void find(String message){
        List<String> commitList = plainFilenamesIn(commitarea);
        List<String> messagematchcommits=new ArrayList<>();
        for (String filename : commitList) {
            Commit commit=getcommit(filename);
            if (commit.getMessage().equals(message)){
                messagematchcommits.add(commit.getCid());
            }
        }
        if (messagematchcommits.isEmpty()){
            System.out.println("Found no commit with that message.");
        }
        for (String messagematchcommit : messagematchcommits) {
            System.out.println(messagematchcommit);
        }

    }

//
    public static void commit(String message) {
        addareas=readObject(stageaddareas,Stage.class);
        rmareas=readObject(stagermareas,Stage.class);
        if (addareas.isEmpty()&&rmareas.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
      if (message.equals("")){
    System.out.println("Please enter a commit message.");
    System.exit(0);
       }
     String parent=getcurrentcommit();
      List<String>parents=new ArrayList<>();
      parents.add(parent);
        Commit currentcommit = getheadpoint();
       TreeMap<String,String> currenttree=currentcommit.getTree();
        updatetree(currenttree,addareas,rmareas);
Commit nowcommit=new Commit(message,parents,new Date(),currenttree);
    nowcommit.commit();
  writeContents(master,nowcommit.getCid());
      initstage();
    }
    //Displays what branches currently exist,
    // and marks the current branch with a *. Also displays what files have been staged for addition or removal.
    public static void status(){
        addareas=readObject(stageaddareas,Stage.class);
        rmareas=readObject(stagermareas,Stage.class);
        Commit currentcommit=getheadpoint();
       TreeSet<String>   branchname =new TreeSet<>();
       TreeSet<String>   modifiedfilename =new TreeSet<>();
       TreeSet<String>   untrackedfilename =new TreeSet<>();
      List<String>branchfiles=plainFilenamesIn(heads); //na dao suoyou  branch
      String headponit=readContentsAsString(head).substring(11);  // dedao head suozhi branch
        for (String branchfile : branchfiles) {
             if (branchfile.equals(headponit)){
                 branchname.add("*"+branchfile);
             }
           else branchname.add(branchfile);
        }
        workingdictory=getcwdfile();
        for (Blob blob : workingdictory) {
            if (currentcommit.contain(blob.getFilename())&&!addareas.contain(blob)){
                   if (!currentcommit.contain(blob)){
                       modifiedfilename.add(blob.getFilename()+" (modified)");
                   }
            }
          else  if (addareas.contain(blob.getFilename())){
                        if (!addareas.contain(blob)){
                            modifiedfilename.add(blob.getFilename()+" (modified)");
                        }
            }
         else if (!addareas.contain(blob.getFilename())&&!currentcommit.contain(blob.getFilename())){
                   untrackedfilename.add(blob.getFilename());
            }
         else if(rmareas.contain(blob.getFilename())){
                untrackedfilename.add(blob.getFilename());
            }
        }
       Set<String>commitownfile=currentcommit.getAllfile();
        Set<String>addownfile=addareas.KeySet();
        Set<String>rmownfile=rmareas.KeySet();
        for (String filename : addownfile) {
            if (!workingdictory.contains(filename)){
                modifiedfilename.add(filename+" (deleted)");
            }
        }
        for (String filename : commitownfile) {
            if (!workingdictory.contains(filename)&&!rmownfile.contains(filename)){
                modifiedfilename.add(filename+" (deleted)");
            }
        }

    statusshow(branchname,"Branches");
    statusshow(addareas.KeySet(),"Staged Files");
    statusshow(rmareas.KeySet(),"Removed Files");
    statusshow(modifiedfilename,"Modifications Not Staged For Commit");
    statusshow(untrackedfilename,"Untracked Files");
    }

//    Creates a new branch with the given name,
//    and points it at the current head commit.
//    A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
//    This command does NOT immediately switch to the newly created branch (just as in real Git).
//    Before you ever call branch, your code should be running with a default branch called "master".
public static void branch(String branchname){
        if (ifbranchexist(branchname)){
            System.out.println("A branch with that name already exists.");
        }
        String current =getcurrentcommit();
        writeContents(join(heads,branchname),current);
}
// Deletes the branch with the given name.
// This only means to delete the pointer associated with the branch;
// it does not mean to delete all commits that were created under the branch, or anything like that.
    public static void rmbranch(String branchname){
        if (!ifbranchexist(branchname)){
            System.out.println("A branch with that name does not exist.");
        }
   if (ifheadinthisbranch(branchname)){
    System.out.println("Cannot remove the current branch.");
    }
   restrictedDelete(join(heads,branchname));
    }

//    Takes the version of the file as it exists in the head commit and puts it in the working directory,
//    overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.

//Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory,
// overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.

//Takes all files in the commit at the head of the given branch, and puts them in the working directory,
// overwriting the versions of the files that are already there if they exist. Also, at the end of this command, the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).

    public static void checkoutfile(String filename){
            String currentcommit=getcurrentcommit();
            checkoutcommitfile(currentcommit,filename);
    }


    public static void checkoutcommitfile(String commitid,String filename){
        if (!commitexist(commitid)) {
            System.out.println("No commit with that id exists.");
        }
        Commit commit=getcommit(commitid);
        String blobid=commit.getbid(filename);
        if (blobid==null) {
            System.out.println("File does not exist in that commit.");
        }
       create(filename,blobid);
}
 public static void branchcheckout(String branchname){
if (!ifbranchexist(branchname)){
    System.out.println("No such branch exists.");
}
if (ifheadinthisbranch(branchname)){
    System.out.println("No need to checkout the current branch.");
}
Set<String> untrackedfile=getuntrackedfile();
if (!untrackedfile.isEmpty()) {
    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
}
Commit currentcommit =getheadpoint();
String branchcontent=readContentsAsString(join(heads,branchname));
Commit branchcommit=getcommit(branchcontent);
TreeMap<String,String> changefile=getchange(currentcommit,branchcommit.getTree());
TreeSet<String> deletefile=getdelete(currentcommit,branchcommit.getTree());
    create(changefile);
   delete(deletefile);
   changehead(branchname);
   initstage();
 }




private static boolean ifheadinthisbranch(String branchname){
    String currentbranch= readContentsAsString(join(heads,branchname));
    String currentcommit=getcurrentcommit();
    return currentcommit.equals(currentbranch);
}

private static boolean ifbranchexist(String branchname){
        File branch=join(heads,branchname);
        return branch.exists();
}

private  static void  statusshow( Set<String> filenames,String message){
    System.out.println("=== "+message+" ===");
    for (String filename : filenames) {
        System.out.println(filename);
    }
}
private static TreeSet<Blob>  getcwdfile(){
    List<String> filenames =plainFilenamesIn(CWD);
    TreeSet<Blob> blobs=new TreeSet<>();
    for (String filename : filenames) {
        blobs.add(new Blob(filename));
    }
    return blobs;
}


    /* TODO: fill in the rest of this class. */
    private static Commit getheadpoint(){
        String currentcommit=getcurrentcommit();
        Commit commit=getcommit(currentcommit);
        return commit;
    }
    private static Commit getcommit(String commithash){
        Commit commit=readObject(join(commitarea,commithash),Commit.class);
        return commit;
    }
    private static String getcurrentcommit(){
        String headcontent=readContentsAsString(head);
        File file=join(Repository.GITLET_DIR,headcontent);
        String currentcommit=readContentsAsString(file);
        return currentcommit;
    }
    private static void updatetree(TreeMap<String,String>treeMap,Stage addstage,Stage rmstage){
        Set<String>add=addstage.KeySet();
        Set<String>rm=rmstage.KeySet();
        for (String filename : rm) {
            String id= addstage.get(filename);
            treeMap.remove(filename,id);
        }
        for (String filename : add) {
            String id= addstage.get(filename);
            treeMap.put(filename,id);
        }
    }
    private static boolean commitexist(String commitid){
        File commit=join(commitarea,commitid);
        return commit.exists();
    }
private static void initstage(){
        writeObject(stageaddareas,new Stage());
        writeObject(stagermareas,new Stage());
}
private  static void create(String filename,String bid){
         Blob blob=readObject(join(blobarea,bid),Blob.class);
         writeContents(join(CWD,filename),blob.getContents());
}
    private  static void create(TreeMap<String,String> news){
       Set<String> keys=news.keySet();
        for (String key : keys) {
            String bid =news.get(key);
            create(key,bid);
        }
    }

    private  static void delete(Set<String> deletes){
        for (String delete : deletes) {
            restrictedDelete(delete);
        }
    }
private  static Set<String> getuntrackedfile(){
        TreeSet<String>files=new TreeSet<>();
    Commit currentcommit=getheadpoint();
    workingdictory=getcwdfile();
    for (Blob blob : workingdictory) {
       if (!currentcommit.contain(blob)){
            files.add(blob.getFilename());
        }
    }
    return files;
}

private static TreeMap<String,String>getchange(Commit base,TreeMap<String,String> news){
        TreeMap<String,String>changes=new TreeMap<>();
        Set<String>files=news.keySet();
    for (String file : files) {
       String bid=news.get(file);
       if (!base.contain(file,bid)){
           changes.put(file,bid);
       }
    }
    return changes;
}

    private static TreeSet<String>getdelete(Commit base,TreeMap<String,String> news){
        TreeSet<String>delete=new TreeSet<>();
        Set<String>files=base.getTree().keySet();
        for (String file : files) {
            if (!news.containsKey(file)){
                delete.add(file);
            }
        }
        return delete;
    }
    private static void  changehead(String branchname){
        writeContents(head,"refs\\heads\\"+branchname);
    }

}
