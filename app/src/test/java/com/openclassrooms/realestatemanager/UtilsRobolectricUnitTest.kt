package com.openclassrooms.realestatemanager

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.utils.Utils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkCapabilities
import org.robolectric.shadows.ShadowNetworkInfo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.M, Build.VERSION_CODES.Q])
class UtilsRobolectricUnitTest {

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun testIsInternetConnectedOnMarshmallow() {
        val context = getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager).setNetworkCapabilities(
            connectivityManager.activeNetwork,
            networkCapabilities
        )

        assertTrue(Utils.isInternetAvailable(context))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun testIsInternetConnectedOnLollipop() {
        val context = getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Suppress("DEPRECATION")
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_WIFI, 0, true, NetworkInfo.State.CONNECTED
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)

        assertTrue(Utils.isInternetAvailable(context))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun testIsInternetDisconnectedOnMarshmallow() {
        val context = getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(connectivityManager).setNetworkCapabilities(
            connectivityManager.activeNetwork,
            networkCapabilities
        )

        assertFalse(Utils.isInternetAvailable(context))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun testIsInternetDisconnectedOnLollipop() {
        val context = getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        shadowOf(connectivityManager).setActiveNetworkInfo(null)

        assertFalse(Utils.isInternetAvailable(context))
    }

}