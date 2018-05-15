package com.savedatatofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "file io.txt";

    private EditText mEdtIn,
                    mEdtFileContent;

    private Button mBtnAdd,
                    mBtnRead,
                    mBtnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtIn = (EditText)findViewById(R.id.edtIn);
        mEdtFileContent = (EditText)findViewById(R.id.edtFileContent);

        mBtnAdd = (Button)findViewById(R.id.btnAdd);
        mBtnRead = (Button)findViewById(R.id.btnRead);
        mBtnClear = (Button)findViewById(R.id.btnClear);

        mBtnAdd.setOnClickListener(btnAddOnClick);
        mBtnRead.setOnClickListener(btnReadOnClick);
        mBtnClear.setOnClickListener(btnClearOnClick);
    }

    private View.OnClickListener btnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileOutputStream fileOut = null;
            BufferedOutputStream bufFileOut = null;

            try {
                fileOut = openFileOutput(FILE_NAME, MODE_APPEND);
                bufFileOut = new BufferedOutputStream(fileOut);
                bufFileOut.write(mEdtIn.getText().toString().getBytes());
                bufFileOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btnReadOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileInputStream fileIn = null;
            BufferedInputStream bufFileIn = null;

            try {
                fileIn = openFileInput(FILE_NAME);
                bufFileIn = new BufferedInputStream(fileIn);

                byte[] bufBytes = new byte[10];

                mEdtFileContent.setText("");

                do {
                    int c = bufFileIn.read(bufBytes);

                    if (c == -1)
                        break;
                    else
                        mEdtFileContent.append(new String(bufBytes), 0, c);
                } while (true);

                bufFileIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btnClearOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FileOutputStream fileOut = null;

            try {
                fileOut = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fileOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
