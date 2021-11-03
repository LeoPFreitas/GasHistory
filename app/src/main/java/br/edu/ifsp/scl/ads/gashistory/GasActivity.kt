package br.edu.ifsp.scl.ads.gashistory

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.gashistory.MainActivity.Extras.EXTRA_GAS
import br.edu.ifsp.scl.ads.gashistory.MainActivity.Extras.EXTRA_POSITION_ELEMENT
import br.edu.ifsp.scl.ads.gashistory.databinding.ActivityGasBinding
import br.edu.ifsp.scl.ads.gashistory.model.Gas
import java.util.*

class GasActivity : AppCompatActivity() {
    private val activityGasBinding: ActivityGasBinding by lazy {
        ActivityGasBinding.inflate(layoutInflater)
    }
    private var positionElement = -1;
    private lateinit var gas: Gas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityGasBinding.root)
        positionElement = intent.getIntExtra(EXTRA_POSITION_ELEMENT, -1)

        intent.getParcelableExtra<Gas>(EXTRA_GAS)?.run {
            activityGasBinding.valueEt.setText(this.value.toString())
            activityGasBinding.dateDp.updateDate(this.date.year, this.date.month, this.date.day)

            if (positionElement == -1) {
                activityGasBinding.valueEt.isEnabled = false
                activityGasBinding.dateDp.isEnabled = false
                activityGasBinding.saveBt.visibility = View.GONE
            }
        }

        activityGasBinding.saveBt.setOnClickListener {
            gas = Gas(
                activityGasBinding.valueEt.text.toString().toDouble(),
                Date(
                    activityGasBinding.dateDp.year,
                    activityGasBinding.dateDp.month,
                    activityGasBinding.dateDp.dayOfMonth
                )
            )
            val resultIntent = intent.putExtra(EXTRA_GAS, gas)
            if (positionElement != -1) {
                resultIntent.putExtra(EXTRA_POSITION_ELEMENT, positionElement)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
