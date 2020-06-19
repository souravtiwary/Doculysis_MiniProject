package personal.project.doculysis.operation_genre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import personal.project.doculysis.R;
import personal.project.doculysis.getFile_g;

public class genre_option extends AppCompatActivity {

    private Button btn_loadfileforgenreoption;
    private Button btn_planTextforgenreoption;
    private Button btn_urlforgenreoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Type of Document");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_option);

        btn_loadfileforgenreoption = findViewById(R.id.btn_loadfileforgenreoption);

        btn_loadfileforgenreoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(genre_option.this, getFile_g.class));
            }
        });

        btn_planTextforgenreoption = findViewById(R.id.btn_planTextforgenreoption);
        btn_planTextforgenreoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(genre_option.this, getText_g.class));
            }
        });

        btn_urlforgenreoption = findViewById(R.id.btn_urlforgenreoption);
        btn_urlforgenreoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(genre_option.this, getURL_g.class));
            }
        });
    }
}
