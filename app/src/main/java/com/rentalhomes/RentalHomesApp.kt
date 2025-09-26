package com.rentalhomes

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

import com.example.firebasechatkotlin.utils.albumloader.MediaLoader
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.utils.Constant
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import timber.log.Timber
import java.util.*

class RentalHomesApp : Application() {

    var list = mutableListOf(1, 2, 3, 4, 5)

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()
        rentalHomesAppInstance = this
        appSessionManager = AppSessionManager.instance!!
        disableModulesInDebugMode()

        Firebase.initialize(this)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        firestore = Firebase.firestore

        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        firestore.firestoreSettings = settings

        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        )

    }

    private fun disableModulesInDebugMode() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    
    companion object {
        lateinit var appSessionManager: AppSessionManager
        var rentalHomesAppInstance: RentalHomesApp? = null
        private val TAG = RentalHomesApp::class.simpleName

        private val instance: Application?
            get() = rentalHomesAppInstance
    }

    fun makeUserOfflineListener() {
        Log.d(TAG, "makeUserOfflineListener is called")
        try {
            val user = appSessionManager.getUser(this)
            val currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
            user?.let { user ->
                if (!user.id.isNullOrEmpty()) {
                    val offlineObject = HashMap<String, Any>()
                    offlineObject.put(Constant.USER_STATE, Constant.OFFLINE)
                    offlineObject.put(Constant.LAST_CHANGED, ServerValue.TIMESTAMP)
                    FirebaseDatabase.getInstance().reference.child(Constant.USERS)
                        .child(currentFirebaseUser.uid)
                        .onDisconnect()
                        .setValue(
                            offlineObject
                        )
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "error: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }

}