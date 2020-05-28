package personal.project.doculysis.readappkication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
;

import personal.project.doculysis.MainActivity;
import personal.project.doculysis.R;


public class Summary extends AppCompatActivity {

    private Button b_load,btnSummary, backButton;
    private TextView tv_output;
    ArrayList<Sentence> sentences, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int noOfSentences, noOfParagraphs;
    //FileInputStream in;
    SummaryTool summaryTool;
    private EditText editText_togetnumber;
    String numberofsent = String.valueOf(5);


    private static final int PERMISSION_REQUEST_STORAGE=1000;
    private static final int READ_REQUEST_CODE=42;
    public String path = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readapp);


        tv_output = findViewById(R.id.tv_output);
        b_load = findViewById(R.id.b_load);
        editText_togetnumber = findViewById(R.id.editText_togetnumber);
        btnSummary = findViewById(R.id.btnSummary);
        backButton = findViewById(R.id.bact_to_mainactivity);

        summaryTool = new SummaryTool();


        //request Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        b_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Summary.this, MainActivity.class));
            }
        });

        if (path == null) {
            Toast.makeText(Summary.this, "Load any file first then click on summary", Toast.LENGTH_LONG).show();
        }

            btnSummary.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    numberofsent = editText_togetnumber.getText().toString();
                    getSummary();
                }
            });

    }

    public void getSummary(){
        summaryTool.init();
        //Toast.makeText(v.getContext(),"Extracting Sentence form file", Toast.LENGTH_SHORT).show();
        summaryTool.extractSentenceFromContext(path);
        //Toast.makeText(v.getContext(),"Extracting Sentence form file", Toast.LENGTH_SHORT).show();
        summaryTool.groupSentencesIntoParagraphs();
        summaryTool.createIntersectionMatrix();
        summaryTool.createDictionary();
        summaryTool.createSummary();
        String ans = summaryTool.printSummary();
        StringTokenizer st = new StringTokenizer(ans,".");
        StringBuilder sb = new StringBuilder();
        int count = 1;

        while (st.hasMoreTokens() && count <= Integer.parseInt(numberofsent)) {
            sb.append(count+ "->  ");
            sb.append(st.nextToken());
            sb.append("\n");
            count++;
        }
        tv_output.setText(sb.toString() );
    }

    private String readText(String  input)
    {
        File file=new File(Environment.getExternalStorageDirectory(),input);
        StringBuilder text=new StringBuilder();
        try{
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null)
            {
                text.append(line);
                text.append("\n");

            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return text.toString();
    }

    //select file  from storage
    public void performFileSearch()
    {

        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent,READ_REQUEST_CODE);
        //btnSummary.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                // if(path.contains("emulated")){
                //    path=path.substring(path.indexOf("0")+1);
                // }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                // tv_output.setText(readText(path));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== PERMISSION_REQUEST_STORAGE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}