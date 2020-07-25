/**
 * class that represents a symbol used in DPLL. This
 * constitutes a name for the symbol, and a boolean that denotes
 * the truth value assigned to it.  If no truth valuse has been assigned,
 * then it is null.
 * @author Reed Nathaniel Schick
 */
public class Symbol implements Comparable<Symbol>{
    private int name; //name of the symbol (an int)
    private Boolean value = null; //truth value assigned to the symbol

    /**
     * constructor that parses a string for the integer name
     * of the symbol.  No error checking is performed here
     * @param name the String to be parsed
     */
    public Symbol(String name){
        this.name = Integer.parseInt(name);
    }

    /**
     * a constructor that directly takes the integer name of
     * a symbol
     * @param name the name of the symbol
     */
    public Symbol(int name){
        this.name = name;
    }

    /**
     * a symbol is "greater" than another based on it's name.
     * As the name is an integer, this is fairly self explanitory.
     * @return 1 if the calling symbol's name is greater, -1 if it's less, and 0 if they are equal.
     */
    public int compareTo(Symbol that){
        if(this.name > that.name){
            return 1;
        }
        if(this.name < that.name){
            return -1;
        }
        return 0;
    }

    /**
     * method call to set the truth value of the symbol.
     * @param value the boolean value (or null) to assign a symbol.
     */
    public void setValue(Boolean value){
        this.value = value;
    }

    /**
     * getter for a symbol's truth value
     * @return the symbol's truth value.
     */
    public Boolean getValue(){
        return this.value;
    }

    /**
     * getter for a symbol's name
     * @return an integer denoting the symbol's name
     */
    public int getName(){
        return name;
    }

    /**
     * returns a string representation of a symbol.  This is
     * it's name, followed by a space, and then a T if the symbol is assigned true,
     * or an F otherwise.
     */
    public String toString(){
        StringBuilder build = new StringBuilder();
        build.append(name + " ");
        if(value == null || value == false){
            build.append("F\n");
        }
        else{
            build.append("T\n");
        }
        return build.toString();
    }
}