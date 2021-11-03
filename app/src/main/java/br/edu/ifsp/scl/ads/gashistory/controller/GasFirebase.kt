package br.edu.ifsp.scl.ads.gashistory.controller

import br.edu.ifsp.scl.ads.gashistory.model.Gas
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class GasFirebase : GasDAO {
    companion object {
        private val DB_GAS = "gas-history"
    }

    // Referência para o RtDb -> livros
    private val gasRtDb = Firebase.database.getReference(DB_GAS)

    // Lista de livros que simula uma consulta
    private val gasList: MutableList<Gas> = mutableListOf()

    init {
        gasRtDb.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newGas: Gas? = snapshot.value as? Gas
                newGas?.apply {
                    if (gasList.find { it.date == this.date } == null) {
                        gasList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val gasEdited: Gas? = snapshot.value as? Gas
                gasEdited?.apply {
                    gasList[gasList.indexOfFirst { it.date == this.date }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val gasRemoved: Gas? = snapshot.value as? Gas
                gasRemoved?.apply {
                    gasList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
        gasRtDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gasList.clear()
                snapshot.children.forEach {
                    val gas: Gas = it.getValue<Gas>() ?: Gas()
                    gasList.add(gas)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // não se aplica
            }
        })
    }

    override fun createGas(gas: Gas): Long {
        createOrUpdateGas(gas)
        return 0L
    }

    override fun getGasList(): MutableList<Gas> = gasList

    override fun updateGas(gas: Gas): Int {
        createOrUpdateGas(gas)
        return 1
    }

    override fun deleteGas(date: Date): Int {
        gasRtDb.child(date.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateGas(gas: Gas) {
        gasRtDb.child(gas.date.toString()).setValue(gas)
    }
}