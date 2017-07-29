package io.github.freuvim.financecontrol.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAOGastos {
    private SQLiteDatabase banco;
    private GerenciaBanco gerenciaBanco;

    // Columns
    private static final String ID_GASTO = "id_gasto";
    private static final String TIPO_GASTO = "tipo_gasto";
    private static final String VALOR_GASTO = "valor_gasto";
    private static final String DIA = "dia";
    private static final String MES = "mes";
    private static final String ANO = "ano";

    private static final String[] todasAsColunas =
            {
                    ID_GASTO, TIPO_GASTO, VALOR_GASTO, DIA, MES, ANO
            };

    // Table
    private static final String TABELA_GASTOS = "gastos";

    public DAOGastos(Context contexto) {
        gerenciaBanco = new GerenciaBanco(contexto);
    }

    public void open() throws SQLException {
        banco = gerenciaBanco.getWritableDatabase();
    }

    public void close() {
        gerenciaBanco.close();
    }

    public void insert(BeanGastos item) {
        ContentValues valores = new ContentValues();
        valores.put(TIPO_GASTO, item.getTipo_gasto());
        valores.put(VALOR_GASTO, item.getValor_gasto());
        valores.put(DIA, item.getDia());
        valores.put(MES, item.getMes());
        valores.put(ANO, item.getAno());

        banco.insert(TABELA_GASTOS, null, valores);
    }

    public void update(BeanGastos item) {
        ContentValues valores = new ContentValues();

        valores.put(ID_GASTO, item.getIdGasto());
        valores.put(TIPO_GASTO, item.getTipo_gasto());
        valores.put(VALOR_GASTO, item.getValor_gasto());
        valores.put(DIA, item.getDia());
        valores.put(MES, item.getMes());
        valores.put(ANO, item.getAno());

        banco.update(TABELA_GASTOS, valores, ID_GASTO + " = " + item.getIdGasto(), null);
    }

    public void delete(BeanGastos item) {
        int id = item.getIdGasto();
        banco.delete(TABELA_GASTOS, ID_GASTO + " = " + id, null);
    }

    public BeanGastos selectUm(BeanGastos item) {
        Cursor cursor = banco.query(
                TABELA_GASTOS,
                todasAsColunas,
                ID_GASTO + " = " + item.getIdGasto(),
                null, null, null, null);
        cursor.moveToFirst();
        return cursorToItem(cursor);
    }

    public List<BeanGastos> selectTipos() {
        List<BeanGastos> itens = new ArrayList<>();
        Cursor cursor = banco.rawQuery("SELECT SUM(valor_gasto) AS VALOR, tipo_gasto FROM gastos GROUP BY tipo_gasto ORDER BY VALOR DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanGastos item = cursorToItemTipo(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    public List<BeanGastos> selectRecente() {
        List<BeanGastos> itens = new ArrayList<>();
        Cursor cursor = banco.query(TABELA_GASTOS,
                todasAsColunas, null, null, null, null, ANO + " DESC, " + MES + " DESC, " + DIA + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanGastos item = cursorToItem(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    public List<BeanGastos> selectAntigo() {
        List<BeanGastos> itens = new ArrayList<>();
        Cursor cursor = banco.query(TABELA_GASTOS,
                todasAsColunas, null, null, null, null, ANO + ", " + MES + ", " + DIA);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanGastos item = cursorToItem(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    public List<BeanGastos> selectASC() {
        List<BeanGastos> itens = new ArrayList<>();
        Cursor cursor = banco.query(TABELA_GASTOS,
                todasAsColunas, null, null, null, null, VALOR_GASTO);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanGastos item = cursorToItem(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    public List<BeanGastos> selectDESC() {
        List<BeanGastos> itens = new ArrayList<>();
        Cursor cursor = banco.query(TABELA_GASTOS,
                todasAsColunas, null, null, null, null, VALOR_GASTO + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanGastos item = cursorToItem(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    private BeanGastos cursorToItem(Cursor cursor) {
        BeanGastos item = new BeanGastos();

        item.setIdGasto(cursor.getInt(0));
        item.setTipo_gasto(cursor.getString(1));
        item.setValor_gasto(cursor.getFloat(2));
        item.setDia(cursor.getInt(3));
        item.setMes(cursor.getInt(4));
        item.setAno(cursor.getInt(5));

        return item;
    }

    private BeanGastos cursorToItemTipo(Cursor cursor){
        BeanGastos item = new BeanGastos();

        item.setValor_gasto(cursor.getFloat(0));
        item.setTipo_gasto(cursor.getString(1));

        return item;
    }

    public void deletaTodos() {
        banco.delete(TABELA_GASTOS, null, null);
    }
}
