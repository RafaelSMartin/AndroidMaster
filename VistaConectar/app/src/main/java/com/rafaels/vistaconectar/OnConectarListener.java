package com.rafaels.vistaconectar;

/**
 * Created by Rafael S Martin on 08/02/2018.
 */

public interface OnConectarListener {

    void onConectar(String ip, int puerto);
    void onConectado(String ip, int puerto);
    void onDesconectado();
    void onError(String mensaje);

}
