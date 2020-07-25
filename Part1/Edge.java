/**
 * The Edge class represents an edge in a graph.  It is a very simple container
 * created to help during file reading and Vertices translation, and is not used
 * in the actual psuedo graph that is created by this program.
 * 
 * @author Reed Nathaniel Schick
 */
public class Edge{
    private String name; //starting point (name of a graph vertices)
    private String connection; //ending point (name of a graph vertices)

    /**
     * Simple constructor that creates an Edge object
     * 
     * @param name the starting point of the edge (name of a graph vertices)
     * @param connection the ending point of the edge (name of a graph vertices)
     */
    public Edge(String name, String connection){
        this.name = name;
        this.connection = connection;
    }

    /**
     * return the "name" (starting point) of the edge
     * @return name (beginning of edge)
     */
    public String getName(){
        return name;
    }

    /**
     * return the "connection" (ending point) of the edge
     * @return connection (end of edge)
     */
    public String getConnection(){
        return connection;
    }
}