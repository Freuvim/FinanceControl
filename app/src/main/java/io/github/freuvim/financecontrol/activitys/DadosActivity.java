package io.github.freuvim.financecontrol.activitys;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import io.github.freuvim.financecontrol.R;
import io.github.freuvim.financecontrol.database.BeanGastos;
import io.github.freuvim.financecontrol.database.DAOGastos;
import io.github.freuvim.financecontrol.various.DateDialog;
import ru.kolotnev.formattedittext.CurrencyEditText;


public class DadosActivity extends AppCompatActivity {

    private Boolean novo;
    private BeanGastos bean;
    private Spinner spinner;
    private CurrencyEditText valor;
    private EditText txtDate;
    private int dia, mes, ano;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        valor = (CurrencyEditText) findViewById(R.id.valor);
        txtDate = (EditText) findViewById(R.id.txtdate);
        Button btSalvar = (Button) findViewById(R.id.btSalvar);
        Button btExcluir = (Button) findViewById(R.id.btExcluir);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setPrompt("Escolha um tipo:");
        spinner.setAdapter(adapter);

        Intent it = getIntent();
        Bundle parametros = it.getExtras();

        novo = true;

        if (parametros != null) {
            novo = false;
            btExcluir.setVisibility(View.VISIBLE);
            DAOGastos dao = new DAOGastos(getBaseContext());
            bean = new BeanGastos();
            bean.setIdGasto(parametros.getInt("idGasto"));

            dao.open();
            bean = dao.selectUm(bean);
            dao.close();
            String texto = "" + bean.getValor_gasto();
            if (texto.substring(texto.length() - 2, texto.length()).contains(".")) {
                texto = texto + "0";
                valor.setText(texto);
            } else {
                valor.setText(texto);
            }
            spinner.setSelection(tipoInt(bean.getTipo_gasto()));
            String sDia = "" + bean.getDia();
            String sMes = "" + bean.getMes();
            if (sDia.length() < 2) {
                sDia = "0" + sDia;
            }
            if (sMes.length() < 2) {
                sMes = "0" + sMes;
            }
            txtDate.setText(sDia + "/" + sMes + "/" + bean.getAno());
        } else {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String sDay = "" + day;
            String sMonth = "" + (month + 1);
            if (sDay.length() < 2) {
                sDay = "0" + sDay;
            }
            if (sMonth.length() < 2) {
                sMonth = "0" + sMonth;
            }
            String date = sDay + "/" + sMonth + "/" + year;
            txtDate.setText(date);
        }

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valor.getText().toString().equals("") || (txtDate.getText().toString().equals(""))) {
                    Toast.makeText(DadosActivity.this, "Um ou mais campos estão em branco!", Toast.LENGTH_SHORT).show();
                } else {
                    data = txtDate.getText().toString();
                    dia = Integer.parseInt(data.substring(0, 2));
                    mes = Integer.parseInt(data.substring(3, 5));
                    ano = Integer.parseInt(data.substring(6, 10));
                    if (novo) {
                        inserir();
                    } else {
                        alterar();
                    }
                }
            }
        });

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluir();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtDate.getWindowToken(), 0);
                DateDialog dialog = new DateDialog(view);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });
    }

    private void inserir() {
        BeanGastos bean = new BeanGastos();
        DAOGastos dao = new DAOGastos(getBaseContext());
        String real = valor.getText().toString().replace("R$", "");
        if (real.contains(".")) {
            real = real.replace(".", "");
        }
        real = real.replace(",", "");
        String centavos = real.substring(real.length() - 2, real.length());
        real = real.substring(0, real.length() - 2);
        bean.setValor_gasto(Float.parseFloat(real + "." + centavos));
        bean.setTipo_gasto(tipoGasto(spinner));
        bean.setDia(dia);
        bean.setMes(mes);
        bean.setAno(ano);

        dao.open();
        dao.insert(bean);
        dao.close();
        Toast.makeText(DadosActivity.this, "Dados Salvos com Sucesso!", Toast.LENGTH_SHORT).show();
        sair();
    }

    public void alterar() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DAOGastos dao = new DAOGastos(getBaseContext());
                    String real = valor.getText().toString().replace("R$", "");
                    if (real.contains(".")) {
                        real = real.replace(".", "");
                    }
                    real = real.replace(",", "");
                    String centavos = real.substring(real.length() - 2, real.length());
                    real = real.substring(0, real.length() - 2);
                    bean.setValor_gasto(Float.parseFloat(real + "." + centavos));
                    bean.setTipo_gasto(tipoGasto(spinner));
                    bean.setDia(dia);
                    bean.setMes(mes);
                    bean.setAno(ano);

                    dao.open();
                    dao.update(bean);
                    dao.close();
                    Toast.makeText(DadosActivity.this, "Dados Alterados com Sucesso!", Toast.LENGTH_SHORT).show();
                    sair();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(DadosActivity.this);
        builder.setMessage("Alterar os Dados?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    public void excluir() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DAOGastos dao = new DAOGastos(getBaseContext());
                    dao.open();
                    dao.delete(bean);
                    dao.close();
                    Toast.makeText(DadosActivity.this, "Dados Deletados com Sucesso!", Toast.LENGTH_SHORT).show();
                    sair();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(DadosActivity.this);
        builder.setMessage("Deletar os Dados?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    public void sair() {
        startActivity(new Intent(DadosActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(DadosActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String tipoGasto(Spinner s) {
        switch (s.getSelectedItemPosition()) {
            case 0:
                return "Vestuário";
            case 1:
                return "Veículos";
            case 2:
                return "Diversos";
            case 3:
                return "Alimentação";
            case 4:
                return "Entretenimento";
            case 5:
                return "Compras Inter.";
            case 6:
                return "Turismo";
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        sair();
    }

    public int tipoInt(String tipo) {
        switch (tipo) {
            case "Vestuário":
                return 0;
            case "Veículos":
                return 1;
            case "Diversos":
                return 2;
            case "Alimentação":
                return 3;
            case "Entretenimento":
                return 4;
            case "Compras Inter.":
                return 5;
            case "Turismo":
                return 6;
        }
        return 0;
    }

}
