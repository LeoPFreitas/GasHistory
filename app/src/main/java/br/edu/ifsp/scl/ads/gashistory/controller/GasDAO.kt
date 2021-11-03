package br.edu.ifsp.scl.ads.gashistory.controller

import br.edu.ifsp.scl.ads.gashistory.model.Gas
import java.util.*

interface GasDAO {
    fun createGas(gas: Gas): Long
    fun getGasList(): MutableList<Gas>
    fun updateGas(gas: Gas): Int
    fun deleteGas(date: Date): Int
}
