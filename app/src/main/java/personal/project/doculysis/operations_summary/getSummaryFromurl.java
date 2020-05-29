package personal.project.doculysis.operations_summary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import personal.project.doculysis.R;
import personal.project.doculysis.getSummaryFromFile;

public class getSummaryFromurl extends AppCompatActivity {

    private EditText editText_geturlforsummaryresult;
    private EditText editText_getnumberofsentence;
    private Button btn_getsummaryfromurl_result;
    private TextView textView_summaryofurl;

    String url = null;
    String numberofsentences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsummaryfromurl);

        editText_geturlforsummaryresult = findViewById(R.id.editText_geturlforsummaryresult);
        editText_getnumberofsentence = findViewById(R.id.editText_getnumberofsentence);
        btn_getsummaryfromurl_result = findViewById(R.id.btn_getsummaryfromurl_result);
        textView_summaryofurl = findViewById(R.id.textView_summaryofurl);

        btn_getsummaryfromurl_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberofsentences = editText_getnumberofsentence.getText().toString();

                url = editText_geturlforsummaryresult.getText().toString().trim();

                Toast.makeText(getSummaryFromurl.this, "URL is added to the app wait for moment... ",Toast.LENGTH_SHORT).show();
                getSummaryFromurlfinal();
            }
        });
    }

    private void getSummaryFromurlfinal() {

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.meaningcloud.com/summarization-1.0?key=86fc19c7e512d729752be51058ead27d&url=" + url + "&sentences=" +numberofsentences ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String x;
                            Log.d("InsideLoad", "onResponse: " + response.getString("summary"));
                            x = (response.getString("summary"));
                            x =  x.replace("[...]", " ");

                            StringTokenizer st = new StringTokenizer(x,".");
                            StringBuilder sb = new StringBuilder();
                            int count = 1;

                            while (st.hasMoreTokens() && count <= Integer.parseInt(numberofsentences)) {
                                sb.append(count+ "->  ");
                                sb.append(st.nextToken());
                                sb.append("\n");
                                count++;
                            }


                            textView_summaryofurl.setText(sb.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getFileForSummary", "onErrorResponse: " + error.getMessage());
                Toast.makeText(getSummaryFromurl.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonobjectRequest);

    }
}
