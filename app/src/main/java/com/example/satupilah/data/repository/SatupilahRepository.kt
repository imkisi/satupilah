package com.example.satupilah.data.repository

import com.example.satupilah.data.model.*
import com.example.satupilah.data.remote.RetrofitClient

class SatupilahRepository {

    private val api = RetrofitClient.apiService

    // Categories
    suspend fun getCategories() = api.getCategories()
    suspend fun createCategory(name: String, price: Double) = api.createCategory(categoryName = name, pricePerKg = price)
    suspend fun updateCategory(id: String, name: String, price: Double) = api.updateCategory(categoryId = id, categoryName = name, pricePerKg = price)
    suspend fun deleteCategory(id: String) = api.deleteCategory(categoryId = id)

    // Staff
    suspend fun getStaff() = api.getStaff()
    suspend fun createStaff(username: String, pass: String, fullName: String) = api.createStaff(username = username, password = pass, fullName = fullName)
    suspend fun updateStaff(id: String, username: String, fullName: String) = api.updateStaff(staffId = id, username = username, fullName = fullName)
    suspend fun deleteStaff(id: String) = api.deleteStaff(staffId = id)

    // Deposits & Withdrawals
    suspend fun getDeposits() = api.getDeposits()
    suspend fun createDeposit(userId: String, categoryId: String, staffId: String, weightKg: Double) = api.createDeposit(userId = userId, categoryId = categoryId, staffId = staffId, weightKg = weightKg)

    suspend fun getWithdrawals() = api.getWithdrawals()
    suspend fun createWithdrawal(userId: String, staffId: String, amount: Double) = api.createWithdrawal(userId = userId, staffId = staffId, amount = amount)
}