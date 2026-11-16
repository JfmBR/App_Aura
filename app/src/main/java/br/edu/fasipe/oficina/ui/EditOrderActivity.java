package br.edu.fasipe.oficina.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import br.edu.fasipe.oficina.R;
import br.edu.fasipe.oficina.data.ServiceOrderDao;
import br.edu.fasipe.oficina.model.ServiceOrder;

/**
 * Edição e exclusão de uma OS (fluxos 3 e 4 do PRODUCT.md).
 */
public class EditOrderActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    private ServiceOrderForm form;
    private ServiceOrderDao dao;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_edit_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Inclui o teclado no espaço de baixo para os botões continuarem alcançáveis
            Insets barsAndKeyboard = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, barsAndKeyboard.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        // Voltar sai sem gravar (critério de aceite 9)
        toolbar.setNavigationOnClickListener(v -> finish());

        dao = new ServiceOrderDao(this);
        orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, 0);
        ServiceOrder order = dao.findById(orderId);
        if (order == null) {
            Toast.makeText(this, R.string.toast_order_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        form = new ServiceOrderForm(this);
        // Depois de girar a tela, os campos já guardam o que foi digitado
        if (savedInstanceState == null) {
            form.fill(order);
        }

        findViewById(R.id.buttonSave).setOnClickListener(v -> save());
        findViewById(R.id.buttonDelete).setOnClickListener(v -> confirmDelete());
    }

    private void save() {
        ServiceOrder order = form.readOrder();
        order.setId(orderId);
        if (!form.validateAndShowErrors(order)) {
            return;
        }
        if (dao.update(order) == 0) {
            Toast.makeText(this, R.string.toast_update_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.toast_order_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        // "Não" só fecha o diálogo, sem mudar nada
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_delete_title)
                .setNegativeButton(R.string.dialog_no, null)
                .setPositiveButton(R.string.dialog_yes, (d, which) -> delete())
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.color_error));
    }

    private void delete() {
        if (dao.delete(orderId) == 0) {
            Toast.makeText(this, R.string.toast_delete_error, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.toast_order_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }
}
