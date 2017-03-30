package com.photon.photonapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;

public class MainActivity extends AppCompatActivity implements MainContract.View{

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
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter=new MainPresenter(this);

        ViewGroup.LayoutParams params = mainLayout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mainLayout.setLayoutParams(params);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(tableLayout(5,5));

    }



    //Method for onClick event of "Update Matrix Size" button.
    @OnClick(R.id.btnUpdateSize)
    public void updateMatrixSize(){
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
    }

    //Method for onClick event of "Calculate LowCostPath" buttosn.
    @OnClick(R.id.btnCalculateLowCost)
    public void calculateMatrixLowCostPath(){
        tvError.setText("");
        if(etRowSize.getText().toString().length()!=0 && etColumnSize.getText().toString().length()!=0 ){
            int rows= Integer.parseInt(etRowSize.getText().toString());
            int columns=Integer.parseInt(etColumnSize.getText().toString());
            //calculateMinCostPath(rows,columns);
            mPresenter.calculateLowCostPath(rows, columns);
        }else{
            tvError.setText("Please enter the row and column sizes");
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
        editText.setInputType(InputType.TYPE_CLASS_NUMBER|TYPE_NUMBER_FLAG_SIGNED);
        editTextsList.add(editText);
        return editText;
    }


    @Override
    public void updateLowCostResult(String result, String pathValue, String path) {

        tvResult.setText(result);
        tvPathValue.setText(pathValue);
        tvPath.setText(path);
    }

    @Override
    public void loadMatrixData(LowCostMatrix matrix) {

        int count=0;
        for( int i = 0 ; i < matrix.getMatrix().length ; i++ ) {
            for ( int j = 0 ; j < matrix.getMatrix()[i].length ; j++ ) {

                if(editTextsList.get(count).getText()!=null && editTextsList.get(count).getText().length()>0){
                    matrix.addElement(i,j,Integer.parseInt(editTextsList.get(count).getText().toString()));
                    count++;
                }else{
                    tvError.setText("Please enter all the row and column values in the matrix");
                    return;
                }

            }
        }

    }
}
