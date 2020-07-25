import java.util.*;
/**
 * This class encodes a DPLL clause.  A clause consists of a list
 * of literals in the clause (a symbol + whether or not it is flipped),
 * an array of unsatisfied literals, and a boolean denoting whether or not the 
 * clause has been satisfied.  Set to null if this has yet to be determined.
 */
public class Clause{
    private ArrayList<Literal> literals = new ArrayList<Literal>(); //array of literals
    private ArrayList<Literal> unsatisfied = new ArrayList<Literal>(); //array of unsatisfied literals
    private Boolean isSatisfied; //true if any literal is true, false if all are false, null if some literals are yet to be determined.

    /**
     * constructor for a clause.  it takes a clause read in as 
     * a string, and uses a hashmap to find symbols based on the
     * names of literals in the string.  It will initialize each literal
     * and store it in it's literals array.
     * @param clauseLine a string representation of a clause
     * @param symbolsByName hashmap for quick access of various symbols by their names
     */
    public Clause(String clauseLine, HashMap<String, Symbol> symbolsByName) throws NullPointerException{
        String[] atoms = clauseLine.split(" ");
        String currentAtom;
        Symbol toAdd;
        for(int i = 0; i < atoms.length; i ++){
            currentAtom = atoms[i];
            if(currentAtom.charAt(0) == '-'){
                toAdd = symbolsByName.get(currentAtom.substring(1));
                if(toAdd == null){
                    throw new NullPointerException("A symbol inside of a clause did not exist");
                }
                literals.add(new Literal(toAdd, true));
            }
            else{
                toAdd = symbolsByName.get(currentAtom);
                if(toAdd == null){
                    throw new NullPointerException("A symbol inside of a clause did not exist");
                }
                literals.add(new Literal(toAdd, false));
            }
        }
    }

    /**
     * return a toString representation of this clause.  This
     * should be equivalent to the original clauseLine string that
     * was passed into the constructor
     * @return a string representation of the clause.
     */
    public String toString(){
        StringBuilder build = new StringBuilder(literals.size() * 3);
        Literal current;
        for(int i = 0; i < literals.size(); i ++){
            current = literals.get(i);
            if(current.isFlipped()){
                build.append("-");
            }
            build.append(current.getSym().getName() + " ");
        }
        return build.toString();
    }

    /**
     * this method returns copies of the unsatisfied literals in a clause.
     * it expects that updateClause() was already called, as that method
     * fills the unsatisfied array when symbols are altered.  No error checking
     * is provided in order to save on runtime.
     * @return an array filled with clones of every literal used.
     */
    public ArrayList<Literal> getUnsatisfied(){
        ArrayList<Literal> clone = new ArrayList<Literal>();
        for(int i = 0; i < unsatisfied.size(); i ++){
            clone.add(unsatisfied.get(i).clone());
        }
        return clone;
    }

    /**
     * a getter that denotes if the clause is satisfied or not.
     * @return true if any literal is true, false if all are false, or null otherwise.
     */
    public Boolean isSatisfied(){
        return isSatisfied;
    }

    /**
     * this method should be called anytime that a truth value has it's
     * name changed.  The model that contains all of the clauses should be the
     * only way that symbols are updated for correct running of this program, and
     * the model will call to update every clause when need be.
     * @return isSatisfied after it has been updated by this method.
     */
    public Boolean updateClause(){
        Boolean curVal; //current value of the symbol in the current literal being checked.
        Literal curLit; //current literal being checked
        isSatisfied = false; //set this to false and update as we go
        unsatisfied.clear(); //clear the unsatisfied array and refill it.

        for( int i = 0; i < literals.size(); i ++){
            curLit = literals.get(i);
            curVal = curLit.getSym().getValue();
            if(curVal == null){ //some symbol is null; can't determine if the clause is false.
                isSatisfied = null;
                unsatisfied.add(curLit);
            }
            else if(curLit.isFlipped()){
                if(!curVal){
                    isSatisfied = true; //clause is satisfied
                    unsatisfied.clear();
                    break;
                }
            }
            else{
                if(curVal){
                    isSatisfied = true; //clause is satisfied.
                    unsatisfied.clear();
                    break;
                }
            }
        }
        return isSatisfied;
    }
}