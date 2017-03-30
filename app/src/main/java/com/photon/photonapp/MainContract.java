package com.photon.photonapp;

/**
 * Created by Manoj on 3/30/2017.
 */

public interface MainContract {

    interface View{

       void  updateLowCostResult(String result, String s, String toString);
       void loadMatrixData(LowCostMatrix matrix);

    }

    interface Presenter{

        void calculateLowCostPath(int rows, int columns);
    }
}
