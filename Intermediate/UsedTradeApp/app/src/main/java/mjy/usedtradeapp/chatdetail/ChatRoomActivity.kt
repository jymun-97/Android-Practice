package mjy.usedtradeapp.chatdetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mjy.usedtradeapp.DBKey.Companion.DB_CHAT
import mjy.usedtradeapp.databinding.ActivityChatDetailBinding

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var chatItemAdapter: ChatItemAdapter
    private var chatDB: DatabaseReference? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val chatList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatItemAdapter = ChatItemAdapter()
        binding.chatRecyclerView.adapter = chatItemAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        initChatDB()
        initSendButton()
    }

    private fun initSendButton() {
        binding.sendButton.setOnClickListener {
            auth.currentUser?.let { user ->
                val message = binding.messageEditText.text.toString()
                if (message.isEmpty()) {
                    Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val chatItem = ChatItem(
                    senderId = user.uid,
                    message = message
                )
                chatDB?.push()?.setValue(chatItem)
            }
        }
    }

    private fun initChatDB() {
        val chatKey = intent.getLongExtra("chatKey", -1)
        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatKey")
        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                chatItemAdapter.submitList(chatList)
                chatItemAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}