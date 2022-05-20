package mjy.usedtradeapp.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mjy.usedtradeapp.R
import mjy.usedtradeapp.databinding.FragmentMypageBinding

class MyPageFragment: Fragment(R.layout.fragment_mypage) {

    private lateinit var binding: FragmentMypageBinding

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding

        initViews()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            unLockViewsAfterSignOut()
        } else {
            lockViewsAfterSignIn()
        }
    }

    private fun initViews() {
        initSignInOutbutton()
        initSignUpButton()
        initEmailEditText()
        initPasswordEditText()
    }

    private fun initSignInOutbutton() {
        binding.signInOutButton.isEnabled = false

        binding.signInOutButton.setOnClickListener {
            if (auth.currentUser == null) {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            successSignIn()
                        } else {
                            Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                auth.signOut()
                unLockViewsAfterSignOut()
            }
        }
    }
    private fun initSignUpButton() {
        binding.signUpButton.isEnabled = false

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "회원가입이 정상적으로 처리되었습니다", Toast.LENGTH_SHORT).show()
                        successSignIn()
                    } else {
                        Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun initPasswordEditText() {
        binding.passwordEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
    }
    private fun initEmailEditText() {
        binding.emailEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
        } else {
            lockViewsAfterSignIn()
        }
    }

    private fun lockViewsAfterSignIn() {
        binding.emailEditText.setText(auth.currentUser?.email)
        binding.passwordEditText.setText("***********")
        binding.emailEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false
        binding.signUpButton.isEnabled = false
        binding.signInOutButton.text = "SIGN-OUT"
    }

    private fun unLockViewsAfterSignOut() {
        binding.emailEditText.text.clear()
        binding.emailEditText.isEnabled = true
        binding.passwordEditText.text.clear()
        binding.passwordEditText.isEnabled = true

        binding.signInOutButton.text = "SIGN-IN"
        binding.signUpButton.isEnabled = false
        binding.signInOutButton.isEnabled = false
    }
}