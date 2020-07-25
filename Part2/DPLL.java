import java.util.*;
import java.io.*;
/** This is the main method where both the file reading and the DPLL algorithm is implemented.
 *  Given a set of symbols and clauses, this program will output a file containing the symbols
 *  and what truth values must be assigned to them for the clauses to all hold.  If there is no
 *  solution, it will simply write a "0" along with the optional footer that it may have read at
 *  the end of the file
 * 
 *  @author Reed Nathaniel Schick
 */
public class DPLL{
    public static int count = 0;
    public static ArrayList<Symbol> symbols = new ArrayList<Symbol>(); //used to hold all of the symbols
    public static ArrayList<Clause> clauses = new ArrayList<Clause>(); //used to hold all of the clauses
    public static Model model; //model variable used during DPLL
    public static String footer; //footer string to add to the end of the output, for communication with part 3
    public static Literal unitClause = null; //used to store a unitClause.  This is found while looking for pure symbols
                                             //to save an extra search through the clauses
   /** Main method that controls the program logic (File reading, DPLL, file output)
    * 
    * @param args command line arguments - should specify the file to read from
    */
    public static void main(String [] args) {
        readInput(args);
        generateResults(runDPLL());
    }

    /** this method is called after DPLL is run.  if DPLL returns true, it prints out each symbol
     *  with their final truth values that DPLL assigned them.  If it returns false, it will not 
     *  print the symbols.  It then prints a 0 and the footer to the original input
     * 
     * @param success - denotes weather or not DPLL found a solution
     */
    public static void generateResults(boolean success){
        File results = new File("DPLLResults.txt"); //create file for writing
        FileWriter output = null;
        try{ //try to open the file and write
            output = new FileWriter(results, false);
            if(!success){ //program failed: print a 0, the footer, and then exit
                output.write("0\n");
                output.write(footer);
                output.close();
                return;
            } 
            Collections.sort(symbols); //program succeded.  print the symbols, a 0, the footer, and then exit
            for(int i = 0; i < symbols.size(); i ++){
                output.write(symbols.get(i).toString());
            }
            output.write("0\n");
            output.write(footer);
            output.close();
            return;
        }
        catch(IOException e){ //throw an exception if the file cannot be opened.
            System.err.println("Error: File \"DPLLInput.txt\" could not be created or could not be written to.\nTerminating...");
        }
    }

    /**Method that actually rund the DPLL algorithm.  This is accomplioshed with a model which is responsible
     * for setting all of the symbols and updating the clauses.  This algorithm will assign truth values on a
     * symbol based on if it is a pure symbol or a unit clause.  If neither exists, it will randomly assign truth values
     * to a single symbol, until either all of the clauses are satisfied, or until the method fails.
     * 
     * @return boolean - signifies success of the algorithm
     */
    public static boolean runDPLL(){
        System.out.print(count + ": ");
        count ++;
        if(symbols.size() < 64){
            System.out.println(model.mostRecent());
        }
        Boolean allSatisfied = model.hasSolution(); //check if the model has a solution, failed, or is indeterminate (also updates the clauses)
        if(allSatisfied == null){ //no clauses are false, but some are still unsatisfied
            Literal pure = getPureSymbolAndUnitClause(); //look for a pure symbol.  This method also puts a unit clause into the global variable if it exists
            if(pure != null){ //there is a pure symbol
                model.add(pure.getSym(), !(pure.isFlipped()));
                symbols.remove(pure.getSym());
                boolean complete = runDPLL(); //add symbol back in case DPLL failed (if it succeded, this doesn't affect it's final truth value)
                symbols.add(pure.getSym());
                if(!complete){
                    model.removeRecent(); //if DPLL failed on this symbol, remove it from the model
                }
                return complete;
            }
            if(unitClause != null){ //there is a unit clause
                Literal checkClause = unitClause; //global variable for unit clause is set while looking for the pure symbol
                model.add(checkClause.getSym(), !(checkClause.isFlipped()));
                symbols.remove(checkClause.getSym());
                boolean complete = runDPLL();
                symbols.add(checkClause.getSym()); //add symbol back in case DPLL failed (if it succeded, this doesn't affect it's final truth value)
                if(!complete){
                    model.removeRecent(); //if DPLL failed on this symbol, remove it from the model
                }
                return complete;
            }
            Symbol first = symbols.get(0); //neither a pure symbol or unit clause exists; test a symbol randomly
            symbols.remove(0);
            model.add(first, true); //try true first
            boolean complete = runDPLL();
            if(complete){ //if it succeds return
                symbols.add(first);
                return complete;
            }
            model.removeRecent(); //other wise try again with false now
            model.add(first, false);
            complete = runDPLL();
            symbols.add(first);
            if(!complete){ //if DPLL failed on this symbol, remove it from the model
                model.removeRecent();
            }
            return complete;
        }
        else if(!allSatisfied){ //one clause is false; there is no solution
            return false;
        }
        return true; //all clauses are true; we did it!
    }

    /**This method looks through the clauses for a pure symbol.  it also will try
     * to find a unit clause while doing this, so that we do not need to make two passes
     * through the unsatisfied clause literals twice in a single run
     * 
     * @return A literal if it represents a pure symbol; null if there are no pure symbols.
     */
    public static Literal getPureSymbolAndUnitClause(){
        boolean[] notPure = new boolean[symbols.size()]; //boolean array saying if a symbol is pure; indexes match with symbol array
        ArrayList<Literal> maybePure = new ArrayList<Literal>(); //array that will hold a literal representation of each symbol; indexes match with symbol array
        HashMap<Symbol, Integer> indexes = new HashMap<Symbol, Integer>(); //hash each vertex to it's index to speed up the runtime of this.
        for(int i = 0; i < symbols.size(); i ++){
            indexes.put(symbols.get(i), i); //has each symbol to it's index.
            maybePure.add(null); //fill literal array with nulls (We don't know whethe the symbol should be flipped or not)
        }

        unitClause = null; //reset global unit clause variable to null for this pass.
        ArrayList<Literal> unsatisfied; //will hold unsatisfied literals from a clause as we loop through all clauses
        Literal current; //unsatisfied literal pulled from a clause
        Literal comparing; //matching literal inside of the maybePure arraylist.
        int index; //index of the current literal's symbol in the arraylists
        for(int i = 0; i < clauses.size(); i ++){
            if(clauses.get(i).isSatisfied() != null && clauses.get(i).isSatisfied()){ //if a clause is satisfied, ignore it
                continue;
            }
            unsatisfied = clauses.get(i).getUnsatisfied();
            for(int j = 0; j < unsatisfied.size(); j ++){ //for each unsatisfied literal, check the maybePure array at the index given
                current = unsatisfied.get(j);             //by the literal's symbol.  if the two literal's match, it may be pure.  If they
                index = indexes.get(current.getSym());    //don't match, the symbol is not pure
                if(!(notPure[index])){
                    comparing = maybePure.get(index);
                    if(comparing == null){
                        maybePure.set(index, new Literal(current.getSym(), current.isFlipped()));
                    }
                    else if (current.isFlipped() != comparing.isFlipped()){
                        notPure[index] = true;
                    }
                }
            }
            if(unsatisfied.size() == 1){ //if there is only one symbol in the clause that doesn't have a truth value, it is a unit clause.
                if(unitClause == null){
                    unitClause = unsatisfied.get(0);
                }
            }
        }

        for(int i = 0; i < symbols.size(); i ++){ //if any symbol is pure, return it.
            if(!notPure[i]){
                if(maybePure.get(i) != null){
                    return maybePure.get(i);
                }
            }
        }
        return null;
    }

    /**
     * method for input reading.  This method will read in all of the symbols and initialize them.
     * it also creates the list of clauses.  Once it is finished, it will initialize the model
     * with the given clauses and return.
     * @param args command line arguments, specifying the file to run DPLL on.
     */
    public static void readInput(String [] args){
        if(args.length < 1){
            System.err.println("Error: the program expects a file as input.\nTerminating...\n");
            System.exit(1);
        }

        File f = new File(args[0]);
        try{
            Scanner scan = new Scanner(f);
            ArrayList<String> clauseLines = new ArrayList<String>();
            HashMap<String, Symbol> symbolsByName = new HashMap<String, Symbol>();
            String[] atoms;
            String currentAtom;
            String currentLine;
            Symbol temp;
            while(scan.hasNextLine()){
                if((currentLine = scan.nextLine()).equals("")){
                    continue;
                }
                if(currentLine.equals("0")){
                    break;
                }
                clauseLines.add(currentLine);
                atoms = currentLine.split(" ");
                for(int i = 0; i < atoms.length; i ++){
                    currentAtom = atoms[i];
                    if(currentAtom.charAt(0) == '-'){
                        currentAtom = currentAtom.substring(1);
                    }
                    if(symbolsByName.get(currentAtom) == null){
                        temp = new Symbol(currentAtom);
                        symbols.add(temp);
                        symbolsByName.put(currentAtom, temp);
                    }
                }
            }

            for(int i = 0; i < clauseLines.size(); i ++){
                clauses.add(new Clause(clauseLines.get(i), symbolsByName));
            }
            model = new Model(clauses);

            StringBuilder footBuilder = new StringBuilder();
            while(scan.hasNextLine()){
                footBuilder.append(scan.nextLine() + "\n");
            }
            footBuilder.deleteCharAt(footBuilder.length() - 1);
            footer = footBuilder.toString();
        }
        catch(FileNotFoundException e){
            System.err.println("Error: The file \"" + args[0] + "\" could not be found.\nTerminating...\n");
            System.exit(1);
        }
    }
}