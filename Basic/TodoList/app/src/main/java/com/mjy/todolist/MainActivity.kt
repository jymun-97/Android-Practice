package com.mjy.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mjy.todolist.databinding.ActivityMainBinding
import com.mjy.todolist.memo.MemoDatabase
import com.mjy.todolist.memo.MemoItem

class MainActivity : AppCompatActivity() {

    private lateinit var memoDB: MemoDatabase
    private lateinit var binding: ActivityMainBinding
    private lateinit var memoAdapter: MemoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memoDB = MemoDatabase.getInstance(this)!!
        initAddButton()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        getAllMemo()
        memoAdapter = MemoAdapter(
            onCheckBoxClicked = {
                updateMemo(it)
            },
            onDeleteClicked = {
                deleteMemo(it)
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = memoAdapter
        }
    }

    private fun initAddButton() {
        binding.addButton.setOnClickListener {
            val memoText = binding.memoEditText.text.toString()

            if (memoText.isBlank()) {
                Toast.makeText(this, "메모를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            insertMemo(MemoItem(null, memoText))
            Toast.makeText(this, "메모가 추가되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertMemo(memoItem: MemoItem) {
        Thread(Runnable {
            memoDB.memoDao().insert(
                memoItem
            )
            getAllMemo()
        }).start()
    }

    private fun getAllMemo() {
        Thread(Runnable {
            val memoList = memoDB.memoDao().getAll()
            runOnUiThread {
                memoAdapter.submitList(memoList)
            }
        }).start()
    }

    private fun deleteMemo(memoItem: MemoItem) {
        Thread(Runnable {
            memoDB.memoDao().delete(
                memoItem
            )
            getAllMemo()
        }).start()
    }

    private fun updateMemo(memoItem: MemoItem) {
        Thread(Runnable {
            memoDB.memoDao().update(
                memoItem
            )
            getAllMemo()
        }).start()
    }
}