package com.example.satupilah.ui.screens.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satupilah.data.model.AdminUi
import com.example.satupilah.data.model.toUiModel
import com.example.satupilah.data.repository.SatupilahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffViewModel(
    private val repository: SatupilahRepository = SatupilahRepository()
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<AdminUi>>(emptyList())
    val staffList: StateFlow<List<AdminUi>> = _staffList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadStaff()
    }

    fun loadStaff() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getStaff()
                if (response.isSuccessful && response.body()?.status == true) {
                    val apiData = response.body()?.data ?: emptyList()
                    _staffList.value = apiData.map { it.toUiModel() }
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memuat data petugas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createStaff(username: String, fullName: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.createStaff(username, fullName)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Petugas berhasil ditambahkan"
                    loadStaff()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menambah petugas ke server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateStaff(staffId: String, username: String, fullName: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updateStaff(staffId, username, fullName)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Petugas berhasil diperbarui"
                    loadStaff()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memperbarui data petugas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteStaff(staffId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.deleteStaff(staffId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Petugas berhasil dihapus"
                    loadStaff()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menghapus petugas dari server"
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
