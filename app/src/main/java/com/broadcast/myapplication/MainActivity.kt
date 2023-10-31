package com.broadcast.myapplication


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import android.content.pm.PackageManager
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign


class MainActivity : AppCompatActivity() {

    private lateinit var appList: RecyclerView
    private lateinit var installedApps: MutableList<AppInfo>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var signoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appList = findViewById(R.id.appList1)
        installedApps = ArrayList()
        signoutButton = findViewById(R.id.signOut)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        signoutButton.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Get the user's email address from Firebase Authentication
        val user = auth.currentUser
        val userEmail = user?.email

        // Define the package names you want to retrieve
        val appNamesToRetrieve = listOf(
            "ERPLY Latest",
            "FieldSmart",
            "FieldPro",
            "App Reader",
            "Agent App",
            "Field App"

        )

        val packageManager = packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            val appName = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
            if (appNamesToRetrieve.contains(appName)) {
                val packageName = packageInfo.packageName
                val versionName = packageInfo.versionName
                installedApps.add(AppInfo(appName, packageName, versionName, userEmail))
            }
        }

        // Initialize RecyclerView and Adapter
        val layoutManager = LinearLayoutManager(this)
        appList.layoutManager = layoutManager
        val adapter = AppInfoAdapter(installedApps)
        appList.adapter = adapter

        // Save the filtered list of installed apps under the user's email address
        if (userEmail != null) {
            saveFilteredAppsToFirebase(userEmail, installedApps)
        }
    }

    private fun saveFilteredAppsToFirebase(userEmail: String, apps: List<AppInfo>) {
        val userAppReference = databaseReference.child("user_apps").child(userEmail.replace(".", "_"))
        userAppReference.setValue(apps)
    }
}

