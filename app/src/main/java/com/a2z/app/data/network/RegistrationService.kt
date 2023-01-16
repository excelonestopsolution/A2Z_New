package com.a2z.app.data.network


import com.a2z.app.data.model.RegistrationResponse
import com.a2z.app.util.FieldMapData
import retrofit2.Response
import retrofit2.http.*

interface RegistrationService {

    @FormUrlEncoded
    @POST("self/registration/mobile_process")
    suspend fun postMobileNumber(
        @Field("mobile") mobileNumber: String,
        @Field("is_self") isSelf: String,
    ): RegistrationResponse

    @FormUrlEncoded
    @POST("self/registration/mobile_verification")
    suspend fun mobileNumberVerify(
        @Field("requestId") requestId: String,
        @Field("otp") otp: String,
        @Field("is_self") isSelf: String,
    ):RegistrationResponse


    @FormUrlEncoded
    @POST("self/registration/email_process")
    suspend fun postEmailId(
        @Field("email") email: String,
        @Field("requestId") requestId: String,
        @Field("is_self") isSelf: String,
    ): RegistrationResponse

    @FormUrlEncoded
    @POST("self/registration/email_verification")
    suspend fun verifyEmailId(
        @Field("otp") email: String,
        @Field("requestId") requestId: String,
        @Field("is_self") isSelf: String,
    ): RegistrationResponse

    @FormUrlEncoded
    @POST("self/registration/pan_process")
    suspend fun postPanNumber(
        @Field("requestId") requestId: String,
        @Field("pan_number") panNumber: String,
        @Field("is_self") isSelf: String,
    ): RegistrationResponse


    @FormUrlEncoded
    @POST("self/registration/register")
    suspend fun selfRegister(
        @Field("requestId") requestId: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("outlet_name") outletName: String,
        @Field("outlet_address") outletAddress: String,
    ): RegistrationResponse


    @FormUrlEncoded
    @POST("user/create")
    suspend fun registerFromDistributor(@FieldMap data: FieldMapData): RegistrationResponse


    @GET("self/registration/resend/otp")
    suspend fun resendOtp(
        @Query("requestId") requestId: String,
        @Query("type") type: String,
        @Field("is_self") isSelf: String,
    ): RegistrationResponse


    @GET("user/role/get")
    suspend fun fetchCreateRole(): RegistrationResponse


    @GET
    suspend fun fetchMappingUserList(
        @Url url: String,
        @QueryMap data: FieldMapData
    ): RegistrationResponse

    @GET("sales/created/member")
    suspend fun fetchCompletedUserList(
        @QueryMap data: FieldMapData
    ): RegistrationResponse

    @GET("user/incomplete/registration")
    suspend fun fetchInCompletedUserList(
        @QueryMap data: FieldMapData
    ):RegistrationResponse

}