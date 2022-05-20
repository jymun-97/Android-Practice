package mjy.tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchedUserActivity: AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var userDB: DatabaseReference

    private val adapter = MatchedUserAdapter()
    private val cardModels = mutableListOf<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        userDB = Firebase.database.reference.child(DBKey.USERS)
        initMatchedUserRecyclerView()

        getMatchUsers()
    }

    private fun initMatchedUserRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.matchedUserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(
                this,
                getString(R.string.str_login_error),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }

    private fun getMatchUsers() {
        val matchedDB = userDB
            .child(getCurrentUserID())
            .child(DBKey.LIKED_BY)
            .child(DBKey.MATCH)

        matchedDB.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getUserByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getUserByKey(userId: String) {
        userDB.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cardModels.add(CardModel(userId, snapshot.child(DBKey.NAME).value.toString()))
                adapter.submitList(cardModels)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}