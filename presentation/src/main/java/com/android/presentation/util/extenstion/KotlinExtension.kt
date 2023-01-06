package com.android.presentation.util.extenstion

import android.content.res.Resources

val screenWidth: Int
    get() = Resources.getSystem().displayMetrics?.widthPixels ?: 0
