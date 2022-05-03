//https://www.geeksforgeeks.org/2-satisfiability-2-sat-problem/amp/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class GFG{
    static final int MAX = 100000;

// Data structures used to implement Kosaraju's
// Algorithm. Please refer
// https://www.geeksforgeeks.org/strongly-connected-components/amp/
    @SuppressWarnings("unchecked")
    static List<List<Integer>> adj = new ArrayList();
    @SuppressWarnings("unchecked")
    static List<List<Integer> > adjInv = new ArrayList();
    static boolean[] visited = new boolean[MAX];
    static boolean[] visitedInv = new boolean[MAX];

    static Stack<Integer> s = new Stack<Integer>();

// This array will store the SCC that the
// particular node belongs to
    static int[] scc = new int[MAX];
// counter maintains the number of the SCC
    static int counter = 1;

// Adds edges to form the original graph void

    static void addEdges(int a, int b)
    {
        adj.get(a).add(b);
    }

// Add edges to form the inverse graph

    static void addEdgesInverse(int a, int b)
    {
        adjInv.get(b).add(a);
    }

// For STEP 1 of Kosaraju's Algorithm
    static void dfsFirst(int u)
    {
        if (visited[u])
            return;
        visited[u] = true;
        for(int i = 0; i < adj.get(u).size(); i++)
            dfsFirst(adj.get(u).get(i));
        s.push(u);
    }

// For STEP 2 of Kosaraju's Algorithm

    static void dfsSecond(int u)
    {
        if (visitedInv[u])
            return;
        visitedInv[u] = true;
        for(int i = 0; i < adjInv.get(u).size(); i++)
            dfsSecond(adjInv.get(u).get(i));
        scc[u] = counter;
    }

// Function to check 2-Satisfiability
    static boolean[] is2Satisfiable(int n, int m, int a[], int b[])
    {
        // Adding edges to the graph
        for(int i = 0; i < m; i++)
        {
            // variable x is mapped to x
            // variable -x is mapped to n+x = n-(-x)
            // for a[i] or b[i], addEdges -a[i] -> b[i] AND -b[i] -> a[i]
            if (a[i] > 0 && b[i] > 0)
            {
                addEdges(a[i] + n, b[i]);
                addEdgesInverse(a[i] + n, b[i]);
                addEdges(b[i] + n, a[i]);
                addEdgesInverse(b[i] + n, a[i]);
            }
            else if (a[i] > 0 && b[i] < 0)
            {
                addEdges(a[i] + n, n - b[i]);
                addEdgesInverse(a[i] + n, n - b[i]);
                addEdges(-b[i], a[i]);
                addEdgesInverse(-b[i], a[i]);
            }
            else if (a[i] < 0 && b[i] > 0)
            {
                addEdges(-a[i], b[i]);
                addEdgesInverse(-a[i], b[i]);
                addEdges(b[i] + n, n - a[i]);
                addEdgesInverse(b[i] + n, n - a[i]);
            }
            else
            {
                addEdges(-a[i], n - b[i]);
                addEdgesInverse(-a[i], n - b[i]);
                addEdges(-b[i], n - a[i]);
                addEdgesInverse(-b[i], n - a[i]);
            }
        }
        // STEP 1 of Kosaraju's Algorithm which traverses the original graph
        for(int i = 1; i <= 2 * n; i++)
            if (!visited[i])
                dfsFirst(i);

        // STEP 2 of Kosaraju's Algorithm which
        // traverses the inverse graph. After this,array scc[] stores the corresponding value
        while (!s.isEmpty())
        {
            int top = s.peek();
            s.pop();
            if (!visitedInv[top])
            {
                dfsSecond(top);
                counter++;
            }
        }

        boolean[] res = new boolean[n];
        for(int i = 1; i <= n; i++)
        {
            // For any 2 variable x and -x lie in same SCC
            if (scc[i] == scc[i + n])
            {
                System.out.println("The given expression is unsatisfiable.");
                return null;
            } else  {
                res[i-1] = scc[i] > scc[i + n ];
            }
        }


        System.out.println("The given expression is satisfiable.");
        return res;

        // No such variables x and -x exist which lie in same SCC

    }

// Driver code

    public static void main(String[] args)
    {
//         n is the number of variables
//         2n is the total number of nodes
//         m is the number of clauses
        BufferedReader reader;
        int n =0 ,m = 0 ;
        int[] a= {},b={};
        try {
            reader = new BufferedReader(new FileReader(
                    "/home/nagy/Desktop/NBS/ADS3/src/sat2_4.txt"));
            String line = reader.readLine();
            int counter =0;
            while (line != null) {

                String[] splitted = line.split(" ");

                if(counter == 0){

                     m=new Integer(splitted[1]);
                     n=new Integer(splitted[0]);
                    a = new int[m];
                    b = new int[m];
                }
                else{
                    int m1 =new Integer(splitted[0]);
                    int n1 =new Integer(splitted[1]);
                    a[counter-1]=m1;
                    b[counter-1]=n1;

                }
                line = reader.readLine();
                counter+=1;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 20; i++)
               {
                   adj.add(new ArrayList<Integer>());
                   adjInv.add(new ArrayList<Integer>());
               }

        boolean[] res = is2Satisfiable(n, m, a, b);

        if(res!=null){

            for(boolean bool : res){
                System.out.println(bool);
            }
        }
    }
}