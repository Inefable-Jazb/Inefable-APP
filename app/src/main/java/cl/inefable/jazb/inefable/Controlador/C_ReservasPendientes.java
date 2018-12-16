package cl.inefable.jazb.inefable.Controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import cl.inefable.jazb.inefable.Controlador.Fragmentos.F_ReservasPendientes_Pendientes;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class C_ReservasPendientes extends AppCompatActivity implements F_ReservasPendientes_Pendientes.OnFragmentInteractionListener {
    public static final int ActCode = 7586;
    private O_Usuario Usuario;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reservas_pendientes);

        InicilizarComponentes();
        ConfigurarComponentes();
        CargarLista();
    }

    private void CargarLista() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void ConfigurarComponentes() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void InicilizarComponentes() {
        Usuario = (O_Usuario) getIntent().getSerializableExtra("USUARIO");

        toolbar = (Toolbar) findViewById(R.id.tb_reservaspendientes_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tl_reservaspendientes_tabs);
        CargarLista();
    }

    @Override
    public void Navegar(Intent destino) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case C_DetalleReservaConductor.ActCode:
                O_Reserva aux = (O_Reserva) data.getSerializableExtra("RESERVA");
                Usuario = aux.getVehiculo().getDueño();
                O_Alerta alerta = null;
                switch (resultCode) {
                    case RESULT_OK:
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_CORRECTO,
                                "Gestionar Reservas",
                                "Reserva aceptada correctamente.",
                                false,
                                1500,
                                O_Alerta.RES_ICO_CORRECTO
                        );
                        break;
                    case C_DetalleReservaConductor.ENCURSO:
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_CORRECTO,
                                "Gestionar Reservas",
                                "La ruta ha comenazado exitosamente.",
                                false,
                                1500,
                                O_Alerta.RES_ICO_CORRECTO
                        );
                        break;
                    case C_DetalleReservaConductor.FINALIZADA:
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_CORRECTO,
                                "Gestionar Reservas",
                                "Reserva dada por finalizada con éxito.",
                                false,
                                1500,
                                O_Alerta.RES_ICO_INFO
                        );
                        break;
                    case C_DetalleReservaConductor.RECHAZADA:
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_CORRECTO,
                                "Gestionar Reservas",
                                "Reserva rechzada.",
                                false,
                                1500,
                                O_Alerta.RES_ICO_PRECAUCION
                        );
                        break;
                }
                if (alerta != null) {
                    MostrarAlerta(alerta);
                }
                CargarLista();
                break;
        }
    }

    private void MostrarAlerta(O_Alerta alerta) {
        Alerter.create(this)
                .setTitle(alerta.getTitulo())
                .setText(alerta.getMensaje())
                .setBackgroundColorRes(alerta.getTipo())
                .enableInfiniteDuration(alerta.isInfinito())
                .enableIconPulse(true)
                .setIcon(alerta.getIcono())
                .setDuration(alerta.getDuracion())
                .enableSwipeToDismiss()
                .show();
    }

    @Override
    public void NavegarResult(Bundle datos, Class<?> destino, int codigo) {
        Intent intent = new Intent(this, destino);
        intent.putExtras(datos);
        startActivityForResult(intent, codigo);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> Vistas = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            F_ReservasPendientes_Pendientes a = F_ReservasPendientes_Pendientes.NuevaInstancia(Usuario);
            Vistas.add(a);
        }

        @Override
        public Fragment getItem(int position) {
            return Vistas.get(position);
        }

        @Override
        public int getCount() {
            return Vistas.size();
        }
    }


}
