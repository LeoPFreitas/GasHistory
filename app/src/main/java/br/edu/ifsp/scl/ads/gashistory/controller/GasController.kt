package br.edu.ifsp.scl.ads.gashistory.controller

import br.edu.ifsp.scl.ads.gashistory.model.Gas
import java.util.*

class GasController {
    private val gasDAO: GasDAO = GasFirebase()

    fun createGas(gas: Gas) = gasDAO.createGas(gas)
    fun getGasList() = gasDAO.getGasList()
    fun updateGas(gas: Gas) = gasDAO.updateGas(gas)
    fun deleteGas(date: Date) = gasDAO.deleteGas(date)
}
