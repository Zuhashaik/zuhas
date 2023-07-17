package edu.princeton.cs.algs4;
import java.io.*;
import java.util.*;

public class DSPT1 {
	    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
	    private Edge[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
	    private IndexMinPQ<Double> pq;    // priority queue of vertices

	    /**
	     * Computes a shortest-paths tree from the source vertex {@code s} to every
	     * other vertex in the edge-weighted graph {@code G}.
	     *
	     * @param  G the edge-weighted digraph
	     * @param  s the source vertex
	     * @throws IllegalArgumentException if an edge weight is negative
	     * @throws IllegalArgumentException unless {@code 0 <= s < V}
	     */
	    public void Dijkstra(EdgeWeightedGraph G, int s) {
	        for (Edge e : G.edges()) {
	            if (e.weight() < 0)
	                throw new IllegalArgumentException("edge " + e + " has negative weight");
	        }

	        distTo = new double[G.V()];
	        edgeTo = new Edge[G.V()];

	        validateVertex(s);

	        for (int v = 0; v < G.V(); v++)
	            distTo[v] = Double.POSITIVE_INFINITY;
	        distTo[s] = 0.0;

	        // relax vertices in order of distance from s
	        pq = new IndexMinPQ<Double>(G.V());
	        pq.insert(s, distTo[s]);
	        while (!pq.isEmpty()) {
	            int v = pq.delMin();
	            for (Edge e : G.adj(v))
	                relax(e, v);
	        }

	        // check optimality conditions
	        assert check(G, s);
	    }

	    // relax edge e and update pq if changed
	    private void relax(Edge e, int v) {
	        int w = e.other(v);
	        if (distTo[w] > distTo[v] + e.weight()) {
	            distTo[w] = distTo[v] + e.weight();
	            edgeTo[w] = e;
	            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
	            else                pq.insert(w, distTo[w]);
	        }
	    }

	    /**
	     * Returns the length of a shortest path between the source vertex {@code s} and
	     * vertex {@code v}.
	     *
	     * @param  v the destination vertex
	     * @return the length of a shortest path between the source vertex {@code s} and
	     *         the vertex {@code v}; {@code Double.POSITIVE_INFINITY} if no such path
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public double distTo(int v) {
	        validateVertex(v);
	        return distTo[v];
	    }

	    /**
	     * Returns true if there is a path between the source vertex {@code s} and
	     * vertex {@code v}.
	     *
	     * @param  v the destination vertex
	     * @return {@code true} if there is a path between the source vertex
	     *         {@code s} to vertex {@code v}; {@code false} otherwise
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public boolean hasPathTo(int v) {
	        validateVertex(v);
	        return distTo[v] < Double.POSITIVE_INFINITY;
	    }

	    /**
	     * Returns a shortest path between the source vertex {@code s} and vertex {@code v}.
	     *
	     * @param  v the destination vertex
	     * @return a shortest path between the source vertex {@code s} and vertex {@code v};
	     *         {@code null} if no such path
	     * @throws IllegalArgumentException unless {@code 0 <= v < V}
	     */
	    public Iterable<Edge> pathTo(int v) {
	        validateVertex(v);
	        if (!hasPathTo(v)) return null;
	        Stack<Edge> path = new Stack<Edge>();
	        int x = v;
	        for (Edge e = edgeTo[v]; e != null; e = edgeTo[x]) {
	            path.push(e);
	            x = e.other(x);
	        }
	        return path;
	    }


	    // check optimality conditions:
	    // (i) for all edges e = v-w:            distTo[w] <= distTo[v] + e.weight()
	    // (ii) for all edge e = v-w on the SPT: distTo[w] == distTo[v] + e.weight()
	    private boolean check(EdgeWeightedGraph G, int s) {

	        // check that edge weights are non-negative
	        for (Edge e : G.edges()) {
	            if (e.weight() < 0) {
	                System.err.println("negative edge weight detected");
	                return false;
	            }
	        }

	        // check that distTo[v] and edgeTo[v] are consistent
	        if (distTo[s] != 0.0 || edgeTo[s] != null) {
	            System.err.println("distTo[s] and edgeTo[s] inconsistent");
	            return false;
	        }
	        for (int v = 0; v < G.V(); v++) {
	            if (v == s) continue;
	            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
	                System.err.println("distTo[] and edgeTo[] inconsistent");
	                return false;
	            }
	        }

	        // check that all edges e = v-w satisfy distTo[w] <= distTo[v] + e.weight()
	        for (int v = 0; v < G.V(); v++) {
	            for (Edge e : G.adj(v)) {
	                int w = e.other(v);
	                if (distTo[v] + e.weight() < distTo[w]) {
	                    System.err.println("edge " + e + " not relaxed");
	                    return false;
	                }
	            }
	        }

	        // check that all edges e = v-w on SPT satisfy distTo[w] == distTo[v] + e.weight()
	        for (int w = 0; w < G.V(); w++) {
	            if (edgeTo[w] == null) continue;
	            Edge e = edgeTo[w];
	            if (w != e.either() && w != e.other(e.either())) return false;
	            int v = e.other(w);
	            if (distTo[v] + e.weight() != distTo[w]) {
	                System.err.println("edge " + e + " on shortest path not tight");
	                return false;
	            }
	        }
	        return true;
	    }

	    // throw an IllegalArgumentException unless {@code 0 <= v < V}
	    private void validateVertex(int v) {
	        int V = distTo.length;
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	    }
	    
	    public double averageSP(File []files,int s,int t)throws IOException 
	    {     
	    	   double result = 0;
	           int count = 0;
	    	
	    	   for(File file: files) 
	       	   {
	       		String filename = file.getName();
	       		In in = new In(filename);
	       		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
	       		DSPT1 sp = new DSPT1();
	       		sp.Dijkstra(G, s);
	       		if(sp.distTo(t) != Double.POSITIVE_INFINITY) {
	       		   result += sp.distTo(t);
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
	       		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
	       		DSPT1 sp = new DSPT1();
	       		sp.Dijkstra(G, s);
	       		if(sp.distTo(t) != Double.POSITIVE_INFINITY) 
	       		   SPweight.add(sp.distTo(t));
	       	    
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
	       		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
	       		DSPT1 sp = new DSPT1();
	       		sp.Dijkstra(G, s);
	       		if(sp.distTo(t) != Double.POSITIVE_INFINITY) { 
	       		   SPweight.add(sp.distTo(t));
	       		   count++;
	       		}
	       	  }
	    	   
	    	  if(SPweight.isEmpty()) {
	    		   System.out.println("NO path from vertex "+ s+ " to "+ t);
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
       		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
       		DSPT1 sp = new DSPT1();
       		sp.Dijkstra(G, s);
       		if(sp.distTo(t) != Double.POSITIVE_INFINITY) 
       		    SPweight.add(sp.distTo(t));
       	    
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

}
