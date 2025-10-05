package com.example.familydirectory;

import android.app.Application
import com.example.familydirectory.data.cloudinary.CloudinaryManager

class FamilyDirectoryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Cloudinary
        CloudinaryManager.initialize(this)

        // Initialize other SDKs if needed
    }
}
