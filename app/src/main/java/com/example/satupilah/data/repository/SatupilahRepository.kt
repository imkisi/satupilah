package com.example.satupilah.data.repository

import com.example.satupilah.data.remote.RetrofitClient

class SatupilahRepository {

    private val api = RetrofitClient.apiService

    // Users
    suspend fun getUsers() = api.getUsers()
    suspend fun createUser(name: String, phoneNumber: String, address: String) =
        api.createUser(name = name, phoneNumber = phoneNumber, address = address)
    suspend fun updateUser(userId: String, name: String, phoneNumber: String, address: String) =
        api.updateUser(userId = userId, name = name, phoneNumber = phoneNumber, address = address)
    suspend fun deleteUser(userId: String) =
        api.deleteUser(userId = userId)

    // Categories
    suspend fun getCategories() = api.getCategories()
    suspend fun createCategory(name: String, price: Double, unit: String = "kg") =
        api.createCategory(categoryName = name, pricePerKg = price, unit = unit)
    suspend fun updateCategory(id: String, name: String, price: Double, unit: String = "kg") =
        api.updateCategory(categoryId = id, categoryName = name, pricePerKg = price, unit = unit)
    suspend fun deleteCategory(id: String) =
        api.deleteCategory(categoryId = id)

    // Staff / Admin
    suspend fun getStaff() = api.getStaff()
    suspend fun createStaff(username: String, fullName: String) =
        api.createStaff(username = username, fullName = fullName)
    suspend fun updateStaff(id: String, username: String, fullName: String) =
        api.updateStaff(adminId = id, username = username, fullName = fullName)
    suspend fun deleteStaff(id: String) =
        api.deleteStaff(adminId = id)

    // Deposits & Withdrawals
    suspend fun getDeposits() = api.getDeposits()
    suspend fun createDeposit(userId: String, categoryId: String, adminId: String, weight: Double) =
        api.createDeposit(userId = userId, categoryId = categoryId, adminId = adminId, weight = weight)

    suspend fun getWithdrawals() = api.getWithdrawals()
    suspend fun createWithdrawal(userId: String, adminId: String, amount: Double, paymentMethod: String = "Tunai") =
        api.createWithdrawal(userId = userId, adminId = adminId, amount = amount, paymentMethod = paymentMethod)
}
