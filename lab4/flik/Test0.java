package flik;
import org.junit.Test;
import static org.junit.Assert.*;
public class Test0 {
     @Test
    public void  test0(){

           Integer  a=139;
             assertTrue(Flik.isSameNumber(a,a));
         }

    @Test
    public void  test1(){
        int   a=139;
        assertTrue(Flik.isSameNumber(a,a));
    }
    @Test
    public void  test2(){

        for (int i = 0; i < 500; i++) {
            int j=i;
            assertTrue(Flik.isSameNumber(i,j));
        }

    }

    @Test
    public void  test3(){
        for (Integer i = 0; i < 500; i++) {
            Integer j=i;
            assertTrue(Flik.isSameNumber(i,j));
        }
    }
    @Test
    public void  test4(){

        for (int i = 0; i < 128; i++) {
            int j=i;
            assertTrue(Flik.isSameNumber(i,j));
        }

    }
}

