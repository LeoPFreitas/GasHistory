package br.edu.ifsp.scl.ads.gashistory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.ads.gashistory.adapter.GasRvAdapter
import br.edu.ifsp.scl.ads.gashistory.controller.GasController
import br.edu.ifsp.scl.ads.gashistory.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.gashistory.model.Gas
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnGasClickListener {
    companion object Extras {
        const val EXTRA_GAS = "EXTRA_GAS"
        const val EXTRA_POSITION_ELEMENT = "EXTRA_GAS_POSITION"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var gasActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editGasActivityResultLauncher: ActivityResultLauncher<Intent>
    private val gasController: GasController by lazy { GasController() }
    private val gasList: MutableList<Gas> by lazy { gasController.getGasList() }
    private val gasAdapter: GasRvAdapter by lazy { GasRvAdapter(this, gasList) }
    private val gasLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.GasRv.adapter = gasAdapter
        activityMainBinding.GasRv.layoutManager = gasLayoutManager

        gasActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado.resultCode == RESULT_OK) {
                    resultado.data?.getParcelableExtra<Gas>(EXTRA_GAS)?.apply {
                        gasController.createGas(this)
                        gasList.add(this)
                        gasAdapter.notifyDataSetChanged()
                    }
                }
            }

        editGasActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado.resultCode == RESULT_OK) {
                    val position = resultado.data?.getIntExtra(EXTRA_POSITION_ELEMENT, -1)
                    resultado.data?.getParcelableExtra<Gas>(EXTRA_GAS)?.apply {
                        if (position != null && position != -1) {
                            gasController.updateGas(this)
                            gasList[position] = this
                            gasAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

        activityMainBinding.addGasFb.setOnClickListener {
            gasActivityResultLauncher.launch(Intent(this, GasActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val elementPosition = gasAdapter.elementPosition
        val gas = gasList[elementPosition]

        return when (item.itemId) {
            R.id.editGasMi -> {
                val editGasIntent = Intent(this, GasActivity::class.java)
                editGasIntent.putExtra(EXTRA_GAS, gas)
                editGasIntent.putExtra(EXTRA_POSITION_ELEMENT, elementPosition)
                editGasActivityResultLauncher.launch(editGasIntent)
                true
            }
            R.id.removeGasMi -> {
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirm Delete?")
                    setPositiveButton("Sim") { _, _ ->
                        gasController.deleteGas(gas.date)
                        gasList.removeAt(elementPosition)
                        gasAdapter.notifyDataSetChanged()
                        Snackbar.make(
                            activityMainBinding.root,
                            "Gas has removed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(
                            activityMainBinding.root,
                            "Canceling remove",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    create()
                }.show()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.updateMi -> {
            gasAdapter.notifyDataSetChanged()
            true
        }
        else -> {
            false
        }
    }

    override fun onGasClick(position: Int) {
        val gas = gasList[position]
        val viewGasIntent = Intent(this, GasActivity::class.java)
        viewGasIntent.putExtra(EXTRA_GAS, gas)
        startActivity(viewGasIntent)
    }
}