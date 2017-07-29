package io.github.freuvim.financecontrol.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import io.github.freuvim.financecontrol.database.DAOGastos;
import io.github.freuvim.financecontrol.fragments.GastosFragment;
import io.github.freuvim.financecontrol.fragments.RelatoriosFragment;
import io.github.freuvim.financecontrol.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private int destino;
    private int atual;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        if (parametros != null) {
            if (parametros.getBoolean("intro")) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            destino = parametros.getInt("destino");
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DadosActivity.class));
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (destino == 0) {
            navigationView.getMenu().getItem(0).setChecked(true);
            GastosFragment gf = new GastosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, gf).commit();
            fab.show();
            setTitle("Sua Lista de Gastos");
        } else {
            navigationView.getMenu().getItem(1).setChecked(true);
            RelatoriosFragment rf = new RelatoriosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, rf).commit();
            fab.hide();
            setTitle("Seus Gastos");
        }

        View header = navigationView.getHeaderView(0);
        final SwitchCompat swNight = header.findViewById(R.id.swNight);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        swNight.setChecked(sharedPreferences.getBoolean("NightMode", true));
        if (sharedPreferences.getBoolean("NightMode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        swNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swNight.isChecked()) {
                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putBoolean("NightMode", true);
                    sharedPreferencesEditor.apply();
                    MainActivity.this.recreate();
                } else {
                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putBoolean("NightMode", false);
                    sharedPreferencesEditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    MainActivity.this.recreate();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_gastos) {
            fab.show();
            GastosFragment gf = new GastosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, gf).commit();
            atual = 0;
            setTitle("Sua Lista de Gastos");
        } else if (id == R.id.nav_relatorios) {
            fab.hide();
            RelatoriosFragment rf = new RelatoriosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, rf).commit();
            atual = 1;
            setTitle("Seus Gastos");
        } else if (id == R.id.nav_limpar) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        DAOGastos dao = new DAOGastos(getBaseContext());
                        dao.open();
                        dao.deletaTodos();
                        dao.close();
                        recreate();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        Toast.makeText(MainActivity.this, "Dados Deletados", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Deletar todos os Dados?").setPositiveButton("Sim", dialogClickListener)
                    .setNegativeButton("Não", dialogClickListener).show();
        } else if (id == R.id.nav_sobre) {
            Bundle bundle = new Bundle();
            bundle.putInt("destino", atual);
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
