package edu.princeton.cs.algs4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DSPT2 {
	    // for floating-point precision issues
	    private static final double EPSILON = 1E-14;

	    private double[] distTo;               // distTo[v] = distance  of shortest s->v path
	    private DirectedEdge[] edgeTo;         // edgeTo[v] = last edge on shortest s->v path
	    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
	    private Queue<Integer> queue;          // queue of vertices to relax
	    private int cost;                      // number of calls to relax()
	    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

	    /**
	     * Computes a shortest paths tree from {@code s} to every other vertex in
	     * the edge-weighted digraph {@code G}.
	     * @param G the acyclic digraph
	     * @param s the source vertex
	     * @throws IllegalArgumentException unless {@code 0 <= s < V}
	     */
	    public void BellmanFord(EdgeWeightedDigraph G, int s) {
	        distTo  = new double[G.V()];
	        edgeTo  = new DirectedEdge[G.V()];
	        onQueue = new boolean[G.V()];
	        for (int v = 0; v < G.V(); v++)
	            distTo[v] = Double.POSITIVE_INFINITY;
	        distTo[s] = 0.0;

	        // Bellman-Ford algorithm
	        queue = new Queue<Integer>();
	        queue.enqueue(s);
	        onQueue[s] = true;
	        while (!queue.isEmpty() && !hasNegativeCycle()) {
	            int v = queue.dequeue();
	            onQueue[v] = false;
	            relax(G, v);
	        }

	        assert check(G, s);
	    }

	    // relax vertex v and put other endpoints on queue if changed
	    private void relax(EdgeWeightedDigraph G, int v) {
	        for (DirectedEdge e : G.adj(v)) {
	            int w = e.to();
	            if (distTo[w] > distTo[v] + e.weight() + EPSILON) {
	                distTo[w] = distTo[v] + e.weight();
	                edgeTo[w] = e;
	                if (!onQueue[w]) {
	                    queue.enqueue(w);
	                    onQueue[w] = true;
	                }
	            }
	            if (++cost % G.V() == 0) {
	                findNegativeCycle();
	                if (hasNegativeCycle()) return;  // found a negative cycle
	            }
	        }
	    }

	    /**
	     * Is there a negative cycle reachable from the source vertex {@code s}?
	     * @return {@code true} if there is a negative cycle reachable from the
	     *    source vertex {@code s}, and {@code false} otherwise
	     */
	    public boolean hasNegativeCycle() {
	        return cycle != null;
	    }

	    /**
	     * Returns a negative cycle reachable from the source vertex {@code s}, or {@code null}
	     * if there is no such cycle.
	     * @return a negative cycle reachable from the source vertex {@code s}
	     *    as an iterable of edges, and {@code null} if there is no such cycle
	     */
	    public Iterable<DirectedEdge> negativeCycle() {
	        return cycle;
	    }

	    // by finding a cycle in predecessor graph
	    private void findNegativeCycle() {
	        int V = edgeTo.length;
	        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
	        for (int v = 0; v < V; v++)
	            if (edgeTo[v] != null)
	                spt.addEdge(edgeTo[v]);

	        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
	        cycle = finder.cycle();
	    }

	    /**
	     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
	     * @param  v the destination vertex
	     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
	     *         {@code Double.POSITIVE_INFINITY} if no such path
	     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
	     *         from the source vertex {@code s}
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public double distTo(int v) {
	        validateVertex(v);
	        if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
	        return distTo[v];
	    }

	    /**
	     * Is there a path from the source {@code s} to vertex {@code v}?
	     * @param  v the destination vertex
	     * @return {@code true} if there is a path from the source vertex
	     *         {@code s} to vertex {@code v}, and {@code false} otherwise
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public boolean hasPathTo(int v) {
	        validateVertex(v);
	        return distTo[v] < Double.POSITIVE_INFINITY;
	    }

	    /**
	     * Returns a shortest path from the source {@code s} to vertex {@code v}.
	     * @param  v the destination vertex
	     * @return a shortest path from the source {@code s} to vertex {@code v}
	     *         as an iterable of edges, and {@code null} if no such path
	     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
	     *         from the source vertex {@code s}
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public Iterable<DirectedEdge> pathTo(int v) {
	        validateVertex(v);
	        if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
	        if (!hasPathTo(v)) return null;
	        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
	        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
	            path.push(e);
	        }
	        return path;
	    }

	    // check optimality conditions: either
	    // (i) there exists a negative cycle reachable from s
	    //     or
	    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
	    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
	    private boolean check(EdgeWeightedDigraph G, int s) {

	        // has a negative cycle
	        if (hasNegativeCycle()) {
	            double weight = 0.0;
	            for (DirectedEdge e : negativeCycle()) {
	                weight += e.weight();
	            }
	            if (weight >= 0.0) {
	                System.err.println("error: weight of negative cycle = " + weight);
	                return false;
	            }
	        }

	        // no negative cycle reachable from source
	        else {

	            // check that distTo[v] and edgeTo[v] are consistent
	            if (distTo[s] != 0.0 || edgeTo[s] != null) {
	                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
	                return false;
	            }
	            for (int v = 0; v < G.V(); v++) {
	                if (v == s) continue;
	                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
	                    System.err.println("distTo[] and edgeTo[] inconsistent");
	                    return false;
	                }
	            }

	            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
	            for (int v = 0; v < G.V(); v++) {
	                for (DirectedEdge e : G.adj(v)) {
	                    int w = e.to();
	                    if (distTo[v] + e.weight() < distTo[w]) {
	                        System.err.println("edge " + e + " not relaxed");
	                        return false;
	                    }
	                }
	            }

	            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
	            for (int w = 0; w < G.V(); w++) {
	                if (edgeTo[w] == null) continue;
	                DirectedEdge e = edgeTo[w];
	                int v = e.from();
	                if (w != e.to()) return false;
	                if (distTo[v] + e.weight() != distTo[w]) {
	                    System.err.println("edge " + e + " on shortest path not tight");
	                    return false;
	                }
	            }
	        }

	        StdOut.println("Satisfies optimality conditions");
	        StdOut.println();
	        return true;
	    }

	    // throw an IllegalArgumentException unless {@code 0 <= v < V}
	    private void validateVertex(int v) {
	        int V = distTo.length;
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	    }
	    
	    
	    public double averageSP(File []files,int s,int t)throws IOException 
	    {      double result = 0;
	           int count = 0;
	    	
	    	   for(File file: files) 
	       	   {
	       		String filename = file.getName();
	       		In in = new In(filename);
	       		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
	       		DSPT2 sp2 = new DSPT2();
	       		sp2.BellmanFord(G, s);
	       		if(sp2.distTo(t) != Double.POSITIVE_INFINITY) {
	       		   result += sp2.distTo(t);
	       		   count++;
	       		}
	       	    
	       	   }
	    	   if(result == 0) {
	    		   System.out.println("NO path from vertex "+ s+ " to "+ t);
	    		   return 0;
	    	   }
	    	   
	    	   return (result/count);
	    		
	    }
	    
	    public double minSP(File [] files, int s,int t)
	    {
	    	   ArrayList<Double> SPweight = new ArrayList<Double>();
	    	   for(File file: files) 
	       	   {
	       		String filename = file.getName();
	       		In in = new In(filename);
	       		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
	       		DSPT2 sp2 = new DSPT2();
	            sp2.BellmanFord(G, s);
	       		if(sp2.distTo(t) != Double.POSITIVE_INFINITY) 
	       		   SPweight.add(sp2.distTo(t));
	       	    
	       	   }
	    	   
	    	   if(SPweight.isEmpty()) {
	    		   System.out.println("NO path from vertex "+ s+ " to "+ t);
	    		   return 0;
	    	   }
	    	   
	    	   SPweight.sort(null);
	  
	    	   return SPweight.get(0);
	       
	       }
	    
	    public double maxSP(File [] files, int s,int t)
	    {   
	    	 int count=0;
	    	 ArrayList<Double> SPweight = new ArrayList<Double>();
	    	 for(File file: files) 
	       	 {
	       		String filename = file.getName();
	       		In in = new In(filename);
	       		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
	       		DSPT2 sp2 = new DSPT2();
	            sp2.BellmanFord(G, s);
	       		
	       		if(sp2.distTo(t) != Double.POSITIVE_INFINITY) { 
	       		   SPweight.add(sp2.distTo(t));
	       		   count++;
	       		}
	       	  }
	    	   
	    	  if(SPweight.isEmpty()) {
	    		   System.out.println("NO path from vertex "+ s + " to "+ t);
	    		   return 0;
	    	   }
	    	   
	    	   SPweight.sort(null);
	  
	    	   return SPweight.get(count-1);
	       
	       }
	   
	   public double medianSP(File [] files, int s, int t) 
	   {
		   ArrayList<Double> SPweight = new ArrayList<Double>();
    	   for(File file: files) 
       	   {
       		String filename = file.getName();
       		In in = new In(filename);
       		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
       		DSPT2 sp2 = new DSPT2();
            sp2.BellmanFord(G, s);
       		if(sp2.distTo(t) != Double.POSITIVE_INFINITY) 
       		    SPweight.add(sp2.distTo(t));
       	    
       	   }
    	   
    	   if(SPweight.isEmpty()) {
    		   System.out.println("NO path from vertex "+ s+ " to "+ t);
    		   return 0;
    	   }
    	   
    	   SPweight.sort(null);
    
    	   int size = SPweight.size();
    	   if(size%2 == 0) {
    		   double result = (SPweight.get(size/2)) + (SPweight.get((size/2)-1));
    		   return (result/2);
    		   
    	   }
    	   
    	   return SPweight.get((size/2));
	   }
	   

	

	   
	    
	}

	   	/******************************************************************************
	 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
	 *
	 *  This file is part of algs4.jar, which accompanies the textbook
	 *
	 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
	 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
	 *      http://algs4.cs.princeton.edu
	 *
	 *
	 *  algs4.jar is free software: you can redistribute it and/or modify
	 *  it under the terms of the GNU General Public License as published by
	 *  the Free Software Foundation, either version 3 of the License, or
	 *  (at your option) any later version.
	 *
	 *  algs4.jar is distributed in the hope that it will be useful,
	 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
	 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 *  GNU General Public License for more details.
	 *
	 *  You should have received a copy of the GNU General Public License
	 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
	 ******************************************************************************/


