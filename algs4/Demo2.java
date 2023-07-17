package edu.princeton.cs.algs4;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Demo2 {
	public static void main(String[] args) throws IOException {
	       File [] files = {
	    		   new File("dataset1.txt"),
	    		   new File("dataset2.txt"),
	    		   new File("dataset3.txt"),
	    		   new File("dataset4.txt"),
	    		   new File("dataset5.txt"),
	    		   new File("dataset6.txt"),
	    		   new File("dataset7.txt"),
	    		   new File("dataset8.txt"),
	    		   new File("dataset9.txt")};
	       
	        DMST1 mst1 = new DMST1();
	        DMST2 mst2 = new DMST2();
	        DSPT1 dst1 = new DSPT1();
	        DSPT2 dst2 = new DSPT2();
	        
	        System.out.println("To find AVERAGE MST or DST press 1!");
	        System.out.println("To find MEDIAN MST or DST press 2!");
	        System.out.println("To find MINIMUM MST or DST press 3!");
	        System.out.println("To find MAXIMUM MST or DST press 4!");
	        int s = 0;
	        int t = 0;
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
	        int opt = input.nextInt();
	        switch(opt) {
	        case 1:
	        	System.out.println("To find Average MST using PRIMS algorithm press 1!");
	        	System.out.println("To find Average MST using KRUSKAL algorithm press 2!");
	        	System.out.println("To find Average Shortest path using DIJSKSTRA algorithm press 3!");
	        	System.out.println("To find Average Shortest path using BELLMAN FORD algorithm press 4!");
	        	
	        	int opt1 = input.nextInt();
	        	switch(opt1) {
	        	case 1:
	        		System.out.print("The Average MST is ");
	        		mst1.averageMST(files);
	        		break;
	        	case 2:
	        		System.out.print("The Average MST is ");
	        		mst2.averageMST(files);
	        		break;
	        		
	        	case 3:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Average Shortest path is " + dst1.averageSP(files,s,t));
	        		break;
	        		
	        	case 4:
	        		System.out.print("Enter the source vertex(0-5): ");
	        		s = input.nextInt();
	        		System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Average Shortest path is " + dst2.averageSP(files,s,t));
	        		break;
	        	default:
	        		System.out.println("YOUR INPUT DOES NOT MATCHES");

	        	}
	        	break;
	        	
	        case 2:
	        	System.out.println("To find Median MST using PRIMS algorithm press 1!");
	        	System.out.println("To find Median MST using KRUSKAL algorithm press 2!");
	        	System.out.println("To find Median Shortest path using DIJSKSTRA algorithm press 3!");
	        	System.out.println("To find Median Shortest path using BELLMAN FORD algorithm press 4!");
	        	
	        	int opt2 = input.nextInt();
	        	switch(opt2) {
	        	case 1:
	        		System.out.println("The Median MST is "+ mst1.medianMST(files));
	        		break;
	        		
	        	case 2:
	        		System.out.println("The Median MST is "+ mst2.medianMST(files));
	        		break;
	        		
	        	case 3:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Median Shortest path is "+ dst1.medianSP(files,s,t) );
	        		break;
	        		
	        	case 4:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Median Shortest path is "+ dst2.medianSP(files,s,t));
	        		break;
					
	        	default:
	        		System.out.println("YOUR INPUT DOES NOT MATCHES!");
					}
	        	break;
	        	
	        case 3:
	        	System.out.println();
	        	System.out.println("To find Minimum MST using PRIMS algorithm press 1!");
	        	System.out.println("To find Minimum MST using KRUSKAL algorithm press 2!");
	        	System.out.println("To find Minimum Shortest path using DIJSKSTRA algorithm press 3!");
	        	System.out.println("To find Minimum Shortest path using BELLMAN FORD algorithm press 4!");
	        	
	        	int opt3 = input.nextInt();
	        	switch(opt3) {
	        	case 1:
	        		System.out.println("The Minimum MST is "+ mst1.minMST(files));
	        		break;
	        		
	        	case 2:
	        		System.out.println("The Minimum MST is "+ mst2.minMST(files));
	        		break;
	        		
	        	case 3:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Minimum Shortest path is "+ dst1.minSP(files,s,t) );
	        		break;
	        		
	        	case 4:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Minimum Shortest path is "+ dst2.minSP(files,s,t));
	        		break;
					
	        	default:
	        		System.out.println("YOUR INPUT DOES NOT MATCHES!");
					}
	        	break;
	        	
	        case 4:
	        	System.out.println("To find Maximum MST using PRIMS algorithm press 1!");
	        	System.out.println("To find Maximum MST using KRUSKAL algorithm press 2!");
	        	System.out.println("To find Maximum Shortest path using DIJSKSTRA algorithm press 3!");
	        	System.out.println("To find Maximum Shortest path using BELLMAN FORD algorithm press 4!");
	        	
	        	int opt4 = input.nextInt();
	        	switch(opt4) {
	        	case 1:
	        		System.out.println("The Maximum MST is "+ mst1.maxMST(files));
	        		break;
	        		
	        	case 2:
	        		System.out.println("The Maximum MST is "+ mst2.maxMST(files));
	        		break;
	        		
	        	case 3:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Maximum Shortest path is "+ dst1.maxSP(files,s,t) );
	        		break;
	        		
	        	case 4:
	        		System.out.print("Enter the source vertex(0-5): ");
	        	    s = input.nextInt();
	        	    System.out.print("Enter the target vertex(0-5): ");
	        	    t = input.nextInt();
	        		System.out.println("The Maximum Shortest path is "+ dst2.maxSP(files,s,t));
	        		break;
					
	        	default :
	        		System.out.println("YOUR INPUT DOES NOT MATCHES!");
					}
	        	break;
	        	
	        	
	        	}
	        

	       


	    
	}
}
