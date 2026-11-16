package br.edu.fasipe.oficina.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import br.edu.fasipe.oficina.R;
import br.edu.fasipe.oficina.data.ServiceOrderDao;
import br.edu.fasipe.oficina.model.ServiceOrder;

/**
 * Cadastro de uma nova OS (fluxo 2 do PRODUCT.md).
 */
public class CreateOrderActivity extends AppCompatActivity {

    private ServiceOrderForm form;
    private ServiceOrderDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_create_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Inclui o teclado no espaço de baixo para o botão Salvar continuar alcançável
            Insets barsAndKeyboard = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, barsAndKeyboard.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        // Voltar sai sem gravar (critério de aceite 9)
        toolbar.setNavigationOnClickListener(v -> finish());

        form = new ServiceOrderForm(this);
        dao = new ServiceOrderDao(this);
        if (savedInstanceState == null) {
            form.setEntryDate(ServiceOrderForm.today());
        }

        findViewById(R.id.buttonSave).setOnClickListener(v -> save());
    }

    private void save() {
        ServiceOrder order = form.readOrder();
        if (!form.validateAndShowErrors(order)) {
            return;
        }
        if (dao.insert(order) == -1) {
            Toast.makeText(this, R.string.toast_save_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.toast_order_saved, Toast.LENGTH_SHORT).show();
        finish();
    }
}
