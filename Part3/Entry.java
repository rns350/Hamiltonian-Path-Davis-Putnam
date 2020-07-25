/**
 * This class is made simply to connect the name of a graph vertex
 * to it's position in the solution.  This class's compare to is performed
 * on the "position" int, which makes sorting the final entries a simple
 * matter of calling Collections.sort()
 * @author Reed Nathaniel Schick
 */
public class Entry implements Comparable<Entry>{
    String name; //name of the graph vertex
    int position; //it's position in the solution

    /**
     * Constructor that takes the name of a graph vertex and
     * it's position in the solution
     * @param name name of the graph vertex
     * @param position the position of the specified graph vertex in the solution
     */
    public Entry(String name, int position){
        this.name = name;
        this.position = position;
    }

    /**
     * CompareTo compares the positions of an entry to determine
     * what is "greater" or "less than."
     * @return 1 if the calling object is greater, -1 if it is less than, and 0 otherwise
     */
    public int compareTo(Entry other){
        if (this.position > other.position){
            return 1;
        }
        if(this.position < other.position){
            return -1;
        }
        return 0;
    }

    /**
     * returns a string representation of an entry.  This is
     * defined simply as the name of the graph vertices
     * @return name data field of this entry
     */
    public String toString(){
        return this.name;
    }
}