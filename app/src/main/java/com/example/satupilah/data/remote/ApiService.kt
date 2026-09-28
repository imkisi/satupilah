package com.example.satupilah.data.remote

import com.example.satupilah.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ================= USERS =================
    @GET("api_users.php")
    suspend fun getUsers(@Query("action") action: String = "read"): Response<UserListResponse>

    @FormUrlEncoded
    @POST("api_users.php")
    suspend fun createUser(
        @Query("action") action: String = "create",
        @Field("name") name: String,
        @Field("phone_number") phoneNumber: String,
        @Field("address") address: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_users.php")
    suspend fun updateUser(
        @Query("action") action: String = "update",
        @Field("user_id") userId: String,
        @Field("name") name: String,
        @Field("phone_number") phoneNumber: String,
        @Field("address") address: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_users.php")
    suspend fun deleteUser(
        @Query("action") action: String = "delete",
        @Field("user_id") userId: String
    ): Response<ApiResponse>


    // ================= CATEGORIES =================
    @GET("api_categories.php")
    suspend fun getCategories(@Query("action") action: String = "read"): Response<CategoryListResponse>

    @FormUrlEncoded
    @POST("api_categories.php")
    suspend fun createCategory(
        @Query("action") action: String = "create",
        @Field("category_name") categoryName: String,
        @Field("price_per_kg") pricePerKg: Double
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_categories.php")
    suspend fun updateCategory(
        @Query("action") action: String = "update",
        @Field("category_id") categoryId: String,
        @Field("category_name") categoryName: String,
        @Field("price_per_kg") pricePerKg: Double
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_categories.php")
    suspend fun deleteCategory(
        @Query("action") action: String = "delete",
        @Field("category_id") categoryId: String
    ): Response<ApiResponse>


    // ================= STAFF (PETUGAS) =================
    @GET("api_staff.php")
    suspend fun getStaff(@Query("action") action: String = "read"): Response<StaffListResponse>

    @FormUrlEncoded
    @POST("api_staff.php")
    suspend fun createStaff(
        @Query("action") action: String = "create",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("full_name") fullName: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_staff.php")
    suspend fun updateStaff(
        @Query("action") action: String = "update",
        @Field("staff_id") staffId: String,
        @Field("username") username: String,
        @Field("full_name") fullName: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("api_staff.php")
    suspend fun deleteStaff(
        @Query("action") action: String = "delete",
        @Field("staff_id") staffId: String
    ): Response<ApiResponse>


    // ================= DEPOSITS =================
    @GET("api_deposits.php")
    suspend fun getDeposits(@Query("action") action: String = "read"): Response<DepositListResponse>

    @FormUrlEncoded
    @POST("api_deposits.php")
    suspend fun createDeposit(
        @Query("action") action: String = "create",
        @Field("user_id") userId: String,
        @Field("category_id") categoryId: String,
        @Field("staff_id") staffId: String,
        @Field("weight_kg") weightKg: Double
    ): Response<ApiResponse>


    // ================= WITHDRAWALS =================
    @GET("api_withdrawals.php")
    suspend fun getWithdrawals(@Query("action") action: String = "read"): Response<WithdrawalListResponse>

    @FormUrlEncoded
    @POST("api_withdrawals.php")
    suspend fun createWithdrawal(
        @Query("action") action: String = "create",
        @Field("user_id") userId: String,
        @Field("staff_id") staffId: String,
        @Field("amount") amount: Double
    ): Response<ApiResponse>
}