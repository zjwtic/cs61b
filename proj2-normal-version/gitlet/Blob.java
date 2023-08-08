package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;
public class Blob implements Serializable {
private byte[]contents;
private String Bid;
private String filename;
private File file;
public  Blob(){

}
    public  Blob(String filename){
      this.filename=filename;
      file=join(Repository.CWD,filename);
      contents=readContents(file);
      Bid=sha1(filename,contents);
    }

    public  Blob(String filename,byte[] contents){
        this.filename=filename;
        file=join(Repository.CWD,filename);
       this.contents=contents;
        Bid=sha1(filename,contents);
    }
    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public String getBid() {
        return Bid;
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
