package edu.up.cs301.shapefitter;

/**
 * Solver: finds fit for a shape; completed solution by Vegdahl.
 *
 * @author Samone Watkins
 * @version 1/27/20
 */
public class MyShapeSolver extends ShapeSolver {


    /**
     * Creates a solver for a particular problem.
     *
     * @param parmShape the shape to fit
     * @param parmWorld the world to fit it into
     * @param acc       to send notification messages to
     */
    public MyShapeSolver(boolean[][] parmShape, boolean[][] parmWorld, ShapeSolutionAcceptor acc) {
        // invoke superclass constructor
        super(parmShape, parmWorld, acc);
    }

    /**
     * Solves the problem by finding a fit, if possible. The last call to display tells where
     * the fit is. If there is no fit, no call to display should be made--alternatively, a call to
     * undisplay can be made.
     */
    public void solve() {

        int r = 0; //initialized row of solution
        int c = 0; //initialized column of solution
        boolean foundSolution = false; //used later to display final solution coordinates

        //temporary array to compare shape to. Same size as shape.
        boolean[][] tempShape = setTempShape();
        boolean[][] tempArray = new boolean[tempShape.length][tempShape[0].length];

        //Loops through all orientations
        for (Orientation or : Orientation.values()) {

            //loops through all coordinates in the world
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world[i].length; j++) {

                    display(i, j, or); //displays place currently being analyzed

                    //creates a temporary array the size of the shape with setTempArray
                    tempShape.equals(setTempArray(world, shape, i, j));

                    //checks to see if the two arrays are a match, if so saves the location
                    if (doesFit(tempShape, tempArray)) {
                        r = i;
                        c = j;
                        foundSolution = true;
                    }

                } // end world column
            } // end world row
            undisplay();

            //if a solution was found then display where it was
            if (foundSolution) {

                display(r, c, or);
            }
        } // end of orientation loop


    }//end of solve()

    //a method that adjusts the shape array to inculde only the shape drawn
    public boolean[][] setTempShape(){

        boolean foundTop = false;
        boolean foundBottom = false;

        int firstRow = 0;
        int firstColumn = 0;
        int lastRow = shape.length;
        int lastColumn = shape.length;
        int xDimension;
        int yDimension;

        //loops through the shape array trying to find the top and leftmost values
        for(int r = 0; r < shape.length; r++){
            for(int c = 0; c < shape[r].length; c++){
                if(shape[r][c] && !foundTop){
                    firstRow = r;
                    firstColumn = c;
                    foundTop = true;
                }

                if(c < firstColumn){
                    firstColumn = c;
                }
            }
        }

        //loops through the shape array backwards to find the bottom and rightmost values
        for (int x = shape.length - 1; x > -1; x--) {
            for (int y = shape[x].length - 1; y > -1; y--) {
                if (shape[x][y]) {
                    if(!foundBottom){
                        lastRow = x;
                        lastColumn = y;
                        foundBottom = true;
                    }

                    if(lastColumn < y){
                        lastColumn = y;
                    }
                }
            }
        }

        //calculates the x and y dimension of the shape
        yDimension = lastColumn - firstColumn;
        xDimension = lastRow - firstRow;

        //creates tempArray given the new dimensions
        boolean[][] tempArray = new boolean[xDimension][yDimension];

        //fills the new fitted array with the shape from the shape array
        for(int x = firstRow; x <= lastRow; x++){
            for(int y = firstColumn; y <= lastColumn; y++){
                tempArray[x][y] = shape[x][y];
            }
        }

        return tempArray;
    }//end of tempShape


    //method that sets a temporary array to be analyzed from world
    public boolean[][] setTempArray(boolean world[][], boolean tempShape[][], int i, int j) {

        boolean[][] tempArray = new boolean[tempShape.length][tempShape[0].length];

        //fills the tempArray with a piece of world
        for(int x = 0; x < tempArray.length; x++){
            for(int y = 0; y < tempArray[x].length; y++){
                tempArray[x][y] = world[i][j];
            }
        }

        return tempArray;

    }// end of setTempArray


    //check to see where if the shape with fit into the world array
    public boolean doesFit(boolean[][] shape, boolean[][] tempArray) {

        for (int l = 0; l < tempArray.length; l++) {
            for (int k = 0; k < tempArray[l].length; k++) {
                if (shape[l][k] && tempArray[l][k]) {
                    return false;
                }

            }
        }
        return true;
    }


    /**
     * Checks if the shape is well-formed: has at least one square, and has all squares connected.
     *
     * @return whether the shape is well-formed
     */
    public boolean check() {

        boolean isValid = false;
        int[][] tempArray = new int[shape.length][shape[0].length];
        int runner = 1;


        //sets each true in the temp array to a number & each false to 0
        for (int x = 0; x < shape.length; x++) {
            for (int y = 0; y < shape.length; y++) {

                if (shape[x][y]) {
                    tempArray[x][y] = runner;
                    runner++;
                } else {
                    tempArray[x][y] = 0;
                }

            }
        }

        boolean changed = false;

        do {

            changed = false;
            for (int x = 0; x < shape.length; x++) {
                for (int y = 0; y < shape.length; y++) {

                    if (tempArray[x][y] > 0) {

                        if (x < shape.length - 1 && y < shape[x].length - 1) {



                            if (tempArray[x + 1][y] > tempArray[x][y]) {
                                tempArray[x + 1][y] = tempArray[x][y];
                                changed = true;
                            }

                            if (tempArray[x + 1][y] < tempArray[x][y]) {
                                tempArray[x][y] = tempArray[x + 1][y];
                                changed = true;
                            }

                            if (tempArray[x][y + 1] > tempArray[x][y]) {
                                tempArray[x][y + 1] = tempArray[x][y];
                                changed = true;
                            }

                            if (tempArray[x][y + 1] < tempArray[x][y]) {
                                tempArray[x][y] = tempArray[x][y + 1];
                                changed = true;
                            }
                        }

                        if (x < shape.length - 1 && y >= shape[x].length - 1) {
                            if (tempArray[x + 1][y] > tempArray[x][y]) {
                                tempArray[x + 1][y] = tempArray[x][y];
                                changed = true;
                            }

                            if (tempArray[x + 1][y] < tempArray[x][y]) {
                                tempArray[x][y] = tempArray[x + 1][y];
                                changed = true;
                            }
                        }

                        if (x >= shape.length - 1 && y < shape[x].length - 1) {
                            if (tempArray[x][y + 1] > tempArray[x][y]) {
                                tempArray[x][y + 1] = tempArray[x][y];
                                changed = true;
                            }

                            if (tempArray[x][y + 1] < tempArray[x][y]) {
                                tempArray[x][y] = tempArray[x][y + 1];
                                changed = true;
                            }
                        }

                    }
                }

            }
        } while (changed) ;


        for(int x  = 0; x < shape.length; x ++){
            for(int y = 0; y < shape[x].length; y ++){
                if(tempArray[x][y] > 1){
                    return false;
                }
            }
        }
            return isValid;

        } //end of check()

    }//end of MyShapeSolver class

