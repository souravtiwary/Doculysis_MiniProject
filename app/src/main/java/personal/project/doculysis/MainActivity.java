package personal.project.doculysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import personal.project.doculysis.operation_genre.genre_option;
import personal.project.doculysis.operations_summary.summary_option;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_summary;
    private Button btn_genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_genre = findViewById(R.id.btn_get_genre_mainA);
        btn_genre.setOnClickListener(this);

        btn_summary = findViewById(R.id.btn_get_summary_mainA);
        btn_summary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_summary_mainA:
                startActivity(new Intent(MainActivity.this, summary_option.class));

                break;
            case R.id.btn_get_genre_mainA:
                startActivity(new Intent(MainActivity.this, genre_option.class));
                break;
        }
    }
}
