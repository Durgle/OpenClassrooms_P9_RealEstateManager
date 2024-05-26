package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiManager;

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

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static void requestNotificationPermission(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    0
            );
        }
    }

    @NonNull
    public static String formatPrice(long price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        return "$" + formatter.format(price);
    }

    @NonNull
    public static String formatPropertyArea(long propertyArea) {
        return propertyArea + " sq m";
    }

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

    public static boolean isTablet(Resources resources) {
        return resources.getBoolean(R.bool.isLargeLayout);
    }
}
