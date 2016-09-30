package com.nathb.swiftshop.log;

import timber.log.Timber;

public class LogTree extends Timber.DebugTree {

    private static final String LOG_TAG = "SwiftShop_";

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, LOG_TAG + tag, message, t);
    }
}
