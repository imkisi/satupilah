package com.example.satupilah.data.model

import com.google.gson.annotations.SerializedName

// Response Umum (Create, Update, Delete)
data class ApiResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)

// UI Model Categories
data class WasteCategoryUi(
    val id: String,
    val name: String,
    val price: String,
    val unit: String = "kg"
)

// Response Categories
data class CategoryListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<CategoryItemResponse> = emptyList()
)

data class CategoryItemResponse(
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("category_name") val categoryName: String? = null,
    @SerializedName("price_per_kg") val pricePerKg: String? = null,
    @SerializedName("unit") val unit: String? = "kg"
)

fun CategoryItemResponse.toUiModel(): WasteCategoryUi {
    return WasteCategoryUi(
        id = this.categoryId ?: "",
        name = this.categoryName ?: "",
        price = this.pricePerKg ?: "0",
        unit = this.unit ?: "kg"
    )
}

// UI Model Staff
data class AdminUi(
    val id: String,
    val username: String,
    val fullName: String
)

// Response Staff
data class StaffListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<StaffItemResponse> = emptyList()
)

data class StaffItemResponse(
    @SerializedName("admin_id") val adminId: String? = null,
    @SerializedName("staff_id") val staffId: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

fun StaffItemResponse.toUiModel(): AdminUi {
    return AdminUi(
        id = this.adminId ?: this.staffId ?: "",
        username = this.username ?: "",
        fullName = this.fullName ?: ""
    )
}

// UI Model Transactions
enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}

data class TransactionItemUi(
    val id: String,
    val type: TransactionType,
    val date: String,
    val time: String,
    val amount: String,
    val userName: String,
    val itemsDetail: List<String> = emptyList()
)

// Response Deposits (Setor Sampah)
data class DepositListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<DepositItemResponse> = emptyList()
)

data class DepositItemResponse(
    @SerializedName("deposit_id") val depositId: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("weight_kg") val weightKg: String? = null,
    @SerializedName("total_earned") val totalEarned: String? = null,
    @SerializedName("total_price") val totalPrice: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("category_name") val categoryName: String? = null,
    @SerializedName("admin_name") val adminName: String? = null
)

fun DepositItemResponse.toUiModel(): TransactionItemUi {
    val createdStr = createdAt ?: ""
    val dateTimeParts = createdStr.split(" ")
    val dateStr = if (dateTimeParts.isNotEmpty() && dateTimeParts[0].isNotBlank()) dateTimeParts[0] else "Hari Ini"
    val timeStr = if (dateTimeParts.size > 1) dateTimeParts[1].take(5) else ""
    val weightValue = weight ?: weightKg ?: "0"
    val priceValue = totalEarned ?: totalPrice ?: "0"
    return TransactionItemUi(
        id = depositId ?: System.currentTimeMillis().toString(),
        type = TransactionType.DEPOSIT,
        date = dateStr,
        time = timeStr,
        amount = "Rp${priceValue}",
        userName = userName ?: "Nasabah",
        itemsDetail = listOf("${categoryName ?: "Sampah"} · ${weightValue}kg (Petugas: ${adminName ?: "Petugas Pos"})")
    )
}

// Response Withdrawals (Pencairan Saldo)
data class WithdrawalListResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<WithdrawalItemResponse> = emptyList()
)

data class WithdrawalItemResponse(
    @SerializedName("withdrawal_id") val withdrawalId: String? = null,
    @SerializedName("amount") val amount: String? = null,
    @SerializedName("amount_withdrawn") val amountWithdrawn: String? = null,
    @SerializedName("ammount_withdrawan") val ammountWithdrawan: String? = null,
    @SerializedName("payment_method") val paymentMethod: String? = "Tunai",
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("admin_name") val adminName: String? = null
)

fun WithdrawalItemResponse.toUiModel(): TransactionItemUi {
    val createdStr = createdAt ?: ""
    val dateTimeParts = createdStr.split(" ")
    val dateStr = if (dateTimeParts.isNotEmpty() && dateTimeParts[0].isNotBlank()) dateTimeParts[0] else "Hari Ini"
    val timeStr = if (dateTimeParts.size > 1) dateTimeParts[1].take(5) else ""
    val amountVal = amount ?: amountWithdrawn ?: ammountWithdrawan ?: "0"
    return TransactionItemUi(
        id = withdrawalId ?: System.currentTimeMillis().toString(),
        type = TransactionType.WITHDRAWAL,
        date = dateStr,
        time = timeStr,
        amount = "Rp${amountVal}",
        userName = userName ?: "Nasabah",
        itemsDetail = listOf("Petugas: ${adminName ?: "Petugas Pos"} (${paymentMethod ?: "Tunai"})")
    )
}
