package com.example.localguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.localguide.Model.User
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_user_profile.*


class DisplayUserProfile : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_user_profile)

        databaseRef = FirebaseDatabase.getInstance().getReference("User")

        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot?.exists()) {
                    val uid = intent.getStringExtra(SigninActivity.EXTRA_UID)
                    val user = dataSnapshot.child("bwRkRbl8PlMcXamlIWs02S2eDi02").getValue(User::class.java)
                    textViewUserName.text = user?.name
                }
                else {
                    Toast.makeText(applicationContext,"Sorry, record not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"Sorry, record not found", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
