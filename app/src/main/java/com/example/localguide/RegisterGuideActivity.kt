package com.example.localguide

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.localguide.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.buttonJoin
import kotlinx.android.synthetic.main.activity_register.editTextEmail
import kotlinx.android.synthetic.main.activity_register.editTextName
import kotlinx.android.synthetic.main.activity_register.editTextPassword
import kotlinx.android.synthetic.main.activity_register_guide.*

class RegisterGuideActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_guide)

        buttonJoin.setOnClickListener {
            registerGuideAccount()
        }
    }

    fun registerGuideAccount() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("User")
        val email = editTextEmail.text.toString()
        val name = editTextName.text.toString()
        val password = editTextPassword.text.toString()

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing Up...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        val userAuth = mAuth.currentUser
                        val userID = userAuth?.uid
                        val role = "Guide"
                        val image = "No image"
                        val user = User(userID.toString(), email, name, role, image)
                        progressDialog.dismiss()
                        databaseRef.child(userID.toString()).setValue(user).addOnCompleteListener {
                            Toast.makeText(applicationContext,"Added", Toast.LENGTH_SHORT).show()
                            goToMainActivity()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext,"Sorry", Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }

    fun goToMainActivity() {
        val intentRegisterActivity = Intent(this, MainActivity::class.java)
        startActivity(intentRegisterActivity)
    }
}
