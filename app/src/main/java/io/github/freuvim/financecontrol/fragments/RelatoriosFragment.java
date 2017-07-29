package io.github.freuvim.financecontrol.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.github.freuvim.financecontrol.database.BeanGastos;
import io.github.freuvim.financecontrol.database.DAOGastos;
import io.github.freuvim.financecontrol.holograph.PieGraph;
import io.github.freuvim.financecontrol.holograph.PieSlice;
import io.github.freuvim.financecontrol.R;

public class RelatoriosFragment extends Fragment {

    private float total = 0;
    private List<BeanGastos> lstGastos = null;
    private PieGraph pg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_relatorios, container, false);

        DAOGastos dao = new DAOGastos(getActivity().getBaseContext());
        dao.open();
        lstGastos = dao.selectTipos();
        dao.close();
        ListView listaTipos = rootView.findViewById(R.id.listaTipos);
        ArrayAdapter<BeanGastos> adapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, lstGastos);
        listaTipos.setAdapter(adapter);
        pg = rootView.findViewById(R.id.graph);
        pg.setInnerCircleRatio(170);
        buildgraphs();
        LinearLayout graphLayout = rootView.findViewById(R.id.graphLayout);
        TextView listaVazia = rootView.findViewById(R.id.listaRelatorios_vazia);
        if (lstGastos.size() == 0 || total <= 0) {
            listaVazia.setVisibility(View.VISIBLE);
            graphLayout.setVisibility(View.GONE);
        } else {
            listaVazia.setVisibility(View.GONE);
            graphLayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    public void buildgraphs() {
        DAOGastos dao = new DAOGastos(getActivity().getBaseContext());
        dao.open();
        lstGastos = dao.selectTipos();
        dao.close();

        for (int i = 0; i < lstGastos.size(); i++) {
            PieSlice slice = new PieSlice();
            slice.setValue(lstGastos.get(i).getValor_gasto());
            slice.setGoalValue(slice.getValue() * 999);
            total += lstGastos.get(i).getValor_gasto();

            switch (lstGastos.get(i).getTipo_gasto()) {
                case "Vestuário":
                    slice.setColor(Color.parseColor("#000CFF"));
                    break;
                case "Veículos":
                    slice.setColor(Color.parseColor("#99CC00"));
                    break;
                case "Diversos":
                    slice.setColor(Color.parseColor("#9C27B0"));
                    break;
                case "Alimentação":
                    slice.setColor(Color.parseColor("#FFEE00"));
                    break;
                case "Entretenimento":
                    slice.setColor(Color.parseColor("#FF0000"));
                    break;
                case "Compras Intern.":
                    slice.setColor(Color.parseColor("#00B7FF"));
                    break;
                case "Turismo":
                    slice.setColor(Color.parseColor("#FF6600"));
                    break;
            }
            pg.addSlice(slice);

            pg.setDuration(5000);
            pg.setInterpolator(new AccelerateDecelerateInterpolator());
            pg.animateToGoalValues();

        }
    }
}
