package br.edu.fasipe.oficina.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import br.edu.fasipe.oficina.R;
import br.edu.fasipe.oficina.data.ServiceOrderDao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

/**
 * Lista das OS (fluxo 1) e entrada para cadastro e edição.
 */
public class MainActivity extends AppCompatActivity {

    private ServiceOrderDao dao;
    private ServiceOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dao = new ServiceOrderDao(this);
        adapter = new ServiceOrderAdapter(this, new ArrayList<>());
        ListView listOrders = findViewById(R.id.listOrders);
        listOrders.setAdapter(adapter);
        listOrders.setEmptyView(findViewById(R.id.textEmpty));

        // Toque numa OS abre a edição (fluxo 3)
        listOrders.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, EditOrderActivity.class);
            intent.putExtra(EditOrderActivity.EXTRA_ORDER_ID, adapter.getItem(position).getId());
            startActivity(intent);
        });

        // Botão "+" abre o cadastro (fluxo 2)
        findViewById(R.id.fabNewOrder).setOnClickListener(
                v -> startActivity(new Intent(this, CreateOrderActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega ao voltar do cadastro/edição
        adapter.clear();
        adapter.addAll(dao.findAll());
    }
}
