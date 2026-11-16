package br.edu.fasipe.oficina.data;

import static br.edu.fasipe.oficina.data.DatabaseHelper.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.fasipe.oficina.model.ServiceOrder;

/**
 * CRUD da tabela service_orders. Única classe que usa o DatabaseHelper.
 * Erro de SQLite nunca fecha o app: é registrado no Log (sem dados do cliente)
 * e o método devolve o valor de falha do contrato em PRODUCT.md.
 */
public class ServiceOrderDao {

    private static final String TAG = "ServiceOrderDao";
    private static final String WHERE_ID = COL_ID + " = ?";

    private final DatabaseHelper helper;

    public ServiceOrderDao(Context context) {
        helper = new DatabaseHelper(context);
    }

    /** Devolve o id novo, ou -1 em erro. */
    public long insert(ServiceOrder order) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.insertOrThrow(TABLE_SERVICE_ORDERS, null, toValues(order));
            } finally {
                db.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Erro ao inserir ordem", e);
            return -1;
        }
    }

    /** Mais recente primeiro; mesma data, a cadastrada por último primeiro. Lista vazia em erro. */
    public List<ServiceOrder> findAll() {
        List<ServiceOrder> orders = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try (Cursor cursor = db.query(TABLE_SERVICE_ORDERS, null, null, null, null, null,
                    COL_ENTRY_DATE + " DESC, " + COL_ID + " DESC")) {
                while (cursor.moveToNext()) {
                    orders.add(fromCursor(cursor));
                }
            } finally {
                db.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Erro ao listar ordens", e);
            return new ArrayList<>();
        }
        return orders;
    }

    /** null se não existir ou em erro. */
    public ServiceOrder findById(long id) {
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try (Cursor cursor = db.query(TABLE_SERVICE_ORDERS, null, WHERE_ID,
                    new String[]{String.valueOf(id)}, null, null, null)) {
                return cursor.moveToFirst() ? fromCursor(cursor) : null;
            } finally {
                db.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Erro ao buscar ordem", e);
            return null;
        }
    }

    /** Linhas alteradas: 1 ok, 0 = não encontrada ou erro. */
    public int update(ServiceOrder order) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.update(TABLE_SERVICE_ORDERS, toValues(order), WHERE_ID,
                        new String[]{String.valueOf(order.getId())});
            } finally {
                db.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Erro ao atualizar ordem", e);
            return 0;
        }
    }

    /** Linhas removidas: 1 ok, 0 = não encontrada ou erro. */
    public int delete(long id) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(TABLE_SERVICE_ORDERS, WHERE_ID, new String[]{String.valueOf(id)});
            } finally {
                db.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Erro ao excluir ordem", e);
            return 0;
        }
    }

    // Opcional null vira NULL no banco (ContentValues aceita null)
    private ContentValues toValues(ServiceOrder order) {
        ContentValues values = new ContentValues();
        values.put(COL_PLATE, order.getPlate());
        values.put(COL_VEHICLE_MODEL, order.getVehicleModel());
        values.put(COL_VEHICLE_YEAR, order.getVehicleYear());
        values.put(COL_MILEAGE, order.getMileage());
        values.put(COL_CUSTOMER_NAME, order.getCustomerName());
        values.put(COL_CUSTOMER_PHONE, order.getCustomerPhone());
        values.put(COL_SERVICE_TYPE, order.getServiceType());
        values.put(COL_DESCRIPTION, order.getDescription());
        values.put(COL_PRICE, order.getPrice());
        values.put(COL_ENTRY_DATE, order.getEntryDate());
        values.put(COL_STATUS, order.getStatus());
        return values;
    }

    private ServiceOrder fromCursor(Cursor cursor) {
        ServiceOrder order = new ServiceOrder();
        order.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
        order.setPlate(getString(cursor, COL_PLATE));
        order.setVehicleModel(getString(cursor, COL_VEHICLE_MODEL));
        order.setVehicleYear(getInteger(cursor, COL_VEHICLE_YEAR));
        order.setMileage(getInteger(cursor, COL_MILEAGE));
        order.setCustomerName(getString(cursor, COL_CUSTOMER_NAME));
        order.setCustomerPhone(getString(cursor, COL_CUSTOMER_PHONE));
        order.setServiceType(getString(cursor, COL_SERVICE_TYPE));
        order.setDescription(getString(cursor, COL_DESCRIPTION));
        int priceIndex = cursor.getColumnIndexOrThrow(COL_PRICE);
        order.setPrice(cursor.isNull(priceIndex) ? null : cursor.getDouble(priceIndex));
        order.setEntryDate(getString(cursor, COL_ENTRY_DATE));
        order.setStatus(getString(cursor, COL_STATUS));
        return order;
    }

    private String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndexOrThrow(column));
    }

    private Integer getInteger(Cursor cursor, String column) {
        int index = cursor.getColumnIndexOrThrow(column);
        return cursor.isNull(index) ? null : cursor.getInt(index);
    }
}
