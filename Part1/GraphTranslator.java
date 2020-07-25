import java.util.*;
/**
 * This class takes an array list of Vertices that represents a graph.  It's
 * methods are used to encode the graph in terms that DPLL can understand and
 * work with.  Each method produces a piece of what is needed to encode the
 * full Hamiltonian path problem.
 * 
 * @author Reed Nathaniel Schick
 */
public class GraphTranslator{
    private ArrayList<Vertices> graph; //graph vertices
    int numVertices; //number of vertices
    
    /**
     * constructor that initializes a graph translator.
     * @param numVertices the number of vertices in the graph
     * @param graph an array of vertices representing a graph
     */
    public GraphTranslator(int numVertices, ArrayList<Vertices> graph){
        this.graph = graph;
        this.numVertices = numVertices;
    }

    /**
     * this method take an array index, corresponding to the vertex at that position,
     * and a time value to produce an integer representation of an atom.  Each vertices
     * has numVertices time values, resulting in numVertices squared atoms. The vertices
     * at index zero gets the first numVertices atoms, the vertices at index one gets
     * the second numVertices atoms, and so on.
     * @param index index of the desired vertices in the arraylist.
     * @param time specifies a time, or position, in the hamiltonian path problem.
     * @return the atom that represents the given index and time.
     */
    public int getAtom(int index, int time){
        return numVertices * index + time;
    }

    /**
     * This method lists all of the atoms in order.  It is essentially a toString
     * and was used during testing
     * @return a String containing a list of all of the atoms in order
     */
    public String listAtoms(){
        StringBuilder build = new StringBuilder(numVertices * numVertices * 2);
        for(int index = 0; index < numVertices; index ++){
            for(int time = 1; time <= numVertices; time ++){
                build.append(getAtom(index, time) + " ");
            }
        }
        build.append("\n");
        return build.toString();
    }

    /**
     * This method produces the first set of clauses needed for the DPLL encoding of
     * the Hamiltonian path problem.  It produces multiple lines, where each one is
     * a list of all of the position indexed atoms for every Vertices.  This is
     * essentially saying that each vertices has to exist at at least one time (position).
     * @return a string containing a line for every Vertices
     */
    public String allVerticesTraversed(){
        StringBuilder build = new StringBuilder(numVertices * numVertices *2);
        for(int index = 0; index < numVertices; index ++){ //select a Vertices
            for(int time = 1; time <= numVertices; time ++){ //append a list of the Vertices at each position
                build.append(getAtom(index, time) + " ");
            }
            build.append("\n");
        }
        return build.toString();
    }

    /**
     * This method produces a set of clauses that will ensure that only one Vertices can exist
     * at each position.  For every pair of atoms that represent two different vertices at the
     * same position, this function creates a line that says -Atom1(time x) v -Atom2(time x).
     * this ensures that only one atom will exist in each position.
     * @return a string where each line is a pair of atoms that are at the same position, saying
     *         that at least one of the two must not be true.
     */
    public String oneVerticesPerTime(){
        StringBuilder build = new StringBuilder(((numVertices * (numVertices - 1))/2) * numVertices * 6);
        for(int time = 1; time <= numVertices; time ++){ //determine the position to work on
            for(int index1 = 0; index1 < numVertices; index1 ++){ //get the first vertices at position "time"
                for(int index2 = index1 + 1; index2 < numVertices; index2 ++){ //get the second vertices at position "time"
                    build.append(-(getAtom(index1, time)) + " ");
                    build.append(-(getAtom(index2, time)) + "\n");
                }
            }
        }
        return build.toString();
    }

    /**
     * This is the method that will produce clauses that actually ensure that the paths of the
     * original graphs are followed.  For each edge that does not exist, this method produces
     * multiple clauses that stipulate that if the start point of the nonexistent edge is true at 
     * time x, then the end point of the nonexistent edge cannot be true at time x + 1.  It will do
     * this for every possible edge between vertices that does not exist.
     * @return a String where the lines represent a set of clauses encoding the illegal paths in the graph, which will ensure
     *         that only legal ones are followed.
     */
    public String noIllegalPaths(){
        StringBuilder build = new StringBuilder((numVertices - 1) * (numVertices - 1) * numVertices/2 * 6);
        boolean [] hasPath;
        Vertices[] connections;
        Vertices current;
        int index;
        for(int index1 = 0; index1 < numVertices; index1 ++){ //get theoretical start point of an edge
            current = graph.get(index1);
            hasPath = new boolean[numVertices]; //indexes will match up with graph index; tells whether a connection with that vertices exists.
            connections = current.getConnections();
            for(int j = 0; j < connections.length; j ++){
                index = graph.indexOf(connections[j]); //get index of each connection in the graph
                hasPath[index] = true; //set that index to "true"
            }
            hasPath[index1] = true; //dont need to encode edges to ourselves; other clauses will ensure that that will not happen
            for(int index2 = 0; index2 < numVertices; index2 ++){ //get theoretical endpoint of an edge
                if(hasPath[index2]){ //if there is a connection, continue
                    continue;
                }
                for(int time = 2; time <= numVertices; time ++){ //if there is no connection, produce clauses saying that we cannot traverse it.
                    build.append(-(getAtom(index1, time - 1)) + " ");
                    build.append(-(getAtom(index2, time)) + "\n");
                }
            }
        }
        return build.toString();
    }

    /**
     * This method produces a set of clauses that will ensure that each position has
     * an atom assigned to it.  it is a list of all atoms at each position line by line,
     * meaning that at least one of those atoms in that position must be true.
     * @return a String where the lines represent set of clauses to ensure that an atom exists at every time vertex
     */
    public String vertexAtAllTimes(){
        StringBuilder build = new StringBuilder(numVertices * numVertices * 2);
        for(int time = 1; time <= numVertices; time ++){ //get the position to work on
            for(int index = 0; index < numVertices; index ++){ //get every vertex at that position
                build.append(getAtom(index, time) + " ");
            }
            build.append("\n");
        }
        return build.toString();
    }

    /**
     * This method produces a set of clauses that will ensure that each vertex is only visited once.
     * This optional clause will speed up DPLL runtime.  Each clause says that if vertex v is true at position x, then
     * it cannot be true at any other position
     * @return a String where the lines represent a set of clauses that ensures that a vertex doesn't exist at two different positions.
     */
    public String singleTraversal(){
        StringBuilder build = new StringBuilder(numVertices * numVertices * (numVertices - 1) * 6);
        for(int index = 0; index < numVertices; index ++){ //select a vertices to work on
            for(int time1 = 1; time1 <= numVertices; time1 ++){ //if it exists at time1...
                for(int time2 = time1+1; time2 <= numVertices; time2 ++){ //then it cannot exist at time2...
                    build.append(-(getAtom(index, time1)) + " ");
                    build.append(-(getAtom(index, time2)) + "\n");
                }
            }
        }
        return build.toString();
    }

    /**
     * This method produces the trailer for the file which encodes what vertex and position that
     * each atom refers to.  starts with a 0 to signify that it is the beginning of the footer.
     * @return a string where each line gives the translation of an atom to it's vertex and position.
     */
    public String addTrailer(){
        StringBuilder build = new StringBuilder(numVertices * numVertices * 8);
        build.append(0 + "\n");
        for(int index = 0; index < numVertices; index ++){
            for(int time = 1; time <= numVertices; time ++){
                build.append(getAtom(index, time) + " ");
                build.append(graph.get(index).getName() + " ");
                build.append(time + "\n");
            }
        }
        build.deleteCharAt(build.length() - 1);
        return build.toString();
    }
}