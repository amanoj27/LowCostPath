package com.photon.photonapp;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.btnUpdateSize)
    Button btnUpdateMatrixSize;

    @BindView(R.id.btnCalculateLowCost)
    Button btnCalculateLowCostPath;

    @BindView(R.id.tv_instruction)
    TextView tvInstruction;

    @BindView(R.id.etRowSize)
    EditText etRowSize;

    @BindView(R.id.etColSize)
    EditText etColumnSize;


    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.tvPathValue)
    TextView tvPathValue;

    @BindView(R.id.tvPath)
    TextView tvPath;

    @BindView(R.id.tvError)
    TextView tvError;

    ArrayList<EditText> editTextsList=new ArrayList<>();

    private LowCostMatrix mLowCostMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLowCostMatrix=new LowCostMatrix(5,6);

        //Set listener for button
        btnUpdateMatrixSize.setOnClickListener(this);
        btnCalculateLowCostPath.setOnClickListener(this);


        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mainLayout.setLayoutParams(params);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(tableLayout(5,5));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnUpdateSize:
                tvError.setText("");
                mainLayout.removeAllViews();
                editTextsList.clear();
                if(etRowSize.getText().toString().length()!=0 && etColumnSize.getText().toString().length()!=0 ){
                    int rows= Integer.parseInt(etRowSize.getText().toString());
                    int columns=Integer.parseInt(etColumnSize.getText().toString());
                    mainLayout.addView(tableLayout(rows,columns));
                }else{
                    tvError.setText("Please enter the row and column sizes");
                }

                break;
            case R.id.btnCalculateLowCost:
                tvError.setText("");
                if(etRowSize.getText().toString().length()!=0 && etColumnSize.getText().toString().length()!=0 ){
                    int rows= Integer.parseInt(etRowSize.getText().toString());
                    int columns=Integer.parseInt(etColumnSize.getText().toString());
                    calculateMinCostPath(rows,columns);
                }else{
                    tvError.setText("Please enter the row and column sizes");
                }
                break;
        }


    }


    // Using a TableLayout as it provides you with a neat ordering structure

    private TableLayout tableLayout(int rows, int columns) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        int noOfRows = rows;
        for (int i = 0; i < noOfRows; i++) {
            int rowId = 1 * i;
            tableLayout.addView(createOneFullRow(rowId,columns));
        }
        return tableLayout;
    }


    private TableRow createOneFullRow(int rowId, int columns) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        for (int i = 0; i < columns; i++) {
            tableRow.addView(editText(String.valueOf(rowId + i)));
        }
        return tableRow;
    }

    private EditText editText(String hint) {
        EditText editText = new EditText(this);
        editText.setWidth(100);
        editText.setId(Integer.valueOf(hint));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextsList.add(editText);
        return editText;
    }


    private void calculateMinCostPath(int rows, int columns) {

        mLowCostMatrix= new LowCostMatrix(rows,columns);
        int m = mLowCostMatrix.getMatrix().length;
        int n = mLowCostMatrix.getMatrix()[0].length;
        int[][] pathMatrix = new int[m][n];
        int[][] minimumCostPath = new int[m][1];

        int count=0;
        for( int i = 0 ; i < mLowCostMatrix.getMatrix().length ; i++ ) {
            for ( int j = 0 ; j < mLowCostMatrix.getMatrix()[i].length ; j++ ) {

                if(editTextsList.get(count).getText()!=null && editTextsList.get(count).getText().length()>0){
                    mLowCostMatrix.addElement(i,j,Integer.parseInt(editTextsList.get(count).getText().toString()));
                    count++;
                }else{
                    tvError.setText("Please enter all the row and column values in the matrix");
                    return;
                }

            }
        }

        //Check if starting values are >50;
        if(mLowCostMatrix.areValuesOverLimit()){
            showWarningDialog();
            return;
        }

        //Add 1st column elements and indexs of the 1st column to pathMatrix
        for (int i = 0; i < m; i++) {
            minimumCostPath[i][0] = mLowCostMatrix.getElement(i,0);
            pathMatrix[i][0]=i+1;
        }

        //
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



        tvResult.setText(result);
        System.out.println("The lowest cost: " + minimumCostPath[index][0]);
        tvPathValue.setText(""+minimumCostPath[index][0]);
        int[] a=pathMatrix[index];
        for(int i=0;i<a.length;i++){
            if(a[i]>50){

            }
        }
        System.out.println("The lowest cost path: " +  Arrays.toString(a));
        tvPath.setText(Arrays.toString(a));




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

    private void showWarningDialog() {


    }
}
