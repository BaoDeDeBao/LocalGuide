package com.example.localguide

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_user_profile.*


class SigninActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        buttonSignIn.setOnClickListener {
            signInAcount()
        }

        textViewForgot.setOnClickListener {
            goToForgotPasswordActivity()
        }
    }

    fun signInAcount() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val intent = Intent(this, DisplayUserProfile::class.java)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing In...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss()
                        //Toast.makeText(applicationContext, "Signed In", Toast.LENGTH_SHORT).show()

                        databaseRef = FirebaseDatabase.getInstance().getReference("User")

                        databaseRef.addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                if(dataSnapshot?.exists()) {
                                    val uid = intent.getStringExtra(SigninActivity.EXTRA_UID)
                                    //val user = dataSnapshot.child(uid).getValue()
                                    //textViewUserName.text = user?.name
                                    for(u: DataSnapshot in dataSnapshot.children.iterator()) {
                                        //if(u.child("role").equals("Traveler")) {
                                            //Log.d("SigninActivity", String.format("Role  : %s",  u.child("role").getValue()))
                                           // break
                                        //}
                                        val uid= user!!.uid

                                        if(u.key!!.equals(uid)) {
                                            //Log.d("SigninActivity", String.format("Role  : %s",  u.child("role").getValue()))
                                            if(u.child("role").getValue()!!.equals("Traveler")) {
                                                Toast.makeText(applicationContext,"Traveler", Toast.LENGTH_SHORT).show()
                                                //if(user != null) {
                                                //intent.putExtra(EXTRA_UID, uid)
                                                //startActivity(intent)
                                                //}
                                            }
                                            else if(u.child("role").getValue()!!.equals("Guide")) {
                                                Toast.makeText(applicationContext,"Guide", Toast.LENGTH_SHORT).show()
                                                //if(user != null) {
                                                    //intent.putExtra(EXTRA_UID, uid)
                                                    //startActivity(intent)
                                                //}
                                            }
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(applicationContext,"Sorry, record not found", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext,"Sorry, record not found", Toast.LENGTH_SHORT).show()
                            }
                        })

                        //if(user != null) {
                            //val uid= user.uid
                            //Toast.makeText(applicationContext, uid, Toast.LENGTH_SHORT).show()
                            //intent.putExtra(EXTRA_UID, uid)
                            //startActivity(intent)
                        //}
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Sorry", Toast.LENGTH_SHORT).show()
                    }

                }
            })
    }

    fun goToForgotPasswordActivity() {
        val intentForgotPasswordActivity = Intent(this, UserForgotPasswordActivity::class.java)
        startActivity(intentForgotPasswordActivity)
    }

    companion object {
        const val EXTRA_UID = "com.example.localguide.UID"
    }
}
