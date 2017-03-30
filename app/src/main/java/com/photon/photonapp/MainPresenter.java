package com.photon.photonapp;

import android.graphics.Point;

import java.util.Arrays;

/**
 * Created by Manoj on 3/30/2017.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private LowCostMatrix mLowCostMatrix;

    public MainPresenter(MainContract.View view){
        this.mView=view;
    }


    @Override
    public void calculateLowCostPath(int rows, int columns) {

        mLowCostMatrix= new LowCostMatrix(rows,columns);
        int m = mLowCostMatrix.getMatrix().length;
        int n = mLowCostMatrix.getMatrix()[0].length;
        int[][] pathMatrix = new int[m][n];
        int[][] minimumCostPath = new int[m][1];


        mView.loadMatrixData(mLowCostMatrix);
        for (int[] a : mLowCostMatrix.getMatrix()) {
            System.out.println("The array values: " + Arrays.toString(a));
        }


        //Add 1st column elements and indexs of the 1st column to pathMatrix
        for (int i = 0; i < m; i++) {
            minimumCostPath[i][0] = mLowCostMatrix.getElement(i,0);
            pathMatrix[i][0]=i+1;
        }

        for (int j = 0; j < n-1; j++) {
            for (int i = 0; i < m; i++) {

                Point p1=new Point();
                p1.x=i - 1 == -1 ? m - 1 : i - 1; p1.y=j + 1;
                Point p2=new Point();
                p2.x=i; p2.y=j + 1;
                Point p3=new Point();
                p3.x=i + 1 == m ? 0 : i + 1; p3.y=j + 1;

                Point p= minimum(mLowCostMatrix.getMatrix(),p1, p2, p3);
                pathMatrix[i][j+1]=p.x+1;
                minimumCostPath[i][0] = minimumCostPath[i][0] + mLowCostMatrix.getMatrix()[p.x][p.y];
            }
        }


        for (int[] a : minimumCostPath) {
            System.out.println("The min cost through row: " + Arrays.toString(a));
        }

        for (int[] a : pathMatrix) {
            System.out.println("The path of min cost array: " + Arrays.toString(a));
        }

        // Printing actual result
        int index=getMinValueRowIndex(minimumCostPath);
        String result;
        if(minimumCostPath[index][0]>50){
            result="No";
            System.out.println("No");
            return;
        }else{
            result="Yes";
            System.out.println("Yes");
        }

        System.out.println("The lowest cost: " + minimumCostPath[index][0]);
        int[] a=pathMatrix[index];
        for(int i=0;i<a.length;i++){
            if(a[i]>50){

            }
        }
        System.out.println("The lowest cost path: " +  Arrays.toString(a));
        mView.updateLowCostResult(result,""+minimumCostPath[index][0],Arrays.toString(a) );


    }


    public  int getMinValueRowIndex(int[][] numbers) {
        int minValue = numbers[0][0];
        int rowIndex=0;
        for (int j = 0; j < numbers.length; j++) {
            for (int i = 0; i < numbers[j].length; i++) {
                if (numbers[j][i] < minValue ) {
                    minValue = numbers[j][i];
                    rowIndex=j;
                }
            }
        }
        return rowIndex ;
    }




    public Point minimum(int[][] matrix, Point p1, Point p2, Point p3) {
        int a = matrix[p1.x][p1.y];
        int b = matrix[p2.x][p2.y];
        int c = matrix[p3.x][p3.y];

        if (a <= b && a <= c){
            return p1;
        }
        if (b <= a && b <= c) {
            return p2;
        }
        return p3;
    }
}
