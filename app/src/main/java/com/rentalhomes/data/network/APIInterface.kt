package com.rentalhomes.data.network

import com.rentalhomes.data.network.Keys.API_ADD_IGNORE_PROPERTY
import com.rentalhomes.data.network.Keys.API_ADD_ITEMS_IN_TEMPLATE
import com.rentalhomes.data.network.Keys.API_ADD_NON_LISTED
import com.rentalhomes.data.network.Keys.API_AGENT
import com.rentalhomes.data.network.Keys.API_BUYER
import com.rentalhomes.data.network.Keys.API_CHANGE_EMAIL
import com.rentalhomes.data.network.Keys.API_CHANGE_PASSWORD
import com.rentalhomes.data.network.Keys.API_COMPARE_PROPERTIES
import com.rentalhomes.data.network.Keys.API_COMPARE_WITH_USER
import com.rentalhomes.data.network.Keys.API_CREATE_CUSTOM_TEMPLATE
import com.rentalhomes.data.network.Keys.API_DELETE_PROPERTY
import com.rentalhomes.data.network.Keys.API_EDIT_NON_LISTED
import com.rentalhomes.data.network.Keys.API_FORGET_PASSWORD
import com.rentalhomes.data.network.Keys.API_GET_AFFILIATIONS
import com.rentalhomes.data.network.Keys.API_GET_CUSTOM_TEMPLATE_LIST
import com.rentalhomes.data.network.Keys.API_GET_GRAPH_DETAILS
import com.rentalhomes.data.network.Keys.API_GET_NON_LISTED
import com.rentalhomes.data.network.Keys.API_GET_NOTIFICATION_LIST
import com.rentalhomes.data.network.Keys.API_GET_PROFILE_DATA
import com.rentalhomes.data.network.Keys.API_GET_PROPARE_DATA
import com.rentalhomes.data.network.Keys.API_GET_PROPERTY_AGENT
import com.rentalhomes.data.network.Keys.API_GET_PROPERTY_BUYER
import com.rentalhomes.data.network.Keys.API_GET_RECOMMEND_AGENT
import com.rentalhomes.data.network.Keys.API_GET_RECOMMEND_BUYER
import com.rentalhomes.data.network.Keys.API_GET_STATISTICS_LIST
import com.rentalhomes.data.network.Keys.API_GET_TIER_1_DETAILS
import com.rentalhomes.data.network.Keys.API_GET_TIER_2
import com.rentalhomes.data.network.Keys.API_GET_TIER_3
import com.rentalhomes.data.network.Keys.API_LIKE_PROSPECTS
import com.rentalhomes.data.network.Keys.API_LINK_UNLINK_USER
import com.rentalhomes.data.network.Keys.API_LINK_USER_LIST
import com.rentalhomes.data.network.Keys.API_LOGIN
import com.rentalhomes.data.network.Keys.API_LOGOUT
import com.rentalhomes.data.network.Keys.API_OTP_VERIFICATION
import com.rentalhomes.data.network.Keys.API_PROPERTY_ADDED_QR_SCAN
import com.rentalhomes.data.network.Keys.API_RATING_DATA
import com.rentalhomes.data.network.Keys.API_SEND_FEEDBACK
import com.rentalhomes.data.network.Keys.API_SEND_OTP_MAIL
import com.rentalhomes.data.network.Keys.API_SET_FILTER_TYPE
import com.rentalhomes.data.network.Keys.API_SET_PROFILE
import com.rentalhomes.data.network.Keys.API_SET_PROPERTY_STATUS
import com.rentalhomes.data.network.Keys.API_SET_TIER_1_DETAILS
import com.rentalhomes.data.network.Keys.API_SET_TIER_1_RATINGS
import com.rentalhomes.data.network.Keys.API_SET_TIER_2
import com.rentalhomes.data.network.Keys.API_SET_TIER_3
import com.rentalhomes.data.network.Keys.API_SIGNUP
import com.rentalhomes.data.network.Keys.API_TERMS_PRIVACY
import com.rentalhomes.data.network.Keys.API_USER_ADDED_BY_QR_SCAN
import com.rentalhomes.data.network.Keys.API_VERIFY_PROMO_CODE
import com.rentalhomes.data.network.Keys.AUTHORIZATION
import com.rentalhomes.data.network.model.requestModel.*
import com.rentalhomes.data.network.model.responseModel.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface APIInterface {
    /**
     * @author Common APIs - Requests
     * */

    @POST(API_TERMS_PRIVACY)
    fun getTermsPrivacy(
        @Body request: TermsPrivacyRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<TermsPrivacyResponse>

    @POST(API_GET_AFFILIATIONS)
    fun getAffiliations(
        @Body request: CommonRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetAffiliationsResponse>

    @POST(API_SEND_FEEDBACK)
    fun sendFeedback(
        @Body request: SendFeedbackRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_CHANGE_EMAIL)
    fun changeEmail(
        @Body request: ChangeEmailRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_CHANGE_PASSWORD)
    fun changePassword(
        @Body request: ChangePasswordRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_GET_NOTIFICATION_LIST)
    fun getNotificationList(
        @Body request: CommonRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetNotificationListResponse>

    @POST(API_LOGOUT)
    fun logout(
        @Body request: LogoutRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_SIGNUP)
    fun signUp(@Body request: SignUpRequest): Call<AuthResponse>

    @POST(API_LOGIN)
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST(API_FORGET_PASSWORD)
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<CommonResponse>

    @POST(API_VERIFY_PROMO_CODE)
    fun verifyPromoCode(@Body request: VerifyPromoRequest): Call<CommonResponse>

    @POST(API_SEND_OTP_MAIL)
    fun sendOTPMail(@Body request: GetOTPRequest): Call<CommonResponse>

    @POST(API_OTP_VERIFICATION)
    fun otpVerification(@Body request: OtpVerificationRequest): Call<CommonResponse>

    @POST(API_GET_PROFILE_DATA)
    fun getProfile(
        @Body request: GetProfileRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetProfileResponse>

    /**
     * @author Buyer APIs
     */

    @POST(API_BUYER + API_GET_NON_LISTED)
    fun getNonListedProperty(
        @Body request: CommonRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetNonListedResponse>

    @POST(API_BUYER + API_GET_PROPERTY_BUYER)
    fun getPropertyListBuyer(
        @Body request: GetPropertyListBuyerRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetPropertyListBuyerResponse>

    @POST(API_BUYER + API_PROPERTY_ADDED_QR_SCAN)
    fun addPropertyScan(
        @Body request: AddPropertyScanRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_GET_RECOMMEND_BUYER)
    fun getRecommendListBuyer(
        @Body request: CommonRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetRecommendListBuyerResponse>

    @POST(API_BUYER + API_GET_TIER_3)
    fun getTier3Data(
        @Body request: AddPropertyScanRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetTier3>

    @POST(API_BUYER + API_GET_CUSTOM_TEMPLATE_LIST)
    fun getCustomTemplateList(
        @Body request: AddPropertyScanRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetTier3>

    @POST(API_BUYER + API_SET_TIER_3)
    fun setTier3Data(
        @Body request: SetTier3DataRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_ADD_ITEMS_IN_TEMPLATE)
    fun addItemsInTemplate(
        @Body request: AddItemsInTemplateRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<AddItemsInTemplateResponse>

    @POST(API_BUYER + API_CREATE_CUSTOM_TEMPLATE)
    fun createCustomTemplates(
        @Body request: CreateCustomTemplateRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_GET_TIER_1_DETAILS)
    fun getTier1Details(
        @Body request: AddPropertyScanRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetTier1Details>

    @POST(API_BUYER + API_SET_TIER_1_DETAILS)
    fun setTier1Details(
        @Body request: SetTier1Request, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_SET_TIER_1_RATINGS)
    fun setTier1Ratings(
        @Body request: SetTier1RatingRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_GET_TIER_2)
    fun getTier2Details(
        @Body request: AddPropertyScanRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetTier2>

    @POST(API_BUYER + API_SET_TIER_2)
    fun setTier2(
        @Body request: SetTier2Request, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_ADD_IGNORE_PROPERTY)
    fun addIgnoreProperty(
        @Body request: AddIgnorePropertyRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_DELETE_PROPERTY)
    fun deleteProperty(
        @Body request: DeletePropertyRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_EDIT_NON_LISTED)
    fun editNonListed(
        @Body request: RequestBody, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    /*@POST(API_BUYER + API_GET_PROFILE_DATA)
    fun getBuyerProfile(
        @Body request: GetProfileRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetProfileResponse>*/

    @POST(API_BUYER + API_SET_PROFILE)
    fun setBuyerProfile(
        @Body request: RequestBody, @Header(AUTHORIZATION) accessToken: String
    ): Call<AuthResponse>

    @POST(API_BUYER + API_ADD_NON_LISTED)
    fun addNonListedProperty(
        @Body request: RequestBody, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_LINK_USER_LIST)
    fun linkUserList(
        @Body request: CommonRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<LinkUserListResponse>

    @POST(API_BUYER + API_LINK_UNLINK_USER)
    fun linkUnlinkUser(
        @Body request: LinkUnlinkUserRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_BUYER + API_COMPARE_WITH_USER)
    fun compareWithUser(
        @Body request: CompareUserRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CompareUserResponse>

    @POST(API_BUYER + API_COMPARE_PROPERTIES)
    fun compareNow(
        @Body request: CompareNowRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CompareNowResponse>

    @POST(API_BUYER + API_USER_ADDED_BY_QR_SCAN)
    fun userAddByQRScan(
        @Body request: UserAddByQRRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    /**
     * @author AGENT APIs
     */

    @POST(API_AGENT + API_GET_RECOMMEND_AGENT)
    fun getRecommendedAgent(
        @Body request: GetRecommendAgentRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetRecommendedAgentResponse>

    @POST(API_AGENT + API_LIKE_PROSPECTS)
    fun likeProspects(
        @Body request: LikeProspectsRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_AGENT + API_GET_STATISTICS_LIST)
    fun getStatisticsList(
        @Body request: StatisticsListRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<StatisticsResponse>

    @POST(API_AGENT + API_SET_FILTER_TYPE)
    fun setFilters(
        @Body request: SetFilterType, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>

    @POST(API_AGENT + API_GET_PROPARE_DATA)
    fun getPropareData(
        @Body request: GetPropareDataRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetPropareResponse>

    @POST(API_AGENT + API_GET_GRAPH_DETAILS)
    fun getChartData(
        @Body request: GetChartDataRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetChartDataResponse>

    @POST(API_AGENT + API_GET_PROPERTY_AGENT)
    fun getPropertyListAgent(
        @Body request: GetAgentPropertyListRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<GetAgentPropertyListResponse>

    @POST(API_AGENT + API_SET_PROFILE)
    fun setProfile(
        @Body request: RequestBody, @Header(AUTHORIZATION) accessToken: String
    ): Call<AuthResponse>

    @POST(API_AGENT + API_RATING_DATA)
    fun getRatingData(
        @Body request: RatingDataRequest, @Header(AUTHORIZATION) accessToken: String
    ): Call<RatingDataResponse>

    @POST(API_AGENT + API_SET_PROPERTY_STATUS)
    fun setPropertyStatus(
        @Body request: SetPropertyStatus, @Header(AUTHORIZATION) accessToken: String
    ): Call<CommonResponse>
}