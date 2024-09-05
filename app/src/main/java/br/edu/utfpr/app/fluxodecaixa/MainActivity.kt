package br.edu.utfpr.app.fluxodecaixa

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.app.fluxodecaixa.Dao.CaixaDao
import br.edu.utfpr.app.fluxodecaixa.Entity.Caixa
import br.edu.utfpr.app.fluxodecaixa.Model.Lista_Lancamento
import com.devs.omni.app.fluxodecaixa.R
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var tipo : String
    private lateinit var detalhes : String
    private var valor : Double = 0.0
    private lateinit var datalancamento : String
    public lateinit var spTipo : Spinner
    private lateinit var spDetalhes : Spinner
    private lateinit var caixa : Caixa
    private lateinit var caixaDao : CaixaDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        caixaDao = CaixaDao(this)
        spTipo = findViewById(R.id.spTipo)
        spTipo.adapter = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item)
        spDetalhes = findViewById(R.id.spDetalhes)
        spTipo.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                when (position) {
                    1 -> spDetalhes.adapter = ArrayAdapter.createFromResource(this@MainActivity,
                        R.array.credito, android.R.layout.simple_spinner_item)
                    2 -> spDetalhes.adapter = ArrayAdapter.createFromResource(this@MainActivity,
                        R.array.debito, android.R.layout.simple_spinner_item)
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
            }
        }
        datalancamento = findViewById<EditText>(R.id.etDataLancamento).text.toString()


    }
    fun btVerLancamentosOnClick(view: android.view.View) {
        val intent = android.content.Intent(this, Lista_Lancamento::class.java)
        startActivity(intent)
    }
    fun btAdicionarOnClick(view: android.view.View) {
        if(spTipo.selectedItem.toString() == "Selecione" || spDetalhes.selectedItem.toString() == "Selecione"){
            Toast.makeText(this, "Selecione um tipo e um detalhe", Toast.LENGTH_SHORT).show()
            return
        }else if(valor.toString() == "" && datalancamento == ""){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }else{
            fecharTeclado()
        }
        var saldo : Double = 0.0
        tipo = spTipo.selectedItem.toString()
        valor = findViewById<EditText>(R.id.etValor).text.toString().toDouble()
        spTipo.selectedItemPosition.let {
            if (it == 1) {
                saldo += valor
            } else if (it == 2) {
                saldo -= valor
            }
        }
        detalhes = spDetalhes.selectedItem.toString()
        datalancamento = findViewById<EditText>(R.id.etDataLancamento).text.toString()
        caixa = Caixa(0,tipo, detalhes, valor, datalancamento, saldo)
        caixaDao.insert(caixa)

        Toast.makeText(this, "Lançamento adicionado com sucesso", Toast.LENGTH_SHORT).show()
        btVerLancamentosOnClick(view)
    }

    fun btSaldoOnClick(view: View) {
        val saldo = caixaDao.emissaoSaldo()
        Toast.makeText(this, "Saldo: ${saldo}", Toast.LENGTH_SHORT).show()
    }
    fun btCalendarioOnClick(view: View)  {
        val calendario = Calendar.getInstance()
        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)
        var data : String

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            data = "${dayOfMonth}/${month }/${year}"
            findViewById<EditText>(R.id.etDataLancamento).setText(data)
        }, ano, mes, dia)
        datePickerDialog.show()

    }
    fun fecharTeclado(){
        val view = this.currentFocus
        val inputMethodManager : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}