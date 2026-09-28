package com.example.satupilah.data.model

import com.google.gson.annotations.SerializedName

// Data Model UI untuk dipakai di Composable Screen (seperti UserScreen & UserDetailScreen)
data class UserUiModel(
    val userId: String,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val balance: String,
    val createdAt: String
)

// Data Model Response JSON dari PHP API (users/api_users.php)
data class UserListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<UserItemResponse> = emptyList()
)

data class UserItemResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("address") val address: String,
    @SerializedName("balance") val balance: String,
    @SerializedName("created_at") val createdAt: String
)

// Fungsi Ekstensi (Mappers) untuk mengubah UserItemResponse dari API menjadi UserUiModel
fun UserItemResponse.toUiModel(): UserUiModel {
    return UserUiModel(
        userId = this.userId,
        name = this.name,
        phoneNumber = this.phoneNumber,
        address = this.address,
        balance = "Rp${this.balance}",
        createdAt = this.createdAt
    )
}