package cl.inefable.jazb.inefable.Controlador.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Controlador.C_DetalleReservaConductor;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class F_ReservasPendientes_Pendientes extends Fragment {
    private static final String USUARIO_TAG = "Usuario";

    private O_Usuario Usuario;

    public void setUsuario(O_Usuario usuario) {
        Usuario = usuario;
    }

    private RecyclerView Lista;

    private OnFragmentInteractionListener callback;

    public F_ReservasPendientes_Pendientes() {
        // Required empty public constructor
    }

    public static F_ReservasPendientes_Pendientes NuevaInstancia(O_Usuario usuario) {
        F_ReservasPendientes_Pendientes fragment = new F_ReservasPendientes_Pendientes();
        Bundle args = new Bundle();
        args.putSerializable(USUARIO_TAG, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            Usuario = (O_Usuario) getArguments().getSerializable(USUARIO_TAG);
        }
    }

    private void CargarListaReservas() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        Lista.setLayoutManager(llm);

        Lista_Reservados lista = new Lista_Reservados(F_Usuario.TaaerReservasPendientesConductor(Usuario));
        if (lista.getItemCount() == 0) {
            O_Alerta alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Mis Reservas",
                    "No tienes reservas.",
                    false,
                    3500,
                    O_Alerta.RES_ICO_INFO
            );
            MostrarAlerta(alerta);
        }
        Lista.setAdapter(lista);
    }


    private void MostrarAlerta(O_Alerta alerta) {
        Alerter.create(getActivity())
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            callback = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_reservaspendientes, container, false);
        Lista = view.findViewById(R.id.rv_frg_reservaspendientes_listareservas);
        CargarListaReservas();
        return view;
    }

    public interface OnFragmentInteractionListener {
        void Navegar(Intent destino);

        void NavegarResult(Bundle datos, Class<?> destino, int codigo);
    }

    class Lista_Reservados extends RecyclerView.Adapter<Lista_Reservados.VehicleViewHolder> {
        private ArrayList<O_Reserva> reservas;

        public Lista_Reservados(ArrayList<O_Reserva> reserva) {
            this.reservas = reserva;
        }

        @NonNull
        @Override
        public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_reservaspendienteconductor, parent, false);
            VehicleViewHolder pvh = new VehicleViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position) {
            holder.asd.setTag(reservas.get(position));
            holder.asd.setOnClickListener(rv_ItemClick());
            holder.Estado.setText("Estado Reserva: " + reservas.get(position).getEstado().getNombre());
            String solicitante = reservas.get(position).getSolicitante().getNombres() + " " + reservas.get(position).getSolicitante().getApellidos();
            holder.Solicitante.setText("Solicitante del vehículo: " + solicitante);
            holder.Distancia.setText("Distancia: " + ((float) reservas.get(position).getDistancia() / 1000) + " Km");
        }

        @Override
        public int getItemCount() {
            return reservas.size();
        }

        public class VehicleViewHolder extends RecyclerView.ViewHolder {
            View asd;
            TextView Estado, Solicitante, Distancia;

            VehicleViewHolder(View itemView) {
                super(itemView);
                asd = itemView;

                Estado = itemView.findViewById(R.id.item_tv_reservaspendientes_estado);
                Solicitante = itemView.findViewById(R.id.item_tv_reservaspendientes_solicitante);
                Distancia = itemView.findViewById(R.id.item_tv_reservaspendientes_distancia);
            }
        }
    }

    private View.OnClickListener rv_ItemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                O_Reserva reserva = (O_Reserva) view.getTag();

                Bundle datos = new Bundle();
                datos.putSerializable("RESERVA", reserva);
                callback.NavegarResult(datos, C_DetalleReservaConductor.class, C_DetalleReservaConductor.ActCode);
            }
        };
    }
}
