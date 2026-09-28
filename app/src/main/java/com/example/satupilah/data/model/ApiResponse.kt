package com.example.satupilah.data.model

import com.google.gson.annotations.SerializedName

// Response Umum (Create, Update, Delete)
data class ApiResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)

// Response Categories
data class CategoryListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<CategoryItemResponse>
)

data class CategoryItemResponse(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("price_per_kg") val pricePerKg: String
)

// Response Staff
data class StaffListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<StaffItemResponse>
)

data class StaffItemResponse(
    @SerializedName("admin_id") val adminId: String,
    @SerializedName("username") val username: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("created_at") val createdAt: String
)

// Response Deposits (Setor Sampah)
data class DepositListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<DepositItemResponse>
)

data class DepositItemResponse(
    @SerializedName("deposit_id") val depositId: String,
    @SerializedName("weight_kg") val weightKg: String,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("admin_name") val adminName: String
)

// Response Withdrawals (Pencairan Saldo)
data class WithdrawalListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<WithdrawalItemResponse>
)

data class WithdrawalItemResponse(
    @SerializedName("withdrawal_id") val withdrawalId: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("admin_name") val adminName: String
)