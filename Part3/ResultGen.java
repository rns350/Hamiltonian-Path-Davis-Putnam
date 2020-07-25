import java.util.*;
import java.io.*;

/**
 * Class responsible for generating the final results of the Hamiltonian
 * Path problem after performing DPLL.  It takes the DPLL output as input.
 * It will print the list of which vertices to visit in order, and NO SOLUTION
 * if a solution was not found by DPLL
 * @author Reed Nathaniel Schick
 */
public class ResultGen{
    /**
     * Main method.  It calls the input reading, and then
     * prints the results.
     * @param args name of the DPLL output file is expected
     */
    public static void main(String [] args){
        ArrayList<Entry> entries;
        entries = readInput(args);
        if(entries == null){
            System.out.println("No Solution!\n\n");
            System.exit(1);
        }
        System.out.println("\nSolution: " + entries.toString());
    }

    /**
     * This method opens the DPLL output file and translates the
     * text into the solution. If the first line of the file is 0,
     * it returns null to signify there is no solution.  Otherwise, it first hashes
     * each atom number to it's truth value, and then creates an entry
     * for any Vertices Position pair whose corresponding atom number was
     * hashed to true.  This entry array is sorted and returned.
     * @param args Command line arguments which should specify the DPLL output file
     * @return null if there is no solution, or a sorted entries array if there is one.
     */
    public static ArrayList<Entry> readInput(String [] args){
        if(args.length < 1){
            System.err.println("Error: the program expects a file as input.\nTerminating...\n");
            System.exit(1);
        }

        File f = new File(args[0]);
        try{
            Scanner scan = new Scanner(f);
            String line;
            String[] splitLine;
            line = scan.nextLine();
            HashMap<Integer, Boolean> truths = new HashMap<Integer, Boolean>();
            if(line.equals("0")){ //no solution
                return null;
            }

            while(!(line.equals("0"))){ //hash each atom number to it's truth value
                splitLine = line.split(" ");
                if(splitLine[1].equals("F")){
                    truths.put(Integer.parseInt(splitLine[0]), false);
                }
                else{
                    truths.put(Integer.parseInt(splitLine[0]), true);
                }
                line = scan.nextLine();
            }

            int index;
            ArrayList<Entry> entries = new ArrayList<Entry>();
            while(scan.hasNextLine()){ //make entries for any atom verticies position pair if the atom number hashes to true
                line = scan.nextLine();
                splitLine = line.split(" ");
                index = Integer.parseInt(splitLine[0]);
                if(truths.get(index) == true){
                    entries.add(new Entry(splitLine[1], Integer.parseInt(splitLine[2])));
                }
            }
            Collections.sort(entries); //sort and return.
            return entries;
        }
        catch(FileNotFoundException e){
            System.err.println("Error: The file \"" + args[0] + "\" could not be found.\nTerminating...\n");
            System.exit(1);
        }
        return null;
    }
}