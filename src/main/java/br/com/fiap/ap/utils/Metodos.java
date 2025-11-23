package br.com.fiap.ap.utils;

import java.sql.Date;

public class Metodos {

    public static String formatDataHora(Date data) {

        String dataHora = data.toString().replace('T', ' ');
        return dataHora;
    }
}
