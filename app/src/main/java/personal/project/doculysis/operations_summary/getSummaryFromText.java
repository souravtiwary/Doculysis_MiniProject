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

public  class getSummaryFromText extends AppCompatActivity {

    private EditText editText_gettextforsummaryresult;
    private EditText editText_getnumberofsentence;
    private Button btn_getsummaryfromtext_result;
    private TextView textView_summaryoftext;

    String url = null;
    String numberofsentences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsummaryfromtext);

        editText_gettextforsummaryresult = findViewById(R.id.editText_gettextforsummaryresult);
        editText_getnumberofsentence = findViewById(R.id.editText_getnumberofsentence);
        btn_getsummaryfromtext_result = findViewById(R.id.btn_getsummaryfromtext_result);
        textView_summaryoftext = findViewById(R.id.textView_summaryoftext);

        btn_getsummaryfromtext_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberofsentences = editText_getnumberofsentence.toString();

                url = editText_gettextforsummaryresult.toString().trim();

                Toast.makeText(getSummaryFromText.this, "URL: " + url + "\nnumber of sentence" + numberofsentences, Toast.LENGTH_SHORT).show();
                getSummaryFromurlfinal();
            }
        });
    }

    private void getSummaryFromurlfinal() {

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.meaningcloud.com/summarization-1.0?key=86fc19c7e512d729752be51058ead27d&url=" + url + "&sentences=" + numberofsentences,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String x;
                            Log.d("InsideLoad", "onResponse: " + response.getString("summary"));
                            x = (response.getString("summary"));

                            StringTokenizer st = new StringTokenizer(x, ".");
                            StringBuilder sb = new StringBuilder();
                            int count = 1;

                            while (st.hasMoreTokens() && count <= Integer.parseInt(numberofsentences)) {
                                sb.append(count + "->  ");
                                sb.append(st.nextToken());
                                sb.append("\n");
                            }


                            textView_summaryoftext.setText(sb.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getFileForSummary", "onErrorResponse: " + error.getMessage());
                Toast.makeText(getSummaryFromText.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonobjectRequest);

    }
}
