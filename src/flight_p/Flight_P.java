
package Flight_P;
import java.util.*;
import java.io.*;
import java.util.HashMap;

	
public class Flight_P
{	    
                // represents a vertex in the graph.  that stores neighboring vertices
		public class Vertex 
		{
                                //time complexity O(1) for basic operations like get and put
			HashMap<String, Integer> nbrs = new HashMap<>();
                        
		}
		static HashMap<String, Vertex> vtces;//   vtces==nodes
                
                              //The constructor initializes the vtces HashMap, which stores the vertices of the graph.
		public Flight_P() 
		{
			vtces = new HashMap<>();
		}
                    //Returns the number of vertices in the graph
		public int numVetex() 
		{
			return this.vtces.size();
		}
                    //Checks if a vertex with the given name exists.
		public boolean containsVertex(String vname) 
		{
			return this.vtces.containsKey(vname);
		}
                    //Adds a new vertex with the given name.
		public void addVertex(String vname) 
		{
			Vertex vtx = new Vertex();
			vtces.put(vname, vtx);
		}

		public void removeVertex(String vname) 
		{
			Vertex vtx = vtces.get(vname);
			ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

			for (String key : keys) 
			{
				Vertex nbrVtx = vtces.get(key);
				nbrVtx.nbrs.remove(vname);
			}

			vtces.remove(vname);
		}

		public int numEdges() 
		{
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int count = 0;

			for (String key : keys) 
			{
				Vertex vtx = vtces.get(key);
				count = count + vtx.nbrs.size();
			}

			return count / 2;
		}

                    //Checks if an edge exists between two vertices/Airports.
		public boolean containsEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return false;
			}

			return true;
		}

                    //Adds an edge between two vertices with a given weight.
		public void addEdge(String vname1, String vname2, int value) 
		{
			Vertex vtx1 = vtces.get(vname1); 
			Vertex vtx2 = vtces.get(vname2); 

			if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.put(vname2, value);
			vtx2.nbrs.put(vname1, value);
		}

		public void removeEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			//check if the vertices given or the edge between these vertices exist or not
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.remove(vname2);
			vtx2.nbrs.remove(vname1);
		}

		public void display_Map() 
		{
			System.out.println("\t FLIGHT ROUTES MAP");
			System.out.println("\t------------------");
			System.out.println("----------------------------------------------------\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());

			for (String key : keys) 
			{
				String str = key + " =>\n";
				Vertex vtx = vtces.get(key);
				ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());
				
				for (String nbr : vtxnbrs)
				{
					str = str + "\t" + nbr + "\t";
                    			if (nbr.length()<16)
                    			str = str + "\t";
                    			if (nbr.length()<8)
                    			str = str + "\t";
                    			str = str + vtx.nbrs.get(nbr) + "\n";
				}
				System.out.println(str);
			}
			System.out.println("\t------------------");
			System.out.println("---------------------------------------------------\n");

		}
		
		public void display_Airports() 
		{
			System.out.println("\n***********************************************************************\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1;
			for(String key : keys) 
			{
				System.out.println(i + ". " + key);
				i++;
			}
			System.out.println("\n***********************************************************************\n");
		}
			
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////              
		//Checks if there is a path between two vertices.
                
		public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) 
		{
			// DIR EDGE
			if (containsEdge(vname1, vname2)) {
				return true;
			}

			//MARK AS DONE
			processed.put(vname1, true);

			Vertex vtx = vtces.get(vname1);
			ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

			//TRAVERSE THE NBRS OF THE VERTEX
			for (String nbr : nbrs) 
			{

				if (!processed.containsKey(nbr))
					if (hasPath(nbr, vname2, processed))
						return true;
			}

			return false;
		}
		
                // Represents a vertex with its path so far (psf) and cost
		private class DijkstraPair implements Comparable<DijkstraPair> {
                    String vname;
                    String psf;
                    int cost;

                    @Override
                    public int compareTo(DijkstraPair o) {
                        return Integer.compare(this.cost, o.cost);
                }
            }
                
                
            
             // Dijkstra's algorithm to find the shortest path and distance
    public int dijkstra(String src, String des, boolean nan) {
        int val = 0;
            //  store the shortest path from s to destination
        ArrayList<String> shortestPath = new ArrayList<>();
            // HashMap to store the vertices and their corresponding DijkstraPair information
        HashMap<String, DijkstraPair> map = new HashMap<>();
        PriorityQueue<DijkstraPair> priorityQueue = new PriorityQueue<>();
            // Initialization: Setting initial costs and paths for each vertex

        for (String key : vtces.keySet()) {
            DijkstraPair np = new DijkstraPair();
            np.vname = key;
            np.cost = Integer.MAX_VALUE;

                    // If the current vertex is the source, set its cost to 0

            if (key.equals(src)) {
                np.cost = 0;
                np.psf = key;
            }

                    // Add the DijkstraPair to the PriorityQueue and HashMap

            priorityQueue.add(np);
            map.put(key, np);
        }

        while (!priorityQueue.isEmpty()) {
            DijkstraPair rp = priorityQueue.poll();

            if (rp.vname.equals(des)) {
                val = rp.cost;
                shortestPath.add(rp.psf);
                break;
            }

            Vertex v = vtces.get(rp.vname);
            for (String nbr : v.nbrs.keySet()) {
                if (map.containsKey(nbr)) {
                    int cc = map.get(nbr).cost;
                    Vertex k = vtces.get(rp.vname);
                    int nc;

                    if (nan)
                        nc = rp.cost + 120 + 40 * k.nbrs.get(nbr);
                    else
                        nc = rp.cost + k.nbrs.get(nbr);

                    if (nc < cc) {
                        DijkstraPair gp = map.get(nbr);
                        gp.psf = rp.psf + nbr;
                        gp.cost = nc;

                        priorityQueue.remove(gp);
                        priorityQueue.add(gp);
                    }
                }
            }
        }
        
        // Printing the shortest path route and distance
        
        
        System.out.println("Shortest Path : -->> " + shortestPath.get(0));
        
        System.out.println("Shortest Distance: -->> " + val);
        return val;
    }
    //*******

                //-----------------------------------
		// Function to get interchanges in the flight route
		public ArrayList<String> getInterchanges(String str) {
                    
                    ArrayList<String> arr = new ArrayList<>();
                    String res[] = str.split("  ");
                    // Process each segment of the route
                    arr.add(res[0]);
                    int count = 0;
                    for (int i = 1; i < res.length - 1; i++) {
                        int index = res[i].indexOf('~');
                        String s = res[i].substring(index + 1);
                        
                            // Check if the segment represents an interchange

                        if (s.length() == 2) {
                            String prev = res[i - 1].substring(res[i - 1].indexOf('~') + 1);
                            String next = res[i + 1].substring(res[i + 1].indexOf('~') + 1);

                            if (prev.equals(next)) {
                                arr.add(res[i]);
                            } else {
                                arr.add(res[i] + " ==> " + res[i + 1]);
                                i++;
                                count++;
                            }
                        } else {
                            arr.add(res[i]);
                        }
                    }
                    arr.add(Integer.toString(count));
                    arr.add(res[res.length - 1]);
                    return arr;
                }
                
                
	
		public static void Create_Flights_Map(Flight_P g)
		{
			g.addVertex("DHAKA ");
			g.addVertex("SYLHET ");
			g.addVertex("CHATTOGRAM ");
			g.addVertex("COX'S BAZAR ");
			g.addVertex("BARISAL ");
			g.addVertex("KHULNA ");
			g.addVertex("KALKATA ");
			g.addVertex("DHELI ");
			g.addVertex("RAJSHAHI ");
			g.addVertex("RANGPUR ");
                        
			
			
			g.addEdge("DHAKA ", "SYLHET ", 7);
			g.addEdge("DHAKA ", "CHATTOGRAM ", 10);
			g.addEdge("DHAKA ", "COX'S BAZAR ", 15);
			g.addEdge("CHATTOGRAM ", "COX'S BAZAR ", 6);
			g.addEdge("CHATTOGRAM ", "BARISAL ", 7);
			g.addEdge("BARISAL ", "KHULNA ", 4);
			g.addEdge("DHAKA ", "BARISAL ", 8);
			g.addEdge("DHAKA ", "KALKATA ", 20);
                        g.addEdge("KALKATA ", "DHAKA ", 20);
			g.addEdge("KHULNA ", "KALKATA ", 13);
			g.addEdge("DHAKA ", "RAJSHAHI ", 7);
			g.addEdge("KALKATA ", "DHELI ", 18);
			g.addEdge("RAJSHAHI ", "RANGPUR ", 4);
			g.addEdge("DHAKA ", "RANGPUR ", 10);
			
		}
		
                 
		public static String[] printCodelist()
		{
			System.out.println("List of AIRPORTS along with their codes:\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1,j=0,m=1;
			StringTokenizer stname;
			String temp="";
                        // Array to store generated airport codes
			String codes[] = new String[keys.size()];
			char c;
                        // Iterate through each airport
			for(String key : keys) 
			{
				stname = new StringTokenizer(key);
				codes[i-1] = "";
				j=0;
                                // Process each word in the airport name
				while (stname.hasMoreTokens())
				{
				        temp = stname.nextToken();
				        c = temp.charAt(0);
                                        
                                        // Process the first character of each word
				        while (c>47 && c<58)
				        {
				                codes[i-1]+= c;
				                j++;
				                c = temp.charAt(j);
				        }
                                         // Append characters that are not digits to the airport code
				        if ((c<48 || c>57) && c<123)
				                codes[i-1]+= c;
				}
                                // If the code is less than 2 characters, append the second character of the last word
				if (codes[i-1].length() < 2)
					codes[i-1]+= Character.toUpperCase(temp.charAt(1));
				            
				System.out.print(i + ". " + key + "\t");
				if (key.length()<(22-m))
                    			System.out.print("\t");
				if (key.length()<(14-m))
                    			System.out.print("\t");
                    		if (key.length()<(6-m))
                    			System.out.print("\t");
                    		System.out.println(codes[i-1]);
				i++;
				if (i == (int)Math.pow(10,m))
				        m++;
			}
			return codes;
		}
		
                

		public static void main(String[] args) throws IOException
		{
			Flight_P g = new Flight_P();
                        
			Create_Flights_Map(g);
                        
			System.out.println("\t\t\t -----------------------------------------------------");
			System.out.println("\t\t\t ||**** WELCOME TO FLIGHT ROUTE PLANNER SYSTEM *****||");
                        System.out.println("\t\t\t -----------------------------------------------------\n");
			
			BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
			// int choice = Integer.parseInt(inp.readLine());
			//STARTING SWITCH CASE
			while(true)
			{
                                System.out.println("\t\t\t\t------------------------------");
				System.out.println("\t\t\t\t||----AVAILABLE SERVICES----||");
                                 System.out.println("\t\t\t\t------------------------------\n\n");
                                
				System.out.println("1. LIST ALL THE AIRPORTS");
				System.out.println("2. SHOW THE FLIGHTS MAP");
				System.out.println("3. GET SHORTEST DISTANCE FROM  'SOURCE' AIRPORTS TO 'DESTINATION' AIRPORTS");
				
				System.out.println("4. EXIT THE MENU");
				System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 4) : ");
				int choice = -1;
                                
				try {
					choice = Integer.parseInt(inp.readLine());
				} catch(Exception e) {
					// default will handle
				}
                                                                
				System.out.print("\n***********************************************************\n");
				if(choice == 4)
				{
					System.exit(0);
				}
                                
                                
				switch(choice)
				{
				case 1:
					g.display_Airports();
					break;
			
				case 2:
					g.display_Map();
					break;
				
                                case 3:
                                        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
                                        String codes[] = printCodelist();
                                        System.out.println("\n1. TO ENTER SERIAL NO. OF AIRPORTS\n2. TO ENTER CODE OF AIRPORTS\n3. TO ENTER NAME OF AIRPORTS\n");
                                        System.out.println("ENTER YOUR CHOICE:");
                                        int ch = Integer.parseInt(inp.readLine());
                                        int j;

                                        String st1 = "", st2 = "";
                                        System.out.println("ENTER THE SOURCE AND DESTINATION AIRPORTS");
                                        if (ch == 1) {
                                            st1 = keys.get(Integer.parseInt(inp.readLine()) - 1);
                                            st2 = keys.get(Integer.parseInt(inp.readLine()) - 1);
                                        } else if (ch == 2) {
                                            String a, b;
                                            a = (inp.readLine()).toUpperCase();
                                            for (j = 0; j < keys.size(); j++)
                                                if (a.equals(codes[j]))
                                                    break;
                                            st1 = keys.get(j);
                                            b = (inp.readLine()).toUpperCase();
                                            for (j = 0; j < keys.size(); j++)
                                                if (b.equals(codes[j]))
                                                    break;
                                            st2 = keys.get(j);
                                        } else if (ch == 3) {
                                            System.out.println("Enter the SOURCE AIRPORT:");
                                            st1 = inp.readLine();
                                            System.out.println("Enter the DESTINATION AIRPORT:");
                                            st2 = inp.readLine();
                                        } else {
                                            System.out.println("Invalid choice");
                                            System.exit(0);
                                        }

                                        HashMap<String, Boolean> processed = new HashMap<>();

                                                // Convert input to uppercase for case-insensitive comparison
                                                st1 = st1.toUpperCase();
                                                st2 = st2.toUpperCase();

                                                if (!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
                                                    System.out.println("THE INPUTS ARE INVALID");
                                                else
                                                    System.out.println("SHORTEST DISTANCE FROM --> " + st1 + " TO " + st2 + " IS " + g.dijkstra(st1, st2, false) + " KM\n");

                                        break;
					
               	         default:  
                    	        System.out.println("Please enter a valid option! ");
                        	    System.out.println("The options you can choose are from 1 to 4. ");
                            
				}
			}
			
		}	
	}