import java.util.ArrayList;
/**
 * This class represents a Vertices.  A Vertices includes a name, and a list of the
 * other Vertices that it is connected to.  All of these vertices together constitute a graph
 * @author Reed Nathaniel Schick
 */
public class Vertices implements Comparable<Vertices>{
    private String name; //name of vertices
    private ArrayList<Vertices> connected = new ArrayList<Vertices>(); //connections to other vertices

    /**
     * constructor sets the name of the Vertices
     * @param name name of the newly created Vertices
     */
    public Vertices(String name){
        this.name = name;
    }

    /**
     * Method for adding new connections to this vertices.  Note that this only creates a
     * path from the calling vertices to the vertices given as a parameter, and not vice versa.
     * @param a the vertices that we are adding a connection to
     */
    public void addConnection(Vertices a){
        connected.add(a);
    }

    /**
     * getter for the vertices' name
     * @return A string containing the name of the vertices
     */
    public String getName(){
        return this.name;
    }

    /**
     * method that duplicates the connections array to safely return to the caller.
     * @return a copied array of all of the vertices that the calling vertices is connected to.
     */
    public Vertices[] getConnections(){
        int size = connected.size();
        Vertices[] connections = new Vertices[size];
        for(int i = 0; i < size; i ++){
            connections[i] = connected.get(i);
        }
        return connections;
    }

    /**
     * returns a string representation of a vertices.  This is defined as [name: (list of connections)]     
     * @return A String representation of the vertices
     */
    public String toString(){
        StringBuilder build = new StringBuilder();
        build.append(this.name);
        build.append(": ");
        for (int i = 0; i < connected.size(); i ++){
            build.append(connected.get(i).getName());
            build.append(" ");
        }
        return build.toString();
    }

    /**
     * This compare to method defines equality of two vertices by a comparison of their names.
     * @return positive int if the caller is great, negative int if it is smaller, or 0 if they are equal.
     */
    public int compareTo(Vertices e){
        return this.name.compareTo(e.name);
    }
}