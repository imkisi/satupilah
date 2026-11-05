package com.example.satupilah.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satupilah.data.model.*
import com.example.satupilah.data.repository.SatupilahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: SatupilahRepository = SatupilahRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserUiModel>>(emptyList())
    val users: StateFlow<List<UserUiModel>> = _users.asStateFlow()

    private val _categories = MutableStateFlow<List<WasteCategoryUi>>(emptyList())
    val categories: StateFlow<List<WasteCategoryUi>> = _categories.asStateFlow()

    private val _staffList = MutableStateFlow<List<AdminUi>>(emptyList())
    val staffList: StateFlow<List<AdminUi>> = _staffList.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionItemUi>>(emptyList())
    val transactions: StateFlow<List<TransactionItemUi>> = _transactions.asStateFlow()

    private val _totalWeightKg = MutableStateFlow(0.0)
    val totalWeightKg: StateFlow<Double> = _totalWeightKg.asStateFlow()

    private val _totalValueRp = MutableStateFlow(0.0)
    val totalValueRp: StateFlow<Double> = _totalValueRp.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch Users
                val usersRes = repository.getUsers()
                if (usersRes.isSuccessful && usersRes.body()?.status == true) {
                    val data = usersRes.body()?.data ?: emptyList()
                    _users.value = data.map { it.toUiModel() }
                }

                // Fetch Categories
                val catRes = repository.getCategories()
                if (catRes.isSuccessful && catRes.body()?.status == true) {
                    val data = catRes.body()?.data ?: emptyList()
                    _categories.value = data.map { it.toUiModel() }
                }

                // Fetch Staff
                val staffRes = repository.getStaff()
                if (staffRes.isSuccessful && staffRes.body()?.status == true) {
                    val data = staffRes.body()?.data ?: emptyList()
                    _staffList.value = data.map { it.toUiModel() }
                }

                // Fetch Deposits & Withdrawals
                val depositsRes = repository.getDeposits()
                val withdrawalsRes = repository.getWithdrawals()

                val rawDeposits = depositsRes.body()?.data ?: emptyList()
                val rawWithdrawals = withdrawalsRes.body()?.data ?: emptyList()

                val depositList = rawDeposits.map { it.toUiModel() }
                val withdrawalList = rawWithdrawals.map { it.toUiModel() }

                val combined = (depositList + withdrawalList).sortedByDescending { it.id }
                _transactions.value = combined

                // Calculate total weight and total value from database deposits
                var sumWeight = 0.0
                var sumValue = 0.0
                for (dep in rawDeposits) {
                    val w = (dep.weight ?: dep.weightKg ?: "0").toDoubleOrNull() ?: 0.0
                    val p = (dep.totalEarned ?: dep.totalPrice ?: "0").toDoubleOrNull() ?: 0.0
                    sumWeight += w
                    sumValue += p
                }
                _totalWeightKg.value = sumWeight
                _totalValueRp.value = sumValue

            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat data dari server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createDeposit(
        userId: String,
        categoryId: String,
        staffId: String,
        weightKg: Double,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = repository.createDeposit(userId, categoryId, staffId, weightKg)
                if (res.isSuccessful && res.body()?.status == true) {
                    _successMessage.value = res.body()?.message ?: "Transaksi setor berhasil"
                    loadDashboardData()
                    onSuccess()
                } else {
                    _errorMessage.value = res.body()?.message ?: "Gagal memproses transaksi di server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createWithdrawal(
        userId: String,
        staffId: String,
        amount: Double,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val res = repository.createWithdrawal(userId, staffId, amount)
                if (res.isSuccessful && res.body()?.status == true) {
                    _successMessage.value = res.body()?.message ?: "Pencairan saldo berhasil"
                    loadDashboardData()
                    onSuccess()
                } else {
                    _errorMessage.value = res.body()?.message ?: "Gagal memproses pencairan di server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
