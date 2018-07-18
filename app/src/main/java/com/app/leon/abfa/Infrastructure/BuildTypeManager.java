package com.app.leon.abfa.Infrastructure;

import com.app.leon.abfa.BuildConfig;

/**
 * Created by Leon on 1/21/2018.
 */

public final class BuildTypeManager {
    private static String gisBuild = "gis";

    public static boolean isGisBuild() {
        return BuildConfig.FLAVOR == gisBuild;

    }
}
