package com.example.satupilah.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satupilah.data.model.UserUiModel
import com.example.satupilah.data.model.toUiModel
import com.example.satupilah.data.repository.SatupilahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: SatupilahRepository = SatupilahRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserUiModel>>(emptyList())
    val users: StateFlow<List<UserUiModel>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getUsers()
                if (response.isSuccessful && response.body()?.status == true) {
                    val apiData = response.body()?.data ?: emptyList()
                    _users.value = apiData.map { it.toUiModel() }
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memuat data nasabah"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createUser(name: String, phoneNumber: String, address: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.createUser(name, phoneNumber, address)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Nasabah berhasil ditambahkan"
                    loadUsers()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menambah nasabah ke server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(userId: String, name: String, phoneNumber: String, address: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updateUser(userId, name, phoneNumber, address)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Data nasabah berhasil diperbarui"
                    loadUsers()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memperbarui nasabah di server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.deleteUser(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Nasabah berhasil dihapus"
                    loadUsers()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menghapus nasabah dari server"
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
