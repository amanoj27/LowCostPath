package com.photon.photonapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Manoj on 3/24/2017.
 */
public class LowCostMatrixTest {

    LowCostMatrix instance;

    @Before
    public void setUp() throws Exception {
        instance= new LowCostMatrix(5,5);
    }

    @Test
    public void initTest(){
        assertEquals(5,instance.getRows());
        assertEquals(5,instance.getColumns());
    }


    @Test
    public void test_add_element(){
        instance.addElement(1,1,5);
        assertEquals(5,instance.getElement(1,1));

        //test for edge cases
        instance.addElement(0,1,5);
        instance.addElement(-1,1,0);
        instance.addElement(1,-1,15);
        instance.addElement(6,1,5);
        instance.addElement(1,6,5);
        instance.addElement(7,6,5);
    }

    @Test
    public void test_get_element(){
        instance.addElement(0,1,5);
        instance.addElement(1,6,5);
        instance.addElement(1,-1,15);
        instance.addElement(6,1,5);
        instance.addElement(1,6,5);
        instance.addElement(7,6,5);
        assertEquals(5,instance.getElement(0,1));
        assertEquals(-1,instance.getElement(1,6));
        assertEquals(-1,instance.getElement(1,-1));
        assertEquals(-1,instance.getElement(6,1));
        assertEquals(-1,instance.getElement(1,6));
        assertEquals(-1,instance.getElement(7,6));

    }

    @Test
    public void test_get_matrix(){
        instance.addElement(0,1,0);
        int[][] matrix=instance.getMatrix();
//        int[][] expected=new int[][]{{3,4,1,2,8,6},{6,1,8,2,7,4},{5,9,3,9,9,5},{8,4,1,3,2,6},{3,7,2,1,2,3}};
        assertArrayEquals(matrix,new int[5][5]);
    }

}