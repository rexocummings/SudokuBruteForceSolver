import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Chris, Rex, Connor on 9/29/2016.
 *
 * Puzzle class deals with creating a puzzle
 * based on the text file read by the parser
 * and solving the puzzle by brute force and
 * checks to verify correctness.
 */
public class Puzzle {

    private String fileName = "";
    private String comment = "";
    private int width = 0;
    private int height = 0;
    private int size = width*height;
    private int grid[][];

    /**
     * Constructor for puzzle object
     */
    public Puzzle(String fileName){
        this.fileName = fileName;
    }

    /**
     * Reads the input file and generates a 2D-Array of ints
     * that contains all of the numbers that make up the sudoku
     */
    public void populatePuzzle() {
        try {

            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line = "";

            if((line = br.readLine()) != null){
                if(line.startsWith("c")){
                    comment = line;
                    try {
                        width = Integer.parseInt(br.readLine());
                        height = Integer.parseInt(br.readLine());
                        if ((width < 0) || (height < 0)) {
                            System.out.println("width and height need to be positive ints");
                            System.exit(0);
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.println("File needs row and column to be an integer.");
                        System.exit(0);
                    }

                    System.out.println("\nFile comment:" + comment.substring(1));

                }else{
                    try {
                        width = Integer.parseInt(line);
                        height = Integer.parseInt(br.readLine());
                        if ((width < 0) || (height < 0)) {
                            System.out.println("width and height need to be positive ints");
                            System.exit(0);
                        }
                    }
                    catch(NumberFormatException e){
                        System.out.println("File needs row and column to be an integer.");
                        System.exit(0);
                    }
                }
            }else{
                System.err.println("The file you are trying to ready is empty! Please try again with a different file.");
                System.exit(0);
            }

            System.out.println("The width is: " + width);
            System.out.println("The height is: " + height + "\n");

            size = width * height;

            grid = new int[size][size];

            for(int i = 0; i < grid.length; i++) {
                String[] rowOfNums = br.readLine().trim().split("\\s+");   // ("\\s+") if " " doesn't work
                for(int j = 0; j < grid.length; j++) {
                    grid[i][j] = Integer.parseInt(rowOfNums[j]);
                }
            }
            br.close();

        } catch(FileNotFoundException e) {
            System.err.println("Can not open the file: "+ fileName);
        } catch(IOException e) {
            System.err.println("Error reading the file: " + fileName);
        } catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("The grid in the .txt file does not match dimensions.");
            System.exit(-1);
        }
    }

    /**
     * Checks the current row for duplicates
     *
     * @param currentRow the row being tested
     * @return True for duplicates, False otherwise
     */
    public boolean checkRow(int currentRow) {
        Set<Integer> rowSet = new HashSet<Integer>();
        for (int colPosition = 0; colPosition < size; colPosition++) { //iterate through row
            //if the value is outside the bounds, the row is invalid
            if (grid[currentRow][colPosition] > size || grid[currentRow][colPosition] < 0)
                return true;
            //if the value has already been entered in the set, it is a duplicate
            //so the row is invalid
            if (grid[currentRow][colPosition] != 0)
                if (rowSet.add(Integer.valueOf(grid[currentRow][colPosition])) == false)
                    return true;
        }
        return false;
    }

    /**
     * Checks the current column for duplicates
     *
     * @param currentCol the column being tested
     * @return True for duplicates, False otherwise
     */
    public boolean checkCol(int currentCol) {
        Set<Integer> colSet = new HashSet<Integer>();
        for (int rowPosition = 0; rowPosition < size; rowPosition++) { //iterate through column
            //if the value is outside the bounds, the column is invalid
            if (grid[rowPosition][currentCol] > size || grid[rowPosition][currentCol] < 0)
                return true;
            //if the value has already been entered in the set, it is a duplicate
            //so the column is invalid
            if (grid[rowPosition][currentCol] != 0)
                if (colSet.add(Integer.valueOf(grid[rowPosition][currentCol])) == false)
                    return true;
        }
        return false;
    }

    /**
     * Checks all boxes inside the grid for potential duplicate numbers. Is one of three checks,
     * along with rows and columns, necessary to satisfy to complete the puzzle.
     *
     * @return true if puzzle has duplicates, false if it does not
     */
    public boolean checkBoxes() {
        Set<Integer> set = new HashSet<Integer>();
        int rowStart = 0, colStart = 0;
        int rowEnd = height;
        int colEnd = width;

        //loop runs while in the context of the grid, bounded by the right most side and bottom side
        while (rowEnd <= grid[0].length && colEnd <= grid.length) {
            //iterate through the box, as designated by the height and width, and
            //add all elements to a 1 dimensional testArray for which to test for duplicates
            for(int r = rowStart; r < rowEnd; r++) {
                for(int c = colStart; c < colEnd; c++) {
                    //if the value is out of bounds, the box is invalid
                    if (grid[r][c] > size || grid[r][c] < 0)
                        return true;
                    //if the value has already been entered into the set, it
                    //is a duplicate so the box is invalid
                    if (grid[r][c] != 0)
                        if (set.add(Integer.valueOf(grid[r][c])) == false)
                            return true;
                }
            }

            //reset the rows to go down the grid and go to the next box
            rowStart = rowEnd;
            rowEnd = rowEnd + height;
            set.clear();

            //if we've reached the bottom of the grid, jump back to the top and start iterating through
            //the boxes to the right of the grid
            if(rowEnd > grid[0].length) {
                rowStart = 0;
                rowEnd = height;
                colStart = colEnd;
                colEnd = colEnd + width;
            }
        }

        return false;
    }

    /**
     * Check if all the rows contain duplicate or not.
     *
     * @return True if there are duplicates and false otherwise.
     */
    public boolean checkAllRows() {
        boolean check = false;

        for (int currentRow = 0; currentRow < grid.length; currentRow++) {
            if(checkRow(currentRow))
                check = true;
        }

        return check;
    }

    /**
     * Check if all the columns contain duplicate or not.
     *
     * @return True if there are duplicates and false otherwise.
     */
    public boolean checkAllCols() {
        boolean check = false;

        for (int currentCol = 0; currentCol < grid.length; currentCol++) {
            if(checkCol(currentCol))
                check = true;
        }

        return check;
    }

    /**
     * Check to see if a puzzle is solvable.
     *
     * @return True if the the puzzle is a valid solution, false otherwise.
     */
    public boolean isCorrect() {
        boolean check = false;

        //Create efficient checkRows&&Columns by using just one for loop for both.
        if(!checkAllRows() && !checkAllCols() && !checkBoxes())
            check = true;
        return check;
    }

    /**
     * Check to see if a puzzle is solvable.
     *
     * @return True if the the puzzle is a valid solution, false otherwise.
     */
    public boolean isComplete() {
        if (isCorrect() == false)
            return false;
        else {
            for(int r = 0; r < width*height; r++){
                for(int c = 0; c < width*height; c++){
                    if(grid[r][c] == 0)
                        return false;
                }
            }
            return true;
        }

    }

    /**
     * getNextCell - Get the next cell in a systematic manner.
     *
     * @return an array of coordinates of next cell.
     */
    public int[] getNextCell() {
        int [] nextCoords = {0,0};

        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c ++) {
                if (grid[r][c] == 0) { // Check if cell is not filled in
                    nextCoords[0] = r;
                    nextCoords[1] = c;

                    return nextCoords;
                }
            }
        }

        return nextCoords;
    }

    /**
     * getValues - Get values that are viable
     *
     * @return an ArrayList of viable cell values.
     */
    public ArrayList<Integer> getValues(int[] coords) {

//        System.out.println(coords[0] + " " + coords[1]);

        //creates array of flags that represent the possible numbers for the cell
        int[] flagList = new int[height*width];

        for (int i = 0; i < flagList.length; i++)
            flagList[i] = 1;

        //check row for possible values
        for(int colPosition = 0; colPosition < size; colPosition++) {
            if (grid[coords[0]][colPosition] > 0 && grid[coords[0]][colPosition] <= height*width)
                flagList[grid[coords[0]][colPosition]-1] = -1;
        }

        //check column for possible values
        for (int rowPosition = 0; rowPosition < size; rowPosition++) {
            if (grid[rowPosition][coords[1]] > 0 && grid[rowPosition][coords[1]] <= height*width)
                flagList[grid[rowPosition][coords[1]]-1] = -1;
        }

        //check boxes for possible values
        int boxRowStart = coords[0] / height;
        int boxColStart = coords[1] / width;
        int startRow = 0, startCol = 0, endRow = 0, endCol = 0;
        //integer division above, it is intended to be that way

        //the following few lines of code establish the boundaries for the box containing the given cell.
        startRow = boxRowStart * height;
        endRow = startRow + height - 1;

        startCol = boxColStart * width;
        endCol = startCol + width - 1;

        //using the found locations of the box containing the cell, run through the box and tag the flagList accordingly
        for (int rowPosition = startRow; rowPosition <= endRow; rowPosition++) {
            for (int colPosition = startCol; colPosition <= endCol; colPosition++) {
                if (grid[rowPosition][colPosition] > 0 && grid[rowPosition][colPosition] <= height*width)
                    flagList[grid[rowPosition][colPosition]-1] = -1;
            }
        }

//        //NEXT CHECK BY PRINT START ROW AND START COL
//        for(int i : flagList)
//            System.out.print(i + " ");

        ArrayList<Integer> finalFlagList = new ArrayList<Integer>();

        for (int i = 0; i < flagList.length; i++) {
            if (flagList[i] == 1) {
                finalFlagList.add(i+1);
            }
        }

//        System.out.println(finalFlagList);

        return finalFlagList;
    }

    /**
     * backTrack - Checks an individual cell recursively and its subcomponents to
     *             verify if the cell value is a correct input.
     *
     * @return true if puzzle is correct, false otherwise.
     */
    public boolean backTrack() {
        if (isComplete()) {
            return true;
        } else {

            int [] coords = getNextCell();

            int r = coords[0];
            int c = coords[1];

            // Pre-processing
            ArrayList<Integer> values = getValues(coords);

            while (values.size() != 0){
                //int i = 0;
                int currentValue;

                currentValue = values.get(0);
                grid[r][c] = currentValue;

                if(isCorrect() && backTrack()) {
                    return true;
                }
                else {
                    grid[r][c] = 0; // Reset cell to 0.
                }
                values.remove(0);
                //i++;

            }
        }
        return false;
    }

    /**
     * Input a puzzle and solve, if possible, using a recursive
     * brute force algorithm.
     * Referred to code here:
     * http://stackoverflow.com/questions/15182546/code-explanation-of-sudoku-solver
     *
     * @return True if puzzle can be solved, false otherwise.
     */
    public boolean solvePuzzle() {
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c ++) { //Go cell by cell
                if (grid[r][c] == 0) { // Check if cell is not filled in
                    for(int i = 1; i <= size; i++) { //guess 1 to max
                        grid[r][c] = i;
                        if (isCorrect() && solvePuzzle()) {
                            return true;
                        }
                        else {
                            grid[r][c] = 0; // Reset cell to 0.
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * printRow is a helper function used to display the finished puzzle
     *
     * @param row a one dimensional array of ints that is an interior
     * array in the 2d array grid
     */
    public static void printRow(int[] row) {
        for (int i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

    /**
     * displayPuzzle prints out the puzzle in the console for the user to see.
     * Uses helper function printRow(int[] row)
     */
    public void displayPuzzle() {
        for(int[] row : grid)
            printRow(row);
    }

    /**
     * Getter and setter methods
     */
    public String getFileName() {
        return fileName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }
}