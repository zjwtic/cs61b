package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Iterator;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        Integer m=10000;
        int N=1000;
        AList<Integer> opo=new AList<>();
        AList<Integer> Ns=new AList<>();
        AList<Double> times=new AList<>();

        SLList<Integer> test=new SLList<>();
        for (int i = 1; i <= 128000; i++) {
            test.addFirst(i);
            if (i==N){
                Stopwatch sw = new Stopwatch();
                for (int j = 0; j < 10000; j++) {
                    test.getLast();
                }
                double timeInSeconds = sw.elapsedTime();
                Ns.addLast(N);
                times.addLast(timeInSeconds);
                opo.addLast(10000);
                N*=2;
            }
            }

        printTimingTable(Ns,times,opo);

        }


}
