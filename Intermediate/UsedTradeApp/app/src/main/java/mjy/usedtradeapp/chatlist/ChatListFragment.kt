package mjy.usedtradeapp.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mjy.usedtradeapp.DBKey.Companion.CHILD_CHAT
import mjy.usedtradeapp.DBKey.Companion.DB_USERS
import mjy.usedtradeapp.R
import mjy.usedtradeapp.chatdetail.ChatRoomActivity
import mjy.usedtradeapp.databinding.FragmentChatlistBinding

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding
    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var chatDB: DatabaseReference

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val chatRoomList = mutableListOf<ChatListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatlistBinding.bind(view)

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }
        })
        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        chatRoomList.clear()

        if (auth.currentUser == null) {
            return
        }
        auth.currentUser?.let { user ->
            chatDB = Firebase.database.reference.child(DB_USERS).child(user.uid).child(CHILD_CHAT)
            chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val model = it.getValue(ChatListItem::class.java)
                        model ?: return

                        chatRoomList.add(model)
                    }
                    chatListAdapter.submitList(chatRoomList)
                    chatListAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        chatListAdapter.notifyDataSetChanged()
    }
}