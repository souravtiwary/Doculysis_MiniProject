package personal.project.doculysis.operation_genre;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Objects;

import personal.project.doculysis.R;

public class getText_g extends AppCompatActivity {

    private Button btn_getgenrefromtext_result;
    private EditText editText_gettextforgenreresult;
    private TextView textView_genreoftext;
    ProgressDialog progress;

    String textforgenre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Type of Document");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getgenrefromtext);

        editText_gettextforgenreresult = findViewById(R.id.editText_gettextforgenreresult);
        btn_getgenrefromtext_result = findViewById(R.id.btn_getgenrefromtext_result);
        textView_genreoftext = findViewById(R.id.textView_genreoftext);

        btn_getgenrefromtext_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress=new ProgressDialog(getText_g.this);
                progress.setMessage("Loading....");
                progress.show();
                textforgenre = editText_gettextforgenreresult.getText().toString().trim();
                getGenreFromText();
            }
        });

    }

    private void getGenreFromText() {

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.meaningcloud.com/class-1.1?key=86fc19c7e512d729752be51058ead27d&txt=" + textforgenre + "&model=IPTC_en" ,
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
                                sb.append("Relevance--> " + jsonArray.getJSONObject(i).getString("relevance"));
                                sb.append("\n\n");
                            }

                            textView_genreoftext.setText(sb.toString());
                            progress.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getText_g.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonobjectRequest);

    }
}
