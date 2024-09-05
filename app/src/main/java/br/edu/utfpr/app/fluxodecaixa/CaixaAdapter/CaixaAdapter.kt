package br.edu.utfpr.app.fluxodecaixa.CaixaAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.edu.utfpr.app.fluxodecaixa.Entity.Caixa
import com.devs.omni.app.fluxodecaixa.R

class CaixaAdapter(val context : Context, val cursor : Cursor) : BaseAdapter() {

    companion object {
        const val ID = 0
        const val TIPO = 1
        const val DETALHES = 2
        const val VALOR = 3
        const val DATA_LANCAMENTO = 4
        const val SALDO = 5
    }
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        return Caixa(
            cursor.getInt( ID ),
            cursor.getString( TIPO ),
            cursor.getString( DETALHES ),
            cursor.getDouble( VALOR ),
            cursor.getString( DATA_LANCAMENTO ),
            cursor.getDouble( SALDO )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams", "MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        val v = inflater.inflate(R.layout.lancamento_item, null)

        val tvTipoLista = v.findViewById<TextView>(R.id.tvTipoLista)
        val tvDetalhesLista = v.findViewById<TextView>(R.id.tvDetalhesLista)
        val tvValorLista = v.findViewById<TextView>(R.id.tvValorLista)
        val tvDataLancamentoLista = v.findViewById<TextView>(R.id.tvDataLista)
        val tvSimbolo = v.findViewById<TextView>(R.id.tvSimbolo)

        cursor.moveToPosition(position)
        tvTipoLista.setText(cursor.getString(TIPO))
        tvValorLista.setText(cursor.getDouble(VALOR).toString())
        if(cursor.getString(TIPO) == "Crédito"){
            tvSimbolo.setText("+")
        }else{
            tvSimbolo.setText("-")
        }
        tvDetalhesLista.setText(cursor.getString(DETALHES))
        tvDataLancamentoLista.setText(cursor.getString(DATA_LANCAMENTO))
        tvValorLista.setText(cursor.getDouble(VALOR).toString())
        return v
    }
}