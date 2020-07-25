import java.util.*;
import java.io.*;
/**
 * Class containing the main method that handles the logic of creating the DPLL input file that
 * encodes a given graph.  The program expects a correctly formatted input file that lists the edges
 * of the graph to be encoded.
 * @author Reed Nathaniel Schick
 */
public class ClauseGen{
    static GraphTranslator graph; //graph to translate
    public static void main(String[] args){
        readInput(args); //file reading
        File DPLL = new File("DPLLInput.txt");
        FileWriter output = null;
        try{ //call every method of the graph translator that produces the needed clauses.  There are five sets of clauses to be produced, and a footer.
            output = new FileWriter(DPLL, false);
            String allVerticesTraversed = graph.allVerticesTraversed();
            output.write(allVerticesTraversed);
            String oneVerticesPerTime = graph.oneVerticesPerTime();
            output.write(oneVerticesPerTime);
            String noIllegalPaths = graph.noIllegalPaths();
            output.write(noIllegalPaths);
            String vertexAtAllTimes = graph.vertexAtAllTimes();
            output.write(vertexAtAllTimes);
            String singleTraversal = graph.singleTraversal();
            output.write(singleTraversal);
            String trailer = graph.addTrailer();
            output.write(trailer);
            output.close();
        }
        catch(IOException e){
            System.err.println("Error: File \"DPLLInput.txt\" could not be created or could not be written to.\nTerminating...");
        }
    }

    /**
     * Method used for file reading.  The method creates the graph in two passes of the data.  The first pass reads
     * each edge in and creates and edge object to store in the program.  It also creates the vertices based on the edges
     * being read in.  Then, the second pass scans through the given edges and connectes the already created vertices based
     * on the data.
     * @param args command line argument specifying the file to read.
     */
    public static void readInput(String[] args){
        if(args.length < 1){
            System.err.println("Error: the program expects a file as input.\nTerminating...\n");
            System.exit(1);
        }

        File f = new File(args[0]);
        try{
            Scanner scan = new Scanner(f);
            String name;
            String connection;
            Vertices temp;
            HashMap<String, Vertices> nodesByName = new HashMap<String, Vertices>(); //will hash a name to it's created vertices
            ArrayList<Edge> pairs = new ArrayList<Edge>(); //used to hold edges as we read them in.
            ArrayList<Vertices> verticesList = new ArrayList<Vertices>(); //used to store Vertices as we create them
            int numVertices = scan.nextInt();
            while(scan.hasNext()){
                name = scan.next();
                connection = scan.next();
                pairs.add(new Edge(name, connection)); //create a new edge and store it
                if(nodesByName.get(name) == null){ //if either the name or connection does not exist yet, create a vertices for it.
                    temp = new Vertices(name);
                    verticesList.add(temp);
                    nodesByName.put(name, temp);
                }
                if(nodesByName.get(connection) == null){
                    temp = new Vertices(connection);
                    verticesList.add(temp);
                    nodesByName.put(connection, temp);
                }
            }

            Edge current;
            Vertices head;
            Vertices tail;
            for(int i = 0; i < pairs.size(); i ++){ //scan through the edges, and use the hashmap to retreive the created vertices and connect them
                current = pairs.get(i);
                head = nodesByName.get(current.getName());
                tail = nodesByName.get(current.getConnection());
                head.addConnection(tail);
            }
            Collections.sort(verticesList); //sort the verticies
            System.out.println(numVertices);
            graph = new GraphTranslator(numVertices, verticesList); //create the graph using the list of connected verticies.
        }
        catch(FileNotFoundException e){
            System.err.println("Error: The file \"" + args[0] + "\" could not be found.\nTerminating...\n");
            System.exit(1);
        }
    }
}