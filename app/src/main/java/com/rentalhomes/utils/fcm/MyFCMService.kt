package com.rentalhomes.utils.fcm

import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.rentalhomes.R
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.network.Keys
import com.rentalhomes.data.network.Keys.AGENT_ID
import com.rentalhomes.data.network.Keys.BUYER_ID
import com.rentalhomes.data.network.Keys.PROPERTY_ID
import com.rentalhomes.ui.agent.homescreen.AgentHomeActivity
import com.rentalhomes.ui.agent.propertydetail.PropertyDetailActivity
import com.rentalhomes.ui.agent.recommend.RecommendActivity
import com.rentalhomes.ui.buyer.home.BuyerHomeActivity
import com.rentalhomes.ui.buyer.propertydetail.BuyerPropertyDetailActivity
import com.rentalhomes.ui.buyer.recommend.BuyerRecommendActivity
import java.util.Random

class MyFCMService : FirebaseMessagingService() {
    private var notificationManager: NotificationManager? = null

    companion object {
        private val TAG = MyFCMService::class.simpleName

        private const val N_GROUP_ID = "com.propare"
        private const val N_CHANNEL_ID = "my_notification_channel"

        const val USER_LOGGED_OUT_NOTIFICATION = "$N_GROUP_ID USER_LOGGED_OUT_NOTIFICATION"

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "MyFCM service generated Token : $token")
        RentalHomesApp.appSessionManager.setDeviceToken(this, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(messageBody: RemoteMessage) {
        try {

            var intent: Intent? = null

            val map: Map<String, String> = messageBody.data
            if (map != null) {
                Log.e(TAG, Gson().toJson(map))
            }

//            val notificationType: Int = messageBody.data.get(Keys.FCM_N_TYPE)?.toInt() ?: -1
//            val message: String = messageBody.data.get(Keys.FCM_BODY)?.trim() ?: ""
            var message: String
            var contentTitle = applicationContext.getString(R.string.app_name)
            val notificationType: Int = messageBody.data.get(Keys.FCM_N_TYPE)?.toInt() ?: -1
            if (notificationType == 12) {
                message = messageBody.data.get(Keys.FCM_BODY)?.trim() ?: ""
            } else {
                message = messageBody.data.get(Keys.FCM_MESSAGE)?.trim() ?: ""
            }

            val notificationNumber = generateRandom()

            Log.e("notificationType : ", notificationType.toString())
            Log.e("messageBody : ", message)
            Log.e("messageBody Full : ", "$messageBody")

            when (notificationType) {
                /*Keys.USER_LOGGED_OUT -> {
                    val user = RentalHomesApp.appSessionManager.getUser(RentalHomesApp.rentalHomesAppInstance)!!
                    Log.e(
                        "session activated: ",
                        "${RentalHomesApp.appSessionManager.getSession(this)}"
                    )
                    if (user.userType == 1) {
                        intent =
                            Intent(RentalHomesApp.rentalHomesAppInstance, AgentHomeActivity::class.java)
                    } else if (user.userType == 2) {
                        intent =
                            Intent(RentalHomesApp.rentalHomesAppInstance, BuyerHomeActivity::class.java)
                    }
                    intent?.putExtra(Keys.FCM_N_TYPE, notificationType)
                    intent?.putExtra(Keys.MESSAGE, message)

                }*/
                Keys.SCAN_PROPERTY -> {
                    intent =
                        Intent(RentalHomesApp.rentalHomesAppInstance, PropertyDetailActivity::class.java)
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(PROPERTY_ID, Integer.parseInt(messageBody.data[PROPERTY_ID]!!))
                    intent.putExtra(AGENT_ID, Integer.parseInt(messageBody.data[AGENT_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.RECOMMEND_PROPERTY -> {
                    intent =
                        Intent(RentalHomesApp.rentalHomesAppInstance, BuyerRecommendActivity::class.java)
//                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
//                    intent.putExtra(PROPERTY_ID, Integer.parseInt(messageBody.data[PROPERTY_ID]!!))
//                    intent.putExtra(AGENT_ID, Integer.parseInt(messageBody.data[AGENT_ID]!!))
//                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.LINKED_BUYER -> {

                }
                Keys.RATTING_PROPERTY -> {
                    intent =
                        Intent(RentalHomesApp.rentalHomesAppInstance, PropertyDetailActivity::class.java)
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(PROPERTY_ID, Integer.parseInt(messageBody.data[PROPERTY_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.BLOCK_BUYER -> {
                    intent = Intent(RentalHomesApp.rentalHomesAppInstance, RecommendActivity::class.java)
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(AGENT_ID, Integer.parseInt(messageBody.data[AGENT_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.DELETE_PROPERTY -> {
                    intent = Intent(RentalHomesApp.rentalHomesAppInstance, BuyerHomeActivity::class.java)
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(AGENT_ID, Integer.parseInt(messageBody.data[AGENT_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.UPDATE_PROPERTY -> {
                    intent = Intent(
                        RentalHomesApp.rentalHomesAppInstance,
                        BuyerPropertyDetailActivity::class.java
                    )
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(AGENT_ID, Integer.parseInt(messageBody.data[AGENT_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.CONTACT_US -> {
                    intent = Intent(RentalHomesApp.rentalHomesAppInstance, AgentHomeActivity::class.java)
                    intent.putExtra(BUYER_ID, Integer.parseInt(messageBody.data[BUYER_ID]!!))
                    intent.putExtra(Keys.FCM_N_TYPE, notificationType)
                }
                Keys.CHAT -> {


                }

            }

        } catch (e: Exception) {
//            Log.e(TAG, e.localizedMessage)
            e.printStackTrace()
        }
    }

    private fun getNotificationIcon(): Int {
        return  0
    }

    private fun generateRandom(): Int {
        val random = Random()
        return random.nextInt(9999 - 1000) + 1000
    }
}