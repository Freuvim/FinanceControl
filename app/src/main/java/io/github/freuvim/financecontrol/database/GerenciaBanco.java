package io.github.freuvim.financecontrol.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GerenciaBanco extends SQLiteOpenHelper {
    // dados do banco
    private static final String NOME_BANCO = "db_gastos";
    private static final int VERSAO_BANCO = 1;

    // tabelas
    private static final String TB_GASTOS =
            "CREATE TABLE gastos ( " +
                    "id_gasto  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo_gasto  TEXT, " +
                    "valor_gasto REAL, " +
                    "dia INTEGER, " +
                    "mes INTEGER, " +
                    "ano INTEGER" +
            ");";

    public GerenciaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // criação do banco
        db.execSQL(TB_GASTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Setup inicial do banco. Ignorando upgrade
    }

}
