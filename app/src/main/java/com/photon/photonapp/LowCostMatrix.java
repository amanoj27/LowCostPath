package com.photon.photonapp;

/**
 * Created by Manoj on 3/24/2017.
 */

public class LowCostMatrix {

    private int rows;
    private int columns;
    private int[][] matrix;

    public LowCostMatrix(int rows, int columns) {
        this.rows=rows;
        this.columns=columns;
        this.matrix=new int[rows][columns];

    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public void addElement(int row, int column, int value) {

        if(isOutOfMatrixLimits(row,column)){
         return;
        }
        matrix[row][column]=value;
    }

    public int getElement(int row, int column) {
        if(isOutOfMatrixLimits(row,column)){
            return -1;
        }
        return matrix[row][column];
    }

    public boolean isOutOfMatrixLimits(int row, int column){
      return  (row<0 ||column<0)||(row>rows-1 ||column>columns-1);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean areValuesOverLimit(){
        for( int i = 0 ; i < matrix.length ; i++ ) {
            for ( int j = 0 ; j < matrix[i].length ; j++ ) {

                if(matrix[i][j]>50){
                    System.out.println( "The array has elements with values >50, not proceeding" );
                    return true;
                }
            }
        }
        return false;
    }
}
