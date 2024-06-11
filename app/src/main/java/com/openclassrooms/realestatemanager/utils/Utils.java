package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    /**
     * Conversion of a real estate price (Dollars to Euros)
     * NOTE: DO NOT DELETE, TO BE SHOWN DURING THE PRESENTATION
     *
     * @param dollars The price in dollars
     * @return The price in euros.
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion of today's date to a more appropriate format
     * NOTE: DO NOT DELETE, TO BE SHOWN DURING THE PRESENTATION
     *
     * @return A string representing today's date in the format "yyyy/MM/dd".
     */
    public static String getTodayDateOld() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Network connection check
     * NOTE: DO NOT DELETE, TO BE SHOWN DURING THE PRESENTATION
     *
     * @param context The context
     * @return A Boolean indicating whether the WiFi is enabled
     */
    public static Boolean isInternetAvailableOld(Context context) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * Real estate price conversion (Euros to Dollars)
     *
     * @param euros Price in Euros
     * @return Price in Dollars
     */
    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros / 0.812);
    }

    /**
     * Convert the current date formatted as "dd/MM/yyyy"
     *
     * @return Date in the format "dd/MM/yyyy"
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Active internet connection check
     *
     * @param context The context
     * @return A Boolean indicating whether there is an active internet connection.
     */
    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //noinspection deprecation
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    /**
     * Requests notification permission for the app
     *
     * @param context  The context
     * @param activity The activity
     */
    public static void requestNotificationPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        0
                );
            }
        }
    }

    /**
     * Formats a price value into a currency string
     *
     * @param price The price
     * @return A formatted string representing the price in US dollars
     */
    @NonNull
    public static String formatPrice(long price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        return "$" + formatter.format(price);
    }

    /**
     * Formats a property area value into a string representing square meters
     *
     * @param propertyArea The property area value
     * @return A string representing the property area in square meters
     */
    @NonNull
    public static String formatPropertyArea(long propertyArea) {
        return propertyArea + " sq m";
    }

    /**
     * Formats an estate address into a single string
     *
     * @param address               The primary address line
     * @param additionalAddressLine An additional address line
     * @param city                  The city
     * @param zipCode               The Zip code
     * @param country               The country
     * @return A formatted string representing the estate address
     */
    @NonNull
    public static String formatEstateAddress(
            @NonNull String address,
            @Nullable String additionalAddressLine,
            @NonNull String city,
            @NonNull String zipCode,
            @NonNull String country
    ) {

        if (additionalAddressLine == null) {
            return String.format("%s%n%s%n%s%n%s", address, city, zipCode, country);
        } else {
            return String.format(
                    "%s%n%s%n%s%n%s%n%s", address, additionalAddressLine, city, zipCode, country
            );
        }
    }

    /**
     * Formats points of interests into a single string
     *
     * @param resources        The resources
     * @param pointOfInterests The points of interests
     * @return A formatted string representing the points of interests
     */
    @NonNull
    public static String formatPointOfInterests(Resources resources, @NonNull List<PointOfInterest> pointOfInterests) {

        StringBuilder concatenatedPointOfInterest = new StringBuilder();
        for (PointOfInterest pointOfInterest : pointOfInterests) {
            concatenatedPointOfInterest.append("- ")
                    .append(resources.getString(pointOfInterest.getLabelResId()))
                    .append(System.lineSeparator());
        }
        return concatenatedPointOfInterest.toString().trim();
    }

    /**
     * Parses a string representing point of interests into a list of PointOfInterest objects
     *
     * @param data The string representing point of interests
     * @return A list of PointOfInterest objects, or null
     */
    @Nullable
    public static List<PointOfInterest> parsePointOfInterests(String data) {

        if (data.isEmpty()) return null;
        String[] pointOfInterests = data.split(",");
        List<PointOfInterest> list = new ArrayList<>();
        for (String value : pointOfInterests) {
            list.add(PointOfInterest.valueOf(value));
        }
        return list;
    }

    /**
     * Checks if the device is a tablet
     *
     * @param resources The resources
     * @return true if the device is a tablet, false otherwise
     */
    public static boolean isTablet(Resources resources) {
        return resources.getBoolean(R.bool.isLargeLayout);
    }

    /**
     * Get the current time in milliseconds
     *
     * @return Current time in milliseconds
     */
    public static long getNow() {
        return System.currentTimeMillis();
    }
}
