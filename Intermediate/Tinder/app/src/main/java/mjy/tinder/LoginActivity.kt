package mjy.tinder

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val loginButton: Button by lazy {
        findViewById(R.id.loginButton)
    }
    private val signUpButton: Button by lazy {
        findViewById(R.id.signUpButton)
    }
    private val emailEditText: EditText by lazy {
        findViewById(R.id.emailEditText)
    }
    private val passwordEditText: EditText by lazy {
        findViewById(R.id.passwordEditText)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        initEmailAndPasswordEditText()
    }

    private fun initLoginButton() {
        loginButton.isEnabled = false
        loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.str_login_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initSignUpButton() {
        signUpButton.isEnabled = false
        signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            getString(R.string.str_signUp_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.str_signUp_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initEmailAndPasswordEditText() {
        emailEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }
        passwordEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }
    }

    private fun handleSuccessLogin() {
        val userId = auth.currentUser?.uid.orEmpty()

        if (userId.isEmpty()){
            Toast.makeText(
                this,
                getString(R.string.str_login_error),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val currentUserDB = Firebase.database.reference.child(DBKey.USERS).child(userId)
        val user = mutableMapOf<String, Any>()
        user[DBKey.USER_ID] = userId
        currentUserDB.updateChildren(user)

        finish()
    }

    private fun getInputEmail(): String = emailEditText.text.toString()
    private fun getInputPassword(): String = passwordEditText.text.toString()
}