import java.util.ArrayList;
/**
 * This class represents a literal.  this includes the symbol used
 * in the literal along with whether it's flipped or not.
 * @author Reed Nathaniel Schick
 */
public class Literal{
    Symbol mySymbol; //Symbol object connected to this literal
    boolean isFlipped; //whether or not the truth value of the symbol is flipped

    /**
     * Basic constructor that creates a literal based on a symbol
     * and whether or not the symbol should be flipped.
     * @param mySymbol The symbol used in the literal
     * @param isFlipped Whether or not the symbol is flipped
     */
    public Literal(Symbol mySymbol, boolean isFlipped){
        this.mySymbol = mySymbol;
        this.isFlipped = isFlipped;
    }

    /**
     * method that creates a clone of the Literal and returns it.
     * @return a new literal object with the same variables as the calling literal.
     */
    public Literal clone(){
        return new Literal(mySymbol, isFlipped);
    }

    /**
     * simple getter for the symbol that is used in the literal
     * @return the symbol used by the literal.
     */
    public Symbol getSym(){
        return mySymbol;
    }

    /**
     * simple getter for whether or not the symbol is flipped
     * @return true if the symbol is flipped, false if not
     */
    public boolean isFlipped(){
        return isFlipped;
    }

    /**
     * returns a string representation of the literal.  This is
     * a "-" if the symbol is flipped, followed by the symbol used
     * in the literal.
     * @return a string representation of the literal.
     */
    public String toString(){
        if(isFlipped){
            return "-" + mySymbol.toString();
        }
        return mySymbol.toString();
    }
}