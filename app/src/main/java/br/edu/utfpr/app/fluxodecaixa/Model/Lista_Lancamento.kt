package br.edu.utfpr.app.fluxodecaixa.Model

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.app.fluxodecaixa.CaixaAdapter.CaixaAdapter
import br.edu.utfpr.app.fluxodecaixa.Dao.CaixaDao
import br.edu.utfpr.app.fluxodecaixa.MainActivity
import com.devs.omni.app.fluxodecaixa.R

class Lista_Lancamento : AppCompatActivity() {
    private lateinit var caixaDao: CaixaDao
    private lateinit var lvCaixa: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_lancamento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lvCaixa = findViewById(R.id.listaLancamento)
        caixaDao = CaixaDao(this)
    }
    override fun onStart() {
        super.onStart()
        val registros = caixaDao.listaCursor()
        val adapter = CaixaAdapter( this, registros)
        lvCaixa.adapter = adapter
    }
    fun btAdicionarOnClick(view: android.view.View) {
        val intent = android.content.Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun btSaldoOnClick(view: View) {
        val saldo = caixaDao.emissaoSaldo()
        val builder = AlertDialog.Builder(this)
        val formatted = String.format("%.2f", saldo) ;
        println(formatted)
        builder.setMessage("Saldo: ${formatted}.")
            .setPositiveButton("ZERAR") { dialog, id ->
                val builderDelete = AlertDialog.Builder(this)
                    builderDelete.setMessage("Você tem certeza que deseja apagar tudo?")
                    .setPositiveButton("SIM") { dialog, id ->
                        caixaDao.onUpgrade(caixaDao.writableDatabase, 1, 1)
                        onStart()
                    }
                    .setNegativeButton("NÃO") { dialog, id ->
                    }
                builderDelete.create()
                builderDelete.show()
            }
            .setNegativeButton("OK") { dialog, id -> }
        builder.create()
        builder.show()
    }
}