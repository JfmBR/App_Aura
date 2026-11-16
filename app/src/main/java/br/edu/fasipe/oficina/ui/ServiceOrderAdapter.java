package br.edu.fasipe.oficina.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

import br.edu.fasipe.oficina.R;
import br.edu.fasipe.oficina.model.ServiceOrder;

/**
 * Monta cada card da lista (item_service_order.xml).
 */
public class ServiceOrderAdapter extends ArrayAdapter<ServiceOrder> {

    // Cores do chip na mesma ordem de order_statuses: Aberta, Em andamento, Concluída, Entregue
    private static final int[] STATUS_BACKGROUNDS = {
            R.color.status_open_bg, R.color.status_in_progress_bg,
            R.color.status_done_bg, R.color.status_delivered_bg
    };
    private static final int[] STATUS_TEXTS = {
            R.color.status_open_text, R.color.status_in_progress_text,
            R.color.status_done_text, R.color.status_delivered_text
    };

    // Valores vêm do strings.xml, sem cópia em Java (PRODUCT.md)
    private final List<String> statuses;

    public ServiceOrderAdapter(Context context, List<ServiceOrder> orders) {
        super(context, 0, orders);
        statuses = Arrays.asList(context.getResources().getStringArray(R.array.order_statuses));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_service_order, parent, false);
        }
        ServiceOrder order = getItem(position);

        ((TextView) view.findViewById(R.id.textPlate)).setText(order.getPlate());
        ((TextView) view.findViewById(R.id.textCustomer)).setText(order.getCustomerName());
        ((TextView) view.findViewById(R.id.textDate))
                .setText(ServiceOrderForm.toDisplayDate(order.getEntryDate()));

        TextView textModel = view.findViewById(R.id.textModel);
        if (order.getVehicleYear() == null) {
            textModel.setText(order.getVehicleModel());
        } else {
            textModel.setText(getContext().getString(R.string.item_model_year,
                    order.getVehicleModel(), order.getVehicleYear()));
        }

        // Status desconhecido usa a cor de "Aberta"; o texto sempre aparece
        TextView textStatus = view.findViewById(R.id.textStatus);
        int index = Math.max(statuses.indexOf(order.getStatus()), 0);
        textStatus.setText(order.getStatus());
        textStatus.setBackgroundTintList(
                ContextCompat.getColorStateList(getContext(), STATUS_BACKGROUNDS[index]));
        textStatus.setTextColor(ContextCompat.getColor(getContext(), STATUS_TEXTS[index]));
        return view;
    }
}
