package com.example.satupilah.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satupilah.data.model.*
import com.example.satupilah.data.repository.SatupilahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: SatupilahRepository = SatupilahRepository()
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionItemUi>>(emptyList())
    val transactions: StateFlow<List<TransactionItemUi>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val sampleTransactions = listOf(
        TransactionItemUi(
            id = "1",
            type = TransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75.000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g", "Botol Kaca · 328g")
        ),
        TransactionItemUi(
            id = "2",
            type = TransactionType.DEPOSIT,
            date = "23 September",
            time = "08.46",
            amount = "Rp75.000",
            userName = "Jane Doe Abdulsalam",
            itemsDetail = listOf("Karton · 728g", "Botol Plastik · 210g", "Botol Kaca · 328g")
        ),
        TransactionItemUi(
            id = "3",
            type = TransactionType.WITHDRAWAL,
            date = "23 September",
            time = "08.46",
            amount = "Rp120.000",
            userName = "Jane Doe Abdulsalam"
        )
    )

    init {
        _transactions.value = sampleTransactions
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val depositsRes = repository.getDeposits()
                val withdrawalsRes = repository.getWithdrawals()

                val depositList = if (depositsRes.isSuccessful && depositsRes.body()?.status == true) {
                    depositsRes.body()?.data?.map { it.toUiModel() } ?: emptyList()
                } else emptyList()

                val withdrawalList = if (withdrawalsRes.isSuccessful && withdrawalsRes.body()?.status == true) {
                    withdrawalsRes.body()?.data?.map { it.toUiModel() } ?: emptyList()
                } else emptyList()

                val combined = (depositList + withdrawalList).sortedByDescending { it.id }
                if (combined.isNotEmpty()) {
                    _transactions.value = combined
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat riwayat: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
