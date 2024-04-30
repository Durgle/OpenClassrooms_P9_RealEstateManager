package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(additionalAddressLine == null) {
            return address + System.lineSeparator() +
                    city + System.lineSeparator() +
                    zipCode + System.lineSeparator() +
                    country;
        } else {
            return address + System.lineSeparator() +
                    additionalAddressLine + System.lineSeparator() +
                    city + System.lineSeparator() +
                    zipCode + System.lineSeparator() +
                    country;
        }
    }
}