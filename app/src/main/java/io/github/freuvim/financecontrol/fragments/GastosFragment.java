package io.github.freuvim.financecontrol.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.github.freuvim.financecontrol.database.BeanGastos;
import io.github.freuvim.financecontrol.database.DAOGastos;
import io.github.freuvim.financecontrol.activitys.DadosActivity;
import io.github.freuvim.financecontrol.R;


public class GastosFragment extends Fragment {

    private List<BeanGastos> lstGastos = null;
    private ListView listaGastos;
    private ArrayAdapter<BeanGastos> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gastos_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gastos, container, false);

        listaGastos = rootView.findViewById(R.id.lstGastos);
        TextView listaVazia = rootView.findViewById(R.id.listaGastos_vazia);

        DAOGastos dao = new DAOGastos(getActivity().getBaseContext());
        dao.open();
        lstGastos = dao.selectRecente();
        dao.close();

        adapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, lstGastos);
        listaGastos.setAdapter(adapter);

        listaGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BeanGastos bean;
                bean = lstGastos.get(position);
                Intent it = new Intent(view.getContext(), DadosActivity.class);
                Bundle parametros = new Bundle();
                parametros.putInt("idGasto", bean.getIdGasto());
                it.putExtras(parametros);
                startActivity(it);
                getActivity().finish();
            }
        });
        if (listaGastos.getCount() == 0) {
            listaGastos.setVisibility(View.GONE);
            listaVazia.setVisibility(View.VISIBLE);
        } else {
            listaGastos.setVisibility(View.VISIBLE);
            listaVazia.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DAOGastos dao = new DAOGastos(getActivity().getBaseContext());
        dao.open();
        switch (item.getItemId()) {
            case R.id.menuRecente:
                lstGastos = dao.selectRecente();
                reload();
                break;
            case R.id.menuAntigo:
                lstGastos = dao.selectAntigo();
                reload();
                break;
            case R.id.menuMaior:
                lstGastos = dao.selectDESC();
                reload();
                break;
            case R.id.menuMenor:
                lstGastos = dao.selectASC();
                reload();
                break;
        }
        item.setChecked(true);
        dao.close();
        return true;
    }

    public void reload() {
        adapter.clear();
        adapter = new
                ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, lstGastos);
        listaGastos.setAdapter(adapter);
    }

}
