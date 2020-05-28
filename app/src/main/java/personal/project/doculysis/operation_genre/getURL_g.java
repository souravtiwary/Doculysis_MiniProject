package personal.project.doculysis.operation_genre;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import personal.project.doculysis.R;
import personal.project.doculysis.getFile_g;

public class getURL_g extends AppCompatActivity {

    private Button btn_getgenrefromurl_result;
    private EditText editText_geturlforgenreresult;
    private TextView textView_genreofurl;

    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getgenrefromurl);

        btn_getgenrefromurl_result = findViewById(R.id.btn_getgenrefromurl_result);
        editText_geturlforgenreresult = findViewById(R.id.editText_geturlforgenreresult);
        textView_genreofurl = findViewById(R.id.textView_genreofurl);

        btn_getgenrefromurl_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = editText_geturlforgenreresult.getText().toString().trim();
                Toast.makeText(getURL_g.this,"URL: " + url, Toast.LENGTH_SHORT).show();
                getGenreFromUrl();
            }
        });

    }

    private void getGenreFromUrl() {

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.meaningcloud.com/class-1.1?key=86fc19c7e512d729752be51058ead27d&url=" + url + "&model=IPTC_en" ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            Log.d("InsideLoad", "onResponse: " + response.getString("category_list"));
                            JSONArray jsonArray = (response.getJSONArray("category_list"));
                            for(int i= 0; i<jsonArray.length(); i++){
                                sb.append("Label --> " +jsonArray.getJSONObject(i).getString("label")+"\n");
                                sb.append("Relevance--> " + jsonArray.getJSONObject(i).getString("relevance"));
                                sb.append("\n\n");
                            }
                            textView_genreofurl.setText(sb.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getURL_g.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonobjectRequest);

    }


}
