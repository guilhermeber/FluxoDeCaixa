package br.edu.utfpr.app.fluxodecaixa.Dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.app.fluxodecaixa.Entity.Caixa

class CaixaDao (context: Context) :SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, detalhes TEXT, valor FLOAT, data_lancamento TEXT, saldo FLOAT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL( "DROP TABLE IF EXISTS $TABLE_NAME" )
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "caixa.SQLite"
        private const val TABLE_NAME = "caixa"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_DETALHES = "detalhes"
        const val COLUMN_VALOR = "valor"
        const val COLUMN_DATA_LANCAMENTO = "data_lancamento"
        const val COLUMN_SALDO = "saldo"
        private const val DB_VERSION = 1
    }

    fun insert(caixa: Caixa) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_TIPO, caixa.tipo.toString())
        values.put(COLUMN_DETALHES, caixa.detalhes.toString())
        values.put(COLUMN_VALOR, caixa.valor.toDouble())
        values.put(COLUMN_DATA_LANCAMENTO, caixa.datalancamento.toString())
        values.put(COLUMN_SALDO, caixa.saldo.toDouble())
        db.insert(TABLE_NAME, null, values)
    }
    //fun update(caixa: Caixa) {
    //    val db = this.writableDatabase
    //    val values = ContentValues()
    //    values.put(COLUMN_TIPO, caixa.tipo)
    //    values.put(COLUMN_DETALHES, caixa.detalhes)
    //    if (caixa.tipo == "Débito"){
    //        caixa.valor = caixa.valor * -1
    //    }
    //    values.put(COLUMN_VALOR, caixa.valor)
    //    values.put(COLUMN_DATA_LANCAMENTO, caixa.datalancamento.toString())
    //    db.update(TABLE_NAME, values, "${COLUMN_ID} = ?", null)
    //}
    //fun delete(caixa: Caixa) {
    //    val db = this.writableDatabase
    //    db.delete(TABLE_NAME, "${COLUMN_ID} = ?", null)
    //}

    fun list() : MutableList<Caixa>{
        val db = this.writableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        var registroCaixa = mutableListOf<Caixa>()
        while (cursor.moveToNext()) {
            val caixa = Caixa(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getString(4),
                cursor.getDouble(5)
            )
            registroCaixa.add(caixa)
        }
        cursor.close()
        return registroCaixa
    }

    fun listaCursor() : Cursor{
        val db = this.writableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }

    fun emissaoSaldo() : Double{
        var fechaSaldo = 0.0
        val db = this.writableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            fechaSaldo += cursor.getDouble(5)
        }
        cursor.close()
        return fechaSaldo
    }
}