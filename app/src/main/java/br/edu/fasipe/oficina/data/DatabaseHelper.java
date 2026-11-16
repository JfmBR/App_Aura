package br.edu.fasipe.oficina.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Cria e atualiza o banco oficina.db. Usado só pelo ServiceOrderDao.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "oficina.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SERVICE_ORDERS = "service_orders";
    public static final String COL_ID = "id";
    public static final String COL_PLATE = "plate";
    public static final String COL_VEHICLE_MODEL = "vehicle_model";
    public static final String COL_VEHICLE_YEAR = "vehicle_year";
    public static final String COL_MILEAGE = "mileage";
    public static final String COL_CUSTOMER_NAME = "customer_name";
    public static final String COL_CUSTOMER_PHONE = "customer_phone";
    public static final String COL_SERVICE_TYPE = "service_type";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_ENTRY_DATE = "entry_date";
    public static final String COL_STATUS = "status";

    // NOT NULL só nos 7 obrigatórios; as demais regras ficam no validador (sem CHECK)
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_SERVICE_ORDERS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PLATE + " TEXT NOT NULL, "
            + COL_VEHICLE_MODEL + " TEXT NOT NULL, "
            + COL_VEHICLE_YEAR + " INTEGER, "
            + COL_MILEAGE + " INTEGER, "
            + COL_CUSTOMER_NAME + " TEXT NOT NULL, "
            + COL_CUSTOMER_PHONE + " TEXT NOT NULL, "
            + COL_SERVICE_TYPE + " TEXT NOT NULL, "
            + COL_DESCRIPTION + " TEXT, "
            + COL_PRICE + " REAL, "
            + COL_ENTRY_DATE + " TEXT NOT NULL, "
            + COL_STATUS + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Decisão do projeto: subir a versão apaga as OS do aparelho (ver docs/architecture.md)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_ORDERS);
        onCreate(db);
    }
}
