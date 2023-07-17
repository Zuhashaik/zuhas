package edu.princeton.cs.algs4;
import java.io.File;
import java.io.IOException;
public class Demo3 {
  public static void main(String[] args) throws IOException {
	  File [] files = {
   		   new File("graph1.txt"),
   		   new File("graph2.txt"),
   		   new File("graph3.txt")};
	  
	  DSPT1 spt = new DSPT1();
      spt.averageSPT(files, 0, 5);
	  
//	  DSPT1 spt = new DSPT1();
//	  System.out.println(spt.minMST(files, 0, 5));
}
}
