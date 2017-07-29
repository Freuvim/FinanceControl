package io.github.freuvim.financecontrol.database;

public class BeanGastos {
    private int idGasto;
    private String tipo_gasto;
    private float valor_gasto;
    private int dia;
    private int mes;
    private int ano;

    public BeanGastos() {
        idGasto = -1;
        tipo_gasto = "";
        valor_gasto = 0;
        dia = -1;
        mes = -1;
        ano = -1;
    }

    @Override
    public String toString() {
        String sDia = "" + dia;
        String sMes = "" + mes;
        if (sDia.length() < 2) {
            sDia = "0" + sDia;
        }
        if (sMes.length() < 2) {
            sMes = "0" + sMes;
        }
        if (sDia.equals("-1")) {
            return "R$" + valor_gasto + " | " + tipo_gasto;
        }
        return "R$" + valor_gasto + " | " + tipo_gasto + " | " + sDia + "/" + sMes + "/" + ano;
    }

    public int getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
    }

    public String getTipo_gasto() {
        return tipo_gasto;
    }

    public void setTipo_gasto(String tipo_gasto) {
        this.tipo_gasto = tipo_gasto;
    }

    public float getValor_gasto() {
        return valor_gasto;
    }

    public void setValor_gasto(float valor_gasto) {
        this.valor_gasto = valor_gasto;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}
