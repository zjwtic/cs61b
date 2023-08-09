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
    public File CWD ;
 public Repository(){
     CWD = new File(System.getProperty("user.dir"));
     Initalize();
 }
    public Repository(String path){
        CWD = new File(path);
        Initalize();
    }
private void Initalize(){
  this.GITLET_DIR = join(CWD, ".gitlet");
    this.STAGE_ADD_AREAS = join(GITLET_DIR, "addareas"); //store the lastest commit has not add ones
    this. STAGE_RM_AREAS = join(GITLET_DIR, "rmareas"); //store the lastest commit has not rm ones
    this.   OBJECTS = join(GITLET_DIR, "objects");
    this.COMMITAREA = join(OBJECTS, "Commits");
    this. BLOBAREA = join(OBJECTS, "Blobs");
    this.  HEAD = join(GITLET_DIR, "HEAD");
    this. REFS = join(GITLET_DIR, "refs");
    this.HEADS = join(REFS, "heads");
    this.REMOTE=join(REFS,"remotes");
    this.MASTER = join(HEADS, "master");
    this.CONFIG = join(GITLET_DIR, "config");

}

    /**
     * The current working directory.
     */

    /**
     * The .gitlet directory.
     */
    public File GITLET_DIR ;
    /**
     * stored areas
     */
    public  File STAGE_ADD_AREAS ; //store the lastest commit has not add ones
    public  File STAGE_RM_AREAS ; //store the lastest commit has not rm ones
    /**
     * commit areas
     */
    public  File OBJECTS ;
    public  File COMMITAREA ;
    /**
     * blob areas
     */
    public File BLOBAREA ;
    /**
     * head master pointer
     **/
    public  File HEAD ;
    public  File REFS ;
    public  File HEADS ;
    public  File MASTER ;

    // config
    public  File CONFIG ;
    //remote
    public File REMOTE;

    public static final String REFS_HEADS ="refs/heads/";
    public  Stage addareas;
    public  Stage rmareas;
    public  HashSet<Blob> workingdictory;
    public  void   TestIfGitExist(){
        if (!GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
    public  void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        OBJECTS.mkdir();
        COMMITAREA.mkdir();
        BLOBAREA.mkdir();
        REFS.mkdir();
        HEADS.mkdir();
        REMOTE.mkdir();
        writeObject(STAGE_ADD_AREAS,new Stage());
        writeObject(STAGE_RM_AREAS,new Stage());
        writeContents(HEAD, REFS_HEADS +"master");
        Commit commititem = new Commit("initial commit", new ArrayList<>(), new Date(0), new TreeMap<>());
       UpdateCommitAreas(commititem);
       writeContents(MASTER,commititem.getCid());
    }
private  void UpdateCommitAreas(Commit commititem){
    writeObject( join(COMMITAREA,commititem.getCid()),commititem);
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
   private void readstage(){
       rmareas=readObject(STAGE_RM_AREAS,Stage.class);
       addareas=readObject(STAGE_ADD_AREAS,Stage.class);
   }
   private  void Updatermareas(){
       writeObject(STAGE_RM_AREAS,rmareas);
   }
    private  void Updateaddareas(){
        writeObject(STAGE_ADD_AREAS,addareas);
    }
    private  void Updatestage(){
        writeObject(STAGE_RM_AREAS,rmareas);
        writeObject(STAGE_ADD_AREAS,addareas);
    }

    private  void UpdateBlobs(Blob newblob){
        writeObject(join(BLOBAREA,newblob.getBid()),newblob);
    }
    public  void add(String filename) {
        File addfile=join(CWD,filename);
        if (!addfile.isFile()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob newblob=new Blob(filename,CWD);
// check If the current working version of the file is identical to the version in the current commit,
//     do not stage it to be added, and remove it from the staging area if it is already there
        Commit currentcommit = getheadpoint();
        readstage();
        if (currentcommit.contain(newblob)) {
            if (rmareas.contain(newblob)){
                rmareas.remove(newblob);
               Updatermareas();
            }
            return;
        }
            if (addareas.contain(newblob)) {
                return;
            }
         addareas.add(newblob);
       Updateaddareas();
        UpdateBlobs(newblob); //update blob
    }
//Unstage the file if it is currently staged for addition.
// If the file is tracked in the current commit,
// stage it for removal and remove the file from the working directory if the user
// has not already done so (do not remove it unless it is tracked in the current commit)
    public  void rm(String filename) {
        readstage();
        if (addareas.contain(filename)){                            //weng jian cun zai yu add qu zhong
            addareas.remove(filename);
           Updateaddareas();
             return;
        }
        Commit currentcommit = getheadpoint();  //weng jian zai dang qian commit zhong
        String bid=currentcommit.getbid(filename);
        if (bid!=null) {
           rmareas.add(filename,bid);
          Updatermareas();
         restrictedDelete(join(CWD,filename));
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
    public  void log() {
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
    public  void globalog(){
        List<String> commitList = plainFilenamesIn(COMMITAREA);
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
    public  void find(String message){
        List<String> commitList = plainFilenamesIn(COMMITAREA);
        List<String> messagematchcommits=new ArrayList<>();
        for (String filename : commitList) {
            Commit commit=getcommit(filename);
            if (commit.getMessage().equals(message)){
                messagematchcommits.add(commit.getCid());
            }
        }
        if (messagematchcommits.isEmpty()){
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        for (String messagematchcommit : messagematchcommits) {
            System.out.println(messagematchcommit);
        }

    }

//
    public  void commit(String message) {
       readstage();
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
    UpdateCommitAreas(nowcommit);
changecurrentbranch(nowcommit.getCid());
      initstage();
    }
    //Displays what branches currently exist,
    // and marks the current branch with a *. Also displays what files have been staged for addition or removal.
    public  void status(){
       readstage();
        Commit currentcommit=getheadpoint();
       TreeSet<String>   branchname =new TreeSet<>();
       TreeSet<String>   modifiedfilename =new TreeSet<>();
       TreeSet<String>   untrackedfilename =new TreeSet<>();
      List<String>branchfiles=plainFilenamesIn(HEADS); //na dao suoyou  branch
      String headponit=readContentsAsString(HEAD).replace(REFS_HEADS,"");  // dedao head suozhi branch
        for (String branchfile : branchfiles) {
             if (branchfile.equals(headponit)){
                 branchname.add("*"+branchfile);
             }
           else branchname.add(branchfile);
        }
     GetCWDfile();
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
            if (!ifcontainfilename(workingdictory,filename)){
                modifiedfilename.add(filename+" (deleted)");
            }
        }
        for (String filename : commitownfile) {
            if (!ifcontainfilename(workingdictory,filename)&&!rmownfile.contains(filename)){
                modifiedfilename.add(filename+" (deleted)");
            }
        }

    statusshow(branchname,"Branches");
    statusshow(addareas.KeySet(),"Staged Files");
    statusshow(rmareas.KeySet(),"Removed Files");
    statusshow(modifiedfilename,"Modifications Not Staged For Commit");
    statusshow(untrackedfilename,"Untracked Files");
    }



    private void GetCWDfile(){
        workingdictory=getcwdfile();
    }

//    Creates a new branch with the given name,
//    and points it at the current head commit.
//    A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
//    This command does NOT immediately switch to the newly created branch (just as in real Git).
//    Before you ever call branch, your code should be running with a default branch called "master".
public  void branch(String branchname){
        if (ifbranchexist(branchname)){
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String current =getcurrentcommit();
        writeContents(join(HEADS,branchname),current);
}
// Deletes the branch with the given name.
// This only means to delete the pointer associated with the branch;
// it does not mean to delete all commits that were created under the branch, or anything like that.
    public  void rmbranch(String branchname){
        if (!ifbranchexist(branchname)){
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
   if (ifheadinthisbranch(branchname)){
    System.out.println("Cannot remove the current branch.");
       System.exit(0);
    }
   join(HEADS,branchname).delete();
    }

//    Takes the version of the file as it exists in the head commit and puts it in the working directory,
//    overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.

//Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory,
// overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.

//Takes all files in the commit at the head of the given branch, and puts them in the working directory,
// overwriting the versions of the files that are already there if they exist. Also, at the end of this command, the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).

    public  void checkoutfile(String filename){
            String currentcommit=getcurrentcommit();
            checkoutcommitfile(currentcommit,filename);
    }
    public  void checkoutcommitfile(String commitid,String filename){
        String id=commitid;
        if (commitid.length()>=6)
        id=checkifshortid(commitid);
        if (!commitexist(id)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit=getcommit(id);
        String blobid=commit.getbid(filename);
        if (blobid==null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
       create(filename,blobid);
}
 public void branchcheckout(String branchname){
      String  branchnamesepartor=branchname.replace("/",File.separator).replace("\\",File.separator);
if (!ifbranchexist(branchname)){
    System.out.println("No such branch exists.");
    System.exit(0);
}
if (ifheadinthisbranch(branchname)){
    System.out.println("No need to checkout the current branch.");
    System.exit(0);
}
Commit currentcommit =getheadpoint();

     String branchcontent;
if (branchnamesepartor.contains(File.separator)){
    branchcontent=readContentsAsString(join(REMOTE,branchnamesepartor));
}
else branchcontent=readContentsAsString(join(HEADS,branchnamesepartor));


Commit branchcommit=getcommit(branchcontent);
Set<String> untrackedfile=getuntrackedfile(currentcommit,branchcommit);
     if (!untrackedfile.isEmpty()) {
         System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
         System.exit(0);
     }

TreeMap<String,String> changefile=getchange(currentcommit,branchcommit.getTree());
TreeSet<String> deletefile=getdelete(currentcommit,branchcommit.getTree());
    create(changefile);
   delete(deletefile);
   changehead(branchname);
   initstage();
 }
 public  void reset(String cid){
        String id=cid;
     if (cid.length()>=6)
    id=checkifshortid(cid);
     if (!commitexist(id)) {
         System.out.println("No commit with that id exists.");
         System.exit(0);
     }
     branch("temp");
   String nowbranch= changecurrentbranch(id);
     changehead("temp");
     branchcheckout(nowbranch);
     rmbranch("temp");
 }
//Merges files from the given branch into the current branch.
// This method is a bit complicated, so here’s a more detailed description
public  void merge(String branchname){
    String  branchnamesepartor=branchname.replace("/",File.separator).replace("\\",File.separator);
   readstage();
    if (!addareas.isEmpty()||!rmareas.isEmpty()){
        System.out.println("You have uncommitted changes.");
        System.exit(0);
    }
    if (!ifbranchexist(branchname)){
        System.out.println("A branch with that name does not exist.");
        System.exit(0);
    }
    if (ifheadinthisbranch(branchname)){
        System.out.println("Cannot merge a branch with itself.");
        System.exit(0);
    }
    Commit currentcommit=getheadpoint();

    String branchcid;
    if (branchnamesepartor.contains(File.separator)){             //if barnch is reomote branch
        branchcid=readContentsAsString(join(REMOTE,branchnamesepartor));
    }
    else branchcid=readContentsAsString(join(HEADS,branchnamesepartor));


    Commit branchcommit=readObject(join(COMMITAREA,branchcid),Commit.class);
    Set<String> untrackedfile=getuntrackedfile(currentcommit,branchcommit);
    if (!untrackedfile.isEmpty()) {
        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
        System.exit(0);
    }
    String basecommitcid=getsplitpoint(currentcommit,branchcommit);
    if (basecommitcid.equals(currentcommit.getCid())){
        branchcheckout(branchname);
        System.out.println("Current branch fast-forwarded.");
        System.exit(0);
    }
    if (basecommitcid.equals(branchcommit.getCid())){
        System.out.println("Given branch is an ancestor of the current branch.");
        System.exit(0);
    }




    Commit basecommit=readObject(join(COMMITAREA,basecommitcid),Commit.class);
    TreeMap<String,String> newtree=  mergefile(basecommit,currentcommit,branchcommit);
    List<String> parents=new ArrayList<>();
    parents.add(currentcommit.getCid());
    parents.add(branchcommit.getCid());
    String currentbranch=readContentsAsString(HEAD).replace(REFS_HEADS,"");
    String message="Merged "+branchname+" into "+currentbranch+".";   //this is not brancgsepator   for log need same barnchname
    Commit news=new Commit(message, parents,new Date(),newtree);
    UpdateCommitAreas(news);
   reset(news.getCid());
}

public  void addremote(String remotename,String remotepath){
    File remotefile=join(REMOTE,remotename);
    if (remotefile.isDirectory()){
        System.out.println("A remote with that name already exists.");
        System.exit(0);
    }
    remotefile.mkdir();
    String configcontent="[remote \""+remotename+"\"]"+"\n";
    String speartorpath=remotepath.replace("/",File.separator).replace("\\",File.separator);
    configcontent+=speartorpath+"\n";
    if (CONFIG.isFile()){
        String confignowcontent=readContentsAsString(CONFIG);
        configcontent=confignowcontent+configcontent;
    }
        writeContents(CONFIG,configcontent);
}

public  void rmremote(String remotename){
    File remotefile=join(REMOTE,remotename);
    if (!remotefile.isDirectory()){
        System.out.println("A remote with that name does not exist.");
        System.exit(0);
    }
    remotefile.delete();
        String confignowcontent=readContentsAsString(CONFIG);
        String remotecontent=getremotecontent(remotename);
        confignowcontent=confignowcontent.replace(remotecontent+"\n","")
                .replace("[remote \""+remotename+"\"]"+"\n","");
        writeContents(CONFIG,confignowcontent);
    }


//Description: Attempts to append the current branch’s commits to the end of the given branch at the given remote. Details:
//
//This command only works if the remote branch’s head is in the history of the current local head,
// which means that the local branch contains some commits in the future of the remote branch.
// In this case, append the future commits to the remote branch.
// Then, the remote should reset to the front of the appended commits (so its head will be the same as the local head).
// This is called fast-forwarding
public   void push(String remotename,String branchname){
       File reomotepath=new File(getremotecontent(remotename));
    Repository remote=new Repository(reomotepath.getParent());
        if (!remote.GITLET_DIR.isDirectory()){
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        Commit currentcommit=getheadpoint();
    LinkedList<String> currentheadlog=getcommitlog(currentcommit);
//    String remotehead=readContentsAsString(remote.HEAD).replace(REFS_HEADS,"");
//    Commit remotebranch=readObject(join(remote.HEADS+remotehead),Commit.class);
    Commit remotebranch=remote.getheadpoint();
    if (!currentheadlog.contains(remotebranch.getCid())){
        System.out.println("Please pull down remote changes before pushing.");
        System.exit(0);
    }

    File remoteBranch = join(remote.HEADS, branchname);
    if (!remoteBranch.exists()) {
        remote.branch(branchname);
    }


    LinkedList<String> remoteheadlog=remote.getcommitlog(remotebranch);
    for (String s : currentheadlog) {
        if (!remoteheadlog.contains(s)){
            Commit commit=readObject(join(COMMITAREA,s),Commit.class);
            writeObject(join(remote.COMMITAREA,commit.getCid()),commit);
            Set<String>commitfilenames=commit.getAllfile();
            for (String commitfilename : commitfilenames) {
                String bid=commit.getbid(commitfilename);
                Blob blob=readObject(join(BLOBAREA,bid),Blob.class);
               File remoteblob=join(remote.BLOBAREA,blob.getBid());
                if (!remoteblob.isFile()) {
                    writeObject(remoteblob, blob);
                }
            }

        }
    }
    remote.reset(currentcommit.getCid());
}
// Brings down commits from the remote Gitlet repository into the local Gitlet repository.
// Basically, this copies all commits and blobs from the given branch in the remote repository
// (that are not already in the current repository) into a branch named [remote name]/[remote branch name] in the local .
// gitlet (just as in real Git), changing [remote name]/[remote branch name] to point to the head commit
// (thus copying the contents of the branch from the remote repository to the current one).
// This branch is created in the local repository if it did not previously exist.
public void fetch(String remotename,String branchname){
    File reomotepath=new File(getremotecontent(remotename));
    Repository remote=new Repository(reomotepath.getParent());
    if (!remote.GITLET_DIR.isDirectory()){
        System.out.println("Remote directory not found.");
        System.exit(0);
    }
    if (!remote.ifbranchexist(branchname)) {
        System.out.println("That remote does not have that branch.");
        System.exit(0);
    }
    String remotebranchppnint=readContentsAsString(join(remote.HEADS,branchname));
    Commit remotecid=remote.getcommit(remotebranchppnint);
    File branch = join(REMOTE, remotename, branchname);   //write the current  to the remote head
    writeContents(branch, remotecid.getCid());


    LinkedList<String> remotebranchlog=remote.getcommitlog(remotecid);
    for (String cid : remotebranchlog) {
        File currentcommits=join(COMMITAREA,cid);
        if (currentcommits.isFile()){
            continue;
        }
        Commit remotecommit=remote.getcommit(cid);
        writeObject(currentcommits,remotecommit);

        Set<String> reomoteblobs=remotecommit.getAllfile();
        for (String blobfilename : reomoteblobs) {
            String blobbid=remotecommit.getbid(blobfilename);

            File currentblobs=join(BLOBAREA,blobbid);
            if (currentblobs.isFile()){
                continue;
            }
            Blob remoteblob=readObject(join(remote.BLOBAREA,blobbid),Blob.class);
            writeObject(currentblobs,remoteblob);
        }

    }

}
public  void pull(String remotename,String branchname){
fetch(remotename,branchname);
merge(remotename+File.separator+branchname);
}



private    String getremotecontent(String remotename){
    String confignowcontent=readContentsAsString(CONFIG);
    String lines[] = confignowcontent.split("\\r?\\n");
    int index=-1;
    for (int i = 0; i < lines.length; i++) {
        if (lines[i].equals("[remote \""+remotename+"\"]")){
            index=i;
            break;
        }
    }
    if (index==-1){
        System.out.println("A remote with that name does not exist.");
        System.exit(0);
    }
    return lines[index+1];
}

private  boolean ifheadinthisbranch(String branchname){
    String currentbranch=readContentsAsString(HEAD).replace(REFS_HEADS,"");
    return branchname.equals(currentbranch);
}

private  boolean ifbranchexist(String branchname){
        File currentbranch=join(HEADS,branchname);
        File remotebranch=join(REMOTE,branchname);
        return currentbranch.exists()||remotebranch.exists();
}

private   void  statusshow( Set<String> filenames,String message){
    System.out.println("=== "+message+" ===");
    for (String filename : filenames) {
        System.out.println(filename);
    }
    System.out.println();
}
private  HashSet<Blob>  getcwdfile(){
    List<String> filenames =plainFilenamesIn(CWD);
    HashSet<Blob> blobs=new HashSet<>();
    for (String filename : filenames) {
        blobs.add(new Blob(filename,CWD));
    }
    return blobs;
}


    /* TODO: fill in the rest of this class. */
    private Commit getheadpoint(){
        String currentcommit=getcurrentcommit();
        Commit commit=getcommit(currentcommit);
        return commit;
    }
    private  Commit getcommit(String commithash){
        Commit commit=readObject(join(COMMITAREA,commithash),Commit.class);
        return commit;
    }
    private  String getcurrentcommit(){
        String branchname=readContentsAsString(HEAD).replace(REFS_HEADS,"");
        String branchnameseaprator=branchname.replace("/",File.separator).replace("\\",File.separator);

        File file;
        if (branchnameseaprator.contains(File.separator))
            file=join(REMOTE,branchnameseaprator);
       else   file=join(HEADS,branchnameseaprator);

        String currentcommit=readContentsAsString(file);
        return currentcommit;
    }
    private  void updatetree(TreeMap<String,String>treeMap,Stage addstage,Stage rmstage){
        Set<String>add=addstage.KeySet();
        Set<String>rm=rmstage.KeySet();
        for (String filename : rm) {
            String id= rmstage.get(filename);
            treeMap.remove(filename,id);
        }
        for (String filename : add) {
            String id= addstage.get(filename);
            treeMap.put(filename,id);
        }
    }
    private  boolean commitexist(String commitid){
        File commit=join(COMMITAREA,commitid);
        return commit.exists();
    }
private  void initstage(){
        writeObject(STAGE_ADD_AREAS,new Stage());
        writeObject(STAGE_RM_AREAS,new Stage());
}
private   void create(String filename,String bid){
         Blob blob=readObject(join(BLOBAREA,bid),Blob.class);
         writeContents(join(CWD,filename),blob.getContents());
}
    private  void create(TreeMap<String,String> news){
       Set<String> keys=news.keySet();
        for (String key : keys) {
            String bid =news.get(key);
            create(key,bid);
        }
    }

    private  void delete(Set<String> deletes){
        for (String delete : deletes) {
            restrictedDelete(delete);
        }
    }
private   Set<String> getuntrackedfile(Commit currentcommit,Commit branchcommit){
        TreeSet<String>files=new TreeSet<>();
         workingdictory=getcwdfile();
    for (Blob blob : workingdictory) {
        if (!currentcommit.contain(blob)&&branchcommit.contain(blob.getFilename())&&!branchcommit.contain(blob)){
            files.add(blob.getFilename());
        }
    }
    return files;
}

private  TreeMap<String,String>getchange(Commit base,TreeMap<String,String> news){
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

    private  TreeSet<String>getdelete(Commit base,TreeMap<String,String> news){
        TreeSet<String>delete=new TreeSet<>();
        Set<String>files=base.getTree().keySet();
        for (String file : files) {
            if (!news.containsKey(file)){
                delete.add(file);
            }
        }
        return delete;
    }
    private void  changehead(String branchname){
        writeContents(HEAD, REFS_HEADS +branchname);
    }
    private  String  checkifshortid(String id){
        String tempid=id;
       List<String> commits=plainFilenamesIn(COMMITAREA);
        for (String commit : commits) {
            String ifshortid=commit.substring(0,tempid.length());
            if (ifshortid.equals(tempid)){
                tempid=commit;
                break;
            }
        }
        return tempid;
    }

    private  boolean ifcontainfilename(HashSet<Blob>base,String name){
        for (Blob blob : base) {
            if (blob.getFilename().equals(name)){
                return true;
            }
        }
        return false;
    }
    private  String changecurrentbranch(String cid){
        String nowbranch=readContentsAsString(HEAD).replace(REFS_HEADS,"");
        writeContents(join(HEADS,nowbranch),cid);
        return nowbranch;
    }
    private  String getsplitpoint(Commit current,Commit mergebranch){
//        LinkedList<String>currentcommitlog=getcommitlog(current);   //   normal  version  O(n)
//        LinkedList<String>mergecommitlog=getcommitlog(mergebranch);
//        String id=null;
//        while (!currentcommitlog.isEmpty()) {
//            String cid = currentcommitlog.removeFirst();
//            if (mergecommitlog.contains(cid)) {
//                id = cid;
//                break;
//            }
//        }
//        return id;

//greater version
//  两个同时进行 bfs
//先到 commit 0 点的就不做了
//让另一个 接着去bfs
//直到 找到对面的或者对面访问过的  那就是我们要的
//我写的复杂了点，但想法差不多  其实只要设置访问过的就可以的了
//不用contain  这样 currentids  mergebranchids 就都不需要了
//
//两个都从头bfs 判断此点是否访问过  访问过返回
//反之 继续bfs 同时设置访问过
        Commit currentone=current;
        Commit branchone=mergebranch;
           HashSet<String> currentids=new HashSet<>();
           HashSet<String> mergebranchids=new HashSet<>();
           Queue<String>currentqueue=new LinkedList<>();
           Queue<String>branchqueue=new LinkedList<>();
           String currentCid="";
        String branchCid="";
           currentqueue.add(currentone.getCid());
           branchqueue.add(branchone.getCid());
           while (!currentqueue.isEmpty()||!branchqueue.isEmpty()){
               if (!currentqueue.isEmpty()){
                    currentCid=currentqueue.remove();
                   currentids.add(currentCid);

               }
              if (!branchqueue.isEmpty()){
                   branchCid=branchqueue.remove();
                  mergebranchids.add(branchCid);
              }
              if (mergebranchids.contains(currentCid)){
                      return currentCid;
              }
              if (currentids.contains(branchCid)){
                  return branchCid;
              }
              if (!currentCid.equals("")){
                  currentone=readObject(join(COMMITAREA,currentCid),Commit.class);
                  List<String> parents=currentone.getParents();
                  if (!parents.isEmpty()){
                      for (String parent : parents) {
                          currentqueue.add(parent);
                      }
                  }
              }
               if (!branchCid.equals("")){
                  branchone=readObject(join(COMMITAREA,branchCid),Commit.class);
                   List<String> parents=branchone.getParents();
                   if (!parents.isEmpty()){
                       for (String parent : parents) {
                          branchqueue.add(parent);
                       }
                   }
               }

           }
           return null;
    }
    public  LinkedList<String> getcommitlog(Commit commit){
        Commit tempcommit=commit;
        LinkedList<String> temp=new LinkedList<>();
        LinkedList<String> tempqueue=new LinkedList<>();
        tempqueue.addFirst(commit.getCid());
        while (!tempqueue.isEmpty()){
            String cid=tempqueue.removeFirst();
            temp.addLast(cid);
            tempcommit=readObject(join(COMMITAREA,cid),Commit.class);
            if (!tempcommit.getParents().isEmpty()){
                List<String>parents=tempcommit.getParents();
                for (String parent : parents) {
                    if (!tempqueue.contains(parent)) {
                        tempqueue.addLast(parent);
                    }
                }
            }
        }
        return temp;

    }

    private  TreeMap<String,String> mergefile(Commit base,Commit current,Commit other){
        TreeMap<String,String> newcommit=new TreeMap<>();
        Set<String> basekeys=base.getAllfile();
        for (String basekey : basekeys) {
            String bids=base.getbid(basekey);
            if (current.ismodifed(basekey,bids)&&other.contain(basekey,bids)){
                newcommit.put(basekey,current.getbid(basekey));
                continue;
            }
            if (other.ismodifed(basekey,bids)&&current.contain(basekey,bids)){
                newcommit.put(basekey,other.getbid(basekey));
                continue;
            }
            if (current.contain(basekey,bids)&&!other.contain(basekey)){
                continue;
            }
            if (other.contain(basekey,bids)&&!current.contain(basekey)){
                continue;
            }
            if (!current.contain(basekey)&&!other.contain(basekey)){
                continue;
            }
            if (current.ismodifed(basekey,bids)&&other.ismodifed(basekey,bids)){
                if (current.getbid(basekey).equals(other.getbid(basekey))){
                    newcommit.put(basekey,current.getbid(basekey));
                }
                else {
                      Blob newblob=getmergeconfictfile(basekey,current.getbid(basekey),other.getbid(basekey));
                      newcommit.put(newblob.getFilename(),newblob.getBid());
                    System.out.println("Encountered a merge conflict.");
                }
                continue;
            }

            if (current.ismodifed(basekey,bids)&&!other.contain(basekey)){
                Blob newblob=getmergeconfictfile(basekey,current.getbid(basekey),null);
                newcommit.put(newblob.getFilename(),newblob.getBid());
                System.out.println("Encountered a merge conflict.");
               continue;
            }
            if (other.ismodifed(basekey,bids)&&!current.contain(basekey)){
                Blob newblob=getmergeconfictfile(basekey,null,other.getbid(basekey));
                newcommit.put(newblob.getFilename(),newblob.getBid());
                System.out.println("Encountered a merge conflict.");
                continue;
            }
                newcommit.put(basekey,bids);
        }
        Set<String> currentkeys=current.getAllfile();
        Set<String> otherkeys=other.getAllfile();
        for (String currentkey : currentkeys) {
            if (!other.contain(currentkey)&&!base.contain(currentkey)){
                newcommit.put(currentkey,current.getbid(currentkey));
            }
            if (other.ismodifed(currentkey,current.getbid(currentkey))&&!base.contain(currentkey)){
//                confict
                Blob newblob=getmergeconfictfile(currentkey,current.getbid(currentkey),other.getbid(currentkey));
                newcommit.put(newblob.getFilename(),newblob.getBid());
                System.out.println("Encountered a merge conflict.");
            }
        }
        for (String otherkey : otherkeys) {
            if (!current.contain(otherkey)&&!base.contain(otherkey)){
                newcommit.put(otherkey,other.getbid(otherkey));
            }
        }
        return newcommit;
    }
    private  Blob getmergeconfictfile(String filename,String onebid,String twobid){
        String one="";
        String two="";

        if (onebid!=null)
    {
        byte[] onecontent = readObject(join(BLOBAREA, onebid), Blob.class).getContents();
        one = new String(onecontent);
    }
        if (twobid!=null) {
            byte[] twocontent = readObject(join(BLOBAREA, twobid), Blob.class).getContents();
            two = new String(twocontent);
        }
        String tempcontent="<<<<<<< HEAD\n" +
               one +
                "=======\n" +
                two+
                ">>>>>>>"+"\n";
        byte[]newcontents=tempcontent.getBytes();
        Blob newblob=new Blob(filename,newcontents,CWD);
        writeObject(join(BLOBAREA,newblob.getBid()),newblob);
        return newblob;
    }

}


