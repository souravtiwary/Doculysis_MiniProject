package personal.project.doculysis;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import personal.project.doculysis.readappkication.Summary;
import personal.project.doculysis.readappkication.SummaryTool;

public class getFile_g extends AppCompatActivity  {

    private Button btn_load_file_result_g;
    private Button btn_get_genre_result;
    private TextView textView_genre_result;
    SummaryTool summaryTool = new SummaryTool();

    String textFromFile =null;
    StringBuilder sb = new StringBuilder();
    String numberofsent =String.valueOf(5);

    private static final int PERMISSION_REQUEST_STORAGE=1000;
    private static final int READ_REQUEST_CODE=42;
    public String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getgenrefromfile);

        btn_get_genre_result = findViewById(R.id.btn_get_genre_result);
        btn_load_file_result_g = findViewById(R.id.btn_load_file_result_g);
        textView_genre_result = findViewById(R.id.textView_genre_result);

        btn_load_file_result_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        btn_get_genre_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSummary();
                getGenreofFile();
            }
        });


    }

    private void getGenreofFile() {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.meaningcloud.com/class-1.1?key=86fc19c7e512d729752be51058ead27d&txt=" + textFromFile + "&model=IPTC_en" ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            StringBuilder sb = new StringBuilder();
                            Log.d("InsideLoad", "onResponse: " + response.getString("category_list"));
                            JSONArray jsonArray = (response.getJSONArray("category_list"));
                            for(int i= 0; i<jsonArray.length(); i++){
                                sb.append("Label --> " +jsonArray.getJSONObject(i).getString("label"));
                                sb.append("\nRelevance--> " + jsonArray.getJSONObject(i).getString("relevance"));
                            }
                            textView_genre_result.setText(sb.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getFile_g.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonobjectRequest);
    }

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
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
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

        int count = 1;

        while (st.hasMoreTokens() && count <= Integer.parseInt(numberofsent)) {
            sb.append(count+ "->  ");
            sb.append(st.nextToken());
            sb.append("\n");
            count++;
        }

        textFromFile = sb.toString();

    }

}
