import java.util.*;
/**
 * This class handles all of the clauses in a run of DPLL and controls
 * updating them.  It should also be solely responsible for taking and updating symbols,
 * but no error checking is provided in order to save on run time (this will always
 * be true in terms of this written program)
 * @author Reed Nathaniel Schick
 */
public class Model{
    private ArrayList<Symbol> model = new ArrayList<Symbol>(); //list of altered Symbols
    private ArrayList<Clause> clauses; //list of clauses
    private Boolean hasSolution; //true if a solution exists, false if any clause is false, null otherwise
    private Boolean Updated; //is the model up to date after an add or remove?

    /**
     * a simple constructor that sets the clauses in the model
     * @param clauses a list of clauses in the model
     */
    public Model(ArrayList<Clause> clauses){
        this.clauses = clauses;
    }

    /**
     * this method adds a symbol to the model and sets it
     * to the given value.  It will not update the clauses,
     * but does set the "satisfied" boolean to false
     * @param s the symbol to be added
     * @param setTo the boolean to set s to
     */
    public void add(Symbol s, boolean setTo){
        s.setValue(setTo);
        model.add(s);
        Updated = false;
    }

    public int mostRecent(){
        return model.get(model.size() - 1).getName();
    }

    /**
     * this method removes the most recent symbol that was added to the model
     * and sets it's truth value back to null.  It will not update the clauses
     * but it does set the "updated" boolean to false.
     */
    public void removeRecent(){
        Symbol s = model.remove(model.size() - 1);
        s.setValue(null);
        Updated = false;
    }

    /**
     * this method looks for a solution; e.g. it makes a call
     * to update every clasue within the model and checks if there is
     * a solution.  This will set the "isSolution" boolean.
     */
    private void lookForSolution(){
        Boolean solved = true; //trivially true as we have not seen any clause yet to prove otherwise
        Boolean temp = true; //will hold a boolean denoting if the current clause is true or not
        for(int i = 0; i < clauses.size(); i ++){
            temp = clauses.get(i).updateClause(); //update the clause
            if(temp == null){ //if the clause is indeterminate, then we have yet to solve the model
                solved = null;
            }
            else if(!temp){ //if the clause is false, this model doesn't work; we must try again.
                hasSolution = false;
                Updated = true;
                return;
            }
        }
        Updated = true;
        hasSolution = solved;
    }

    /**
     * this method signifies whether or not the model has a solution.
     * if the model is not updated when this call is made, it will
     * make a call to lookForSolution() first, which updates the clauses in the model.
     * @return true if there is a solution, false if any clause is false, null otherwise
     */
    public Boolean hasSolution(){
        if(model.size() == 0){
            return null;
        }
        if(!Updated){
            lookForSolution();
        }
        return hasSolution;
    }
}