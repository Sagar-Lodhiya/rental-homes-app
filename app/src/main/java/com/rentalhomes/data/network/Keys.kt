package com.rentalhomes.data.network

object Keys {

    //    Field Name
    //    Notification Type
    const val ADMIN = 0

    //    Notification
    const val FCM_N_TYPE = "notificationType"
    const val FCM_BODY = "body"
    const val FCM_MESSAGE = "message"

    //Notification Types
    const val SCAN_PROPERTY = 3
    const val RECOMMEND_PROPERTY = 1
    const val LINKED_BUYER = 2
    const val RATTING_PROPERTY = 4
    const val BLOCK_BUYER = 5
    const val DELETE_PROPERTY = 6
    const val UPDATE_PROPERTY = 7
    const val CONTACT_US = 8

    //    KEYS
    const val AGENT = 1
    const val BUYER = 2
    const val LISTED = 1
    const val OFF_MARKET = 2
    const val RECOMMENDED = 1
    const val PROSPECT = 0
    const val PROPERTY_ID = "propertyId"
    const val PROPERTY_ADDRESS = "propertyAddress"
    const val PROPERTY_CITY = "propertyCity"
    const val PROPERTY_QR = "propertyQR"
    const val ADD_RECOMMEND_PROPERTY = 0
    const val IGNORE_RECOMMEND_PROPERTY = 2
    const val LINK_USER = 1
    const val UNLINK_USER = 2
    const val AGENT_ID = "agentId"
    const val PROPERTY_CREATED = "propertyCreated"
    const val PROPERTY_THUMB = "propertyThumb"
    const val BUYER_ID = "buyerId"
    const val IS_OTHER_USER = "isOtherUser"
    const val OTHER_USER_ID = "otherUserId"

    const val SET_CONNECTION_REQ = 2
    const val SET_CONNECTION_ACC_REJ = 3
    const val N_DELETE_EVENT = 4
    const val DELETE_USER_PRO_BY_ADMIN = 5
    const val ACTION_USER_PRO_BY_ADMIN = 6 //Block/Unblock/Suspend/Active user by Admin
    const val ADMIN_NOTIFICATION = 7
    const val POST_LIKE = 8
    const val POST_COMMENT = 9
    const val ADD_POST_IN_GROUP = 10
    const val CHAT = 12
    const val USER_ID = "userId"
    const val ACCESS_TOKEN = "accessToken"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val PHONE_NUMBER = "phoneNumber"
    const val EMAIL = "email"
    const val AGE = "age"
    const val GENDER = "gender"
    const val LOGIN_VIA = "loginVia"
    const val PASSWORD = "password"
    const val DEVICE_TYPE = "deviceType"
    const val DEVICE_TYPE_VALUE = 2
    const val DEVICE_TOKEN = "deviceToken"
    const val DATE = "date"
    const val USER_LOGGED_OUT = 4
    const val USER_TYPE = "userType"
    const val IS_PROFILE_REMOVE = "isProfileRemove"

    const val NOTIFICATION_NUMBER = "notificationNumber"
    const val MESSAGE = "message"
    const val CHAT_USERS = "chatUsers"

    //  API Name
//  Google Place Key
//  public static final String GOOGLE_API_KEY = "AIzaSyAQUaWSRjqE-xMPrNZtM15_qeZq-udH0W0";
    const val GOOGLE_REQUEST_CODE = 100

    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer"
    const val API_BUYER = "buyer/"
    const val API_AGENT = "agent/"

    //  API names
    const val API_LOGIN = "login"
    const val API_LOGOUT = "logoutuser"
    const val API_SIGNUP = "signUp"
    const val API_FORGET_PASSWORD = "forgotPassword"
    const val API_CHANGE_PASSWORD = "changePassword"
    const val API_CHANGE_EMAIL = "changeEmail"
    const val API_GET_PROFILE = "getProfile"
    const val API_GET_PROFILE_DATA = "getProfileData"
    const val API_SET_PROFILE = "setProfile"
    const val API_ADD_NON_LISTED = "addNonListedProperty"
    const val API_TERMS_PRIVACY = "getTermsAndPrivacy"
    const val API_SEND_FEEDBACK = "sendFeedback"
    const val API_GET_AFFILIATIONS = "getAffiliations"
    const val API_VERIFY_PROMO_CODE = "verifyPromoCode"
    const val API_GET_NON_LISTED = "getNonListedPropertyList"
    const val API_GET_PROPERTY_BUYER = "getPropertylistBuyer"
    const val API_PROPERTY_ADDED_QR_SCAN = "propertyAddedByQRScan"
    const val API_GET_PROPERTY_AGENT = "getPropertyListAgent"
    const val API_GET_RECOMMEND_BUYER = "getPropertyRecommendationBuyer"
    const val API_GET_RECOMMEND_AGENT = "getRecommendListAgent"
    const val API_LIKE_PROSPECTS = "likeProspects"
    const val API_GET_TIER_3 = "listTier3Data"
    const val API_GET_CUSTOM_TEMPLATE_LIST = "customTempList"
    const val API_SET_TIER_3 = "setTier3"
    const val API_CREATE_CUSTOM_TEMPLATE = "createCustomTemplate"
    const val API_SET_DEFAULT_TEMPLATE = "setDefaultTemplate"
    const val API_ADD_ITEMS_IN_TEMPLATE = "addItemsInTemplate"
    const val API_GET_STATISTICS_LIST = "getStatisticsList"
    const val API_SET_FILTER_TYPE = "setFilterType"
    const val API_GET_PROPARE_DATA = "getNumberOfPropares"
    const val API_GET_TIER_1_DETAILS = "getTier1Details"
    const val API_SET_TIER_1_DETAILS = "setTier1Details"
    const val API_SET_TIER_1_RATINGS = "setTier1RatingScore"
    const val API_GET_TIER_2 = "getTier2"
    const val API_SET_TIER_2 = "setTier2"
    const val API_ADD_IGNORE_PROPERTY = "addIgnoreProperty"
    const val API_DELETE_PROPERTY = "deleteProperty"
    const val API_EDIT_NON_LISTED = "editNonListedProperty"
    const val API_LINK_USER_LIST = "linkUserList"
    const val API_LINK_UNLINK_USER = "linkUnlinkUser"
    const val API_COMPARE_WITH_USER = "compareWithUser"
    const val API_USER_ADDED_BY_QR_SCAN = "userAddedByQRScan"
    const val API_COMPARE_PROPERTIES = "compareNow"
    const val API_GET_GRAPH_DETAILS = "getGraphDetails"
    const val API_SEND_OTP_MAIL = "sendOtpMail"
    const val API_OTP_VERIFICATION = "otpVerification"
    const val API_GET_NOTIFICATION_LIST = "getNotificationList"
    const val API_RATING_DATA = "ratingData"
    const val API_SET_PROPERTY_STATUS = "setPropertyStatus"
    const val API_GET_PROPERTY_DETAILS = "getPropertyDetails"

    //    setup params
    const val PROFILE_PIC = "profilePic"
    const val PROPERTY_IMAGE = "propertyImage"
    const val ADDRESS = "address"
    const val PROPERTY_CITY_TEXT = "propertyCity"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val BED_COUNT = "bedCount"
    const val BATH_COUNT = "bathCount"
    const val CAR_COUNT = "carCount"
    const val DESCRIPTION = "description"
    const val LAND_SIZE = "landSize"
    const val FILTER_TYPE = "filterType"
    const val IS_SCAN = "isScan"
    const val MOBILE = "mobile"
    const val HAS_CALL = "hasCall"
    const val HAS_SMS = "hasSMS"
    const val HAS_EMAIL = "hasEmail"
    const val AVAILABILITY_TIME = "availabilityTime"
    const val ANSWER_1 = "answer1"
    const val ANSWER_2 = "answer2"
    const val PARTNERS = "partners"
    const val PREFERRED_SUBURBS = "preferredSuburbs"
    const val MIN_PRICE = "minPrice"
    const val MAX_PRICE = "maxPrice"
    const val POST_CODE = "postCode"
    const val AGENCY_NAME = "agencyName"
    const val SUBURB = "suburb"
    const val TIER_3_DATA = "tier3Data"
    const val CATEGORY_ID = "categoryId"
    const val AGENT_DATA = "AGENT_DATA"
    const val NON_LISTED = "NON_LISTED"
    const val BUYER_RECOMMENDATION = "BUYER_RECOMMENDATION"
    const val TYPE = "TYPE"
    const val FROM_ACTIVITY = "fromActivity"
    const val COMPARE_ACTIVITY = "compareActivity"
    const val BUYER_HOME_ACTIVITY = "buyerHomeActivity"

    const val PROPERTY_STATUS_FOR_SALE = "For Sale"
    const val PROPERTY_STATUS_AUCTION = "Auction"
    const val PROPERTY_STATUS_UNDER_OFFER = "Under Offer"
    const val PROPERTY_STATUS_UNDER_CONTRACT = "Under Contract"
    const val PROPERTY_STATUS_SOLD = "Sold"

    const val PROPERTY_VALUE_FOR_SALE = 1
    const val PROPERTY_VALUE_AUCTION = 2
    const val PROPERTY_VALUE_UNDER_OFFER = 3
    const val PROPERTY_VALUE_UNDER_CONTRACT = 4
    const val PROPERTY_VALUE_SOLD = 5

}