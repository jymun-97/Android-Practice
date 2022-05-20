package com.mjy.recyclerview_practice

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjy.recyclerview_practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var memberAdapter: MemberAdapter
    private val memberItems: ArrayList<MemberItem> by lazy {
        arrayListOf(
            MemberItem("test", "000-0000-0000")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        memberAdapter = MemberAdapter(
            this,
            memberItems,
            itemClicked = {
                val intent = Intent(this, DetailActivity::class.java)
                intent.apply {
                    putExtra(IntentKeys.NAME, it.name)
                    putExtra(IntentKeys.PHONE, it.phone)
                }
                startActivity(intent)
            }
        )
        binding.recyclerView.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.addButton.setOnClickListener {
            showAlertDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        memberAdapter.notifyItemInserted(memberItems.lastIndex)
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("New Member!")
            .setView(R.layout.dialog_add)
            .setPositiveButton("추가", DialogInterface.OnClickListener { p, _ ->
                val alert = p as AlertDialog
                val name = alert.findViewById<EditText>(R.id.nameEditText)?.text.toString()
                if (!isValidName(name)) {
                    Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                val phone = alert.findViewById<EditText>(R.id.phoneEditText)?.text.toString()
                if (!isValidPhone(phone)) {
                    Toast.makeText(this, "전화번호를 입력하지 않았거나 잘못된 형식입니다.", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                insertMember(name, convertPhone(phone))
            })
            .setNegativeButton("취소", null).show()
    }

    private fun insertMember(
        name: String,
        phone: String
    ) {
        val newMemberItem = MemberItem(name, phone)
        memberItems.add(newMemberItem)
        memberAdapter.notifyDataSetChanged()
    }

    private fun isValidName(name: String) = name.isNotBlank()
    private fun isValidPhone(phone: String) =
        phone.isNotBlank() && phone.isDigitsOnly() && phone.length == 11
    private fun convertPhone(phone: String) =
        "${phone.subSequence(0, 3)} - ${phone.subSequence(3, 7)} - ${phone.substring(7)}"

}