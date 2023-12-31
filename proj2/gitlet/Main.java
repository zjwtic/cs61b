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
        Repository reps=new Repository();
        switch(firstArg) {
            case "init":
                TestNumberIsTrue(args,1);
                reps.init();
                // TODO: handle the `init` command
                break;
            case "add":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.add(args[1]);
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "commit":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.commit(args[1]);
               break;
            case "rm":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.rm(args[1]);
                break;
            case "log":
                TestNumberIsTrue(args,1);
                reps.TestIfGitExist();
                reps.log();
                break;
            case "global-log":
                TestNumberIsTrue(args,1);
                reps. TestIfGitExist();
                reps.globalog();
                break;
            case "status":
                TestNumberIsTrue(args,1);
                reps.TestIfGitExist();
                reps.status();
                break;
            case "find":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.find(args[1]);
                break;
            case "branch":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.branch(args[1]);
                break;
            case "rm-branch":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.rmbranch(args[1]);
                break;
            case "checkout":
                TestNumberIsTrue(args,2,3,4);
                reps.TestIfGitExist();
               switch (args.length){
                   case 2:
                       reps.branchcheckout(args[1]);
                       break;
                   case 3:
                       checkcorrent(args[1],"--");
                       reps.checkoutfile(args[2]);
                       break;
                   case 4:
                       checkcorrent(args[2],"--");
                       reps.checkoutcommitfile(args[1],args[3]);
                       break;
               }
                break;

            case "reset":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.reset(args[1]);
                break;
            case "merge":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.merge(args[1]);
                break;
            case "add-remote":
                TestNumberIsTrue(args,3);
                reps.TestIfGitExist();
                reps.addremote(args[1],args[2]);
                break;
            case "rm-remote":
                TestNumberIsTrue(args,2);
                reps.TestIfGitExist();
                reps.rmremote(args[1]);
                break;
            case "push":
                TestNumberIsTrue(args,3);
                reps.TestIfGitExist();
                reps.push(args[1],args[2]);
                break;
            case "fetch":
                TestNumberIsTrue(args,3);
                reps.TestIfGitExist();
                reps.fetch(args[1],args[2]);
                break;
            case "pull":
                TestNumberIsTrue(args,3);
                reps.TestIfGitExist();
                reps.pull(args[1],args[2]);
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

}
