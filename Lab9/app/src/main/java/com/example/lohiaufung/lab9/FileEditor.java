package com.example.lohiaufung.lab9;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileEditor extends AppCompatActivity {
    final static String RECORD_NOT_EXIST = "_THIS_RECORD_NOT_EXIST_";
    private EditText mFileName;
    private EditText mFileContent;
    private Button mSaveBtn;
    private Button mLoadBtn;
    private Button mClearBtn;
    private Button mDeleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        mFileName = (EditText)findViewById(R.id.fileTitle);
        mFileContent = (EditText)findViewById(R.id.fileContent);
        mSaveBtn = (Button)findViewById(R.id.saveBtn);
        mLoadBtn = (Button)findViewById(R.id.loadBtn);
        mClearBtn =(Button)findViewById(R.id.clearBtn);
        mDeleteBtn = (Button)findViewById(R.id.deleteBtn);

        // 为SAVR按钮添加点击事件
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mFileName.getText().toString();
                String context = mFileContent.getText().toString();
                if (title.isEmpty() || context.isEmpty()) {
                    Toast.makeText(view.getContext(), "Empty title or empty context!", Toast.LENGTH_SHORT).show();
                } else {
                    try(FileOutputStream fileOutputStream = openFileOutput(title, MODE_PRIVATE)) {
                        fileOutputStream.write(context.getBytes());
                        Toast.makeText(view.getContext(), "Save successfully!", Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        Log.e("TAG", "Fail to save file");
                    }
                }
            }
        });
        // 为LOAD按钮添加点击事件
        mLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mFileName.getText().toString();
                StringBuilder content = new StringBuilder();;
                if (title.isEmpty() ) {
                    Toast.makeText(view.getContext(), "Empty title!", Toast.LENGTH_SHORT).show();
                } else {
                    try(FileInputStream fileInputStream = openFileInput(title)) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        String line = "";
                        while((line = reader.readLine()) != null) {
                            content.append(line);
                        }
                    } catch (FileNotFoundException ex) {
                        Toast.makeText(view.getContext(), "Fail to load file!", Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        Log.e("TAG", "Fail to load file");
                    } finally {
                        mFileContent.setText(content.toString());
                        Toast.makeText(view.getContext(), "Load successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // 添加CLEAR按钮点击响应
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileContent.setText("");
            }
        });
        // 为DELETE按钮添加点击响应
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mFileName.getText().toString();
                if (title.isEmpty() ) {
                    Toast.makeText(view.getContext(), "Empty title!", Toast.LENGTH_SHORT).show();
                } else {
                    File dir = getFilesDir();
                    File file = new File(dir, title);
                    if (file.exists()) {
                        file.delete();
                    }
                    Toast.makeText(view.getContext(), "Delete successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
