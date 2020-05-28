package personal.project.doculysis;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class getSummaryFromFile extends AppCompatActivity {

    private Button btn_load_file_result_summary;
    private EditText editText_getnumberofsentence;
    private Button btn_get_summary_result;
    private TextView textView_summary_result;
   // private

    ArrayList<Sentence> sentences, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int noOfSentences, noOfParagraphs;
    //FileInputStream in;
    summary_option.SummaryTool summaryTool;

    double[][] intersectionMatrix;
    LinkedHashMap<Sentence,Double> dictionary;

    public String path = null;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    public String forSummary=null;
    String numberofsentences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsummaryfromfile);
        summaryTool=new summary_option.SummaryTool();
        btn_load_file_result_summary = findViewById(R.id.btn_load_file_result_summary);
        editText_getnumberofsentence = findViewById(R.id.editText_getnumberofsentence);
        btn_get_summary_result = findViewById(R.id.btn_get_summary_result);
        textView_summary_result = findViewById(R.id.textView_summary_result);

        //request Permission
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        btn_load_file_result_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberofsentences = editText_getnumberofsentence.toString();
                performFileSearch();
                //getSummaryFromFileFinal();
            }
        });

        btn_get_summary_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryTool.init();
                summaryTool.extractSentenceFromContext(path);
                summaryTool.groupSentencesIntoParagraphs();
                summaryTool.createIntersectionMatrix();
                summaryTool.createDictionary();
                summaryTool.createSummary();
                String ans=summaryTool.printSummary();
                //tv_output.setText(ans);
                textView_summary_result.setText(ans);

            }
        });

    }

//    private void getSummaryFromFileFinal() {
//
//        RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(this);
//
//
//        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
//                                        "https://api.meaningcloud.com/summarization-1.0?key=86fc19c7e512d729752be51058ead27d&txt=" + forSummary + "&sentences=" +numberofsentences ,
//                                        null,
//                                        new Response.Listener<JSONObject>() {
//                                            @Override
//                                            public void onResponse(JSONObject response) {
//                                                try {
//
//                                                    String x;
//                                                    Log.d("InsideLoad", "onResponse: " + response.getString("summary"));
//                                                    x = (response.getString("summary"));
//
//                                                    StringTokenizer st = new StringTokenizer(x,".");
//                                                    StringBuilder sb = new StringBuilder();
//                                                    int count = 1;
//
//                                                    while (st.hasMoreTokens() && count <= Integer.parseInt(numberofsentences)) {
//                                                        sb.append(count+ "->");
//                                                        sb.append(st.nextToken());
//                                                        sb.append("\n");
//                            }
//
//
//                            textView_summary_result.setText(sb.toString());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("getFileForSummary", "onErrorResponse: " + error.getMessage());
//                Toast.makeText(getSummaryFromFile.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        requestQueue.add(jsonobjectRequest);
//
//    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                assert uri != null;
                path = uri.getPath();
                assert path != null;
                path = path.substring(path.indexOf(":") + 1);
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                readText(path);
                if (path != null) {
                    btn_get_summary_result.setEnabled(true);
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_STORAGE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void readText(String  input)
    {
        File file=new File(Environment.getExternalStorageDirectory(),input);
        StringBuilder txt=new StringBuilder();
        //text=new StringBuilder();
        try{
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null)
            {
                txt.append(line);
                txt.append("\n");

            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        forSummary=txt.toString();
        Log.d("myTag",forSummary.toString());
    }

}
