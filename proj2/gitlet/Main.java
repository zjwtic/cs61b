package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length==0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                TestNumberIsTrue(args,1);
                Repository.init();
                // TODO: handle the `init` command
                break;
            case "add":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.add(args[1]);
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "commit":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.commit(args[1]);
               break;
            case "rm":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.rm(args[1]);
                break;
            case "log":
                TestNumberIsTrue(args,1);
                TestIfGitExist();
                Repository.log();
                break;
            case "global-log":
                TestNumberIsTrue(args,1);
                TestIfGitExist();
                Repository.globalog();
                break;
            case "status":
                TestNumberIsTrue(args,1);
                TestIfGitExist();
                Repository.status();
                break;
            case "find":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.find(args[1]);
                break;
            case "branch":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.rmbranch(args[1]);
                break;
            case "checkout":
                TestNumberIsTrue(args,2,3,4);
                TestIfGitExist();
               switch (args.length){
                   case 2:
                       Repository.branchcheckout(args[1]);
                       break;
                   case 3:
                       checkcorrent(args[1],"--");
                         Repository.checkoutfile(args[2]);
                       break;
                   case 4:
                       checkcorrent(args[2],"--");
                       Repository.checkoutcommitfile(args[1],args[3]);
                       break;
               }
                break;

            case "reset":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.reset(args[1]);
                break;
            case "merge":
                TestNumberIsTrue(args,2);
                TestIfGitExist();
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
    private static void   TestNumberIsTrue(String[]args,int ...n){
        boolean lengthtrue=false;
        for (int i : n) {
            if (args.length==i){
                lengthtrue=true;
                break;
            }
        }
        if (!lengthtrue) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
    private static void checkcorrent(String get,String need){
        if (!get.equals(need)){
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
    private static void   TestIfGitExist(){
        if (!Repository.GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
