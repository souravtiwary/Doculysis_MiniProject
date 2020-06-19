package personal.project.doculysis.operations_summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import personal.project.doculysis.R;
import personal.project.doculysis.textreadingfromfile.Summary;

public class summary_option extends AppCompatActivity {

    private Button btn_loadfileforsummaryoption;
    private Button btn_planTextforsummaryoption;
    private Button btn_urlforsummaryoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Summary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_option);

        btn_loadfileforsummaryoption = findViewById(R.id.btn_loadfileforsummaryoption);
        btn_planTextforsummaryoption = findViewById(R.id.btn_planTextforsummaryoption);
        btn_urlforsummaryoption = findViewById(R.id.btn_urlforsummaryoption);

        btn_loadfileforsummaryoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary_option.this, Summary.class));
                finish();
            }
        });

        btn_planTextforsummaryoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary_option.this, getSummaryFromText.class));
                finish();
            }
        });

        btn_urlforsummaryoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(summary_option.this, getSummaryFromurl.class));
            }
        });
    }
}