package com.example.satupilah.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satupilah.data.model.WasteCategoryUi
import com.example.satupilah.data.model.toUiModel
import com.example.satupilah.data.repository.SatupilahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: SatupilahRepository = SatupilahRepository()
) : ViewModel() {

    private val _categories = MutableStateFlow<List<WasteCategoryUi>>(emptyList())
    val categories: StateFlow<List<WasteCategoryUi>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getCategories()
                if (response.isSuccessful && response.body()?.status == true) {
                    val apiData = response.body()?.data ?: emptyList()
                    _categories.value = apiData.map { it.toUiModel() }
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memuat kategori sampah"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createCategory(name: String, price: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.createCategory(name, price)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Kategori berhasil ditambahkan"
                    loadCategories()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menambah kategori ke server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCategory(categoryId: String, name: String, price: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updateCategory(categoryId, name, price)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Kategori berhasil diperbarui"
                    loadCategories()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal memperbarui kategori di server"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCategory(categoryId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.deleteCategory(categoryId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _successMessage.value = response.body()?.message ?: "Kategori berhasil dihapus"
                    loadCategories()
                    onSuccess()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal menghapus kategori dari server"
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
