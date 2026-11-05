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
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("balance") val balance: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Fungsi Ekstensi (Mappers) untuk mengubah UserItemResponse dari API menjadi UserUiModel
fun UserItemResponse.toUiModel(): UserUiModel {
    val rawBalance = this.balance ?: "0"
    val formattedBalance = if (rawBalance.startsWith("Rp")) rawBalance else "Rp$rawBalance"
    return UserUiModel(
        userId = this.userId ?: "",
        name = this.name ?: "",
        phoneNumber = this.phoneNumber ?: "",
        address = this.address ?: "",
        balance = formattedBalance,
        createdAt = this.createdAt ?: ""
    )
}
