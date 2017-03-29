import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SudokuDriver class
 * Created by Chris, Rex on 9/29/2016.
 *
 * SudokuDriver contains the main of the program of which to run
 */
public class SudokuDriver {

    /**
     * main is the main method in the driver class that
     * starts the entire program.
     */
    public static void main(String[] args){

        // Initializes space to store the time the program finished
        // and the total time the program ran from start of solving
        // the solution or rejection of the puzzle.
        long endTime;
        long totalTimeMS;
        long hours;
        long minutes;
        long seconds;
        long milli;

        ParseCmdLine parse = new ParseCmdLine();
        Puzzle puzzle1 = new Puzzle(parse.getArgs(args));

        // Fills a 2D array with inputs from a file
        // to create sudoku puzzle to solve
        puzzle1.populatePuzzle();

        System.out.println("This is the original Sudoku puzzle: ");
        puzzle1.displayPuzzle();
        System.out.println();

        System.out.println("Attempting to Solve...");

        // Stores the time the program starts solving the Sudoku Puzzle
        long startTime = System.currentTimeMillis();

        if (puzzle1.backTrack()) {
            System.out.println("Solvable: ");
            puzzle1.displayPuzzle();
            // Time the program finishes solving
            endTime = System.currentTimeMillis();
            // Calculated total time it took to solve the sudoku
            totalTimeMS = endTime - startTime;
            milli = totalTimeMS%1000;
            seconds = (totalTimeMS/1000)%60;
            minutes = (totalTimeMS/(60000))%60;
            hours = (totalTimeMS/(36000000))%24;
            String totalTime = hours + "h " + minutes + "m " + seconds + "s " + milli + "ms";
            System.out.println("Solved in: " + totalTimeMS + "ms (" + totalTime + ")" );
        }
        else {
            // Time the program finishes solving
            endTime = System.currentTimeMillis();
            // Calculated total time the program took to reject the sudoku
            totalTimeMS = endTime - startTime;
            milli = totalTimeMS%1000;
            seconds = (totalTimeMS/1000)%60;
            minutes = (totalTimeMS/(60000))%60;
            hours = (totalTimeMS/(36000000))%24;
            String totalTime = hours + "h " + minutes + "m " + seconds + "s " + milli + "ms";
            System.out.println("No Solution found after: " + totalTimeMS + "ms (" + totalTime + ")" );
        }

    }
}