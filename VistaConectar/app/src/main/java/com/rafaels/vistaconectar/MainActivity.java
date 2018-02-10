package com.rafaels.vistaconectar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnConectarListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VistaConectar conectar = (VistaConectar)findViewById(R.id.vistaConectar);
        conectar.setOnConectarListener(this);

    }

    @Override
    public void onConectar(String ip, int puerto) {
        Toast.makeText(getApplicationContext(), "Conectando "+ip+":"+puerto, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConectado(String ip, int puerto) {

    }

    @Override
    public void onDesconectado() {

    }

    @Override
    public void onError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}



