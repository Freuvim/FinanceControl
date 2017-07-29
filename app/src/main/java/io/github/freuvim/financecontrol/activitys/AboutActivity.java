package io.github.freuvim.financecontrol.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import io.github.freuvim.financecontrol.R;

public class AboutActivity extends AppCompatActivity {

    private int destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        if (parametros != null) {
            destino = parametros.getInt("destino");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent it = new Intent(AboutActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("destino", destino);
            it.putExtras(bundle);
            startActivity(it);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(AboutActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("destino", destino);
        it.putExtras(bundle);
        startActivity(it);
        finish();
    }
}
