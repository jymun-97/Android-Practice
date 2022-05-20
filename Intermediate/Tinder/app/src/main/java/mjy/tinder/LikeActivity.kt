package mjy.tinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class LikeActivity : AppCompatActivity(), CardStackListener {

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var userDB: DatabaseReference

    private val adapter = CardModelAdapter()
    private val cardModels = mutableListOf<CardModel>()
    private val manager by lazy {
        CardStackLayoutManager(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        userDB = Firebase.database.reference.child(DBKey.USERS)

        val currentUserDB = userDB.child(getCurrentUserID())
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(DBKey.NAME).value == null) {
                    showNameInputPopup()
                    return
                }

                getUnselectedUsers()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        initCardStackView()
        initLogOutButton()
        initMatchedListButton()
    }

    private fun initCardStackView() {
        val stackView = findViewById<CardStackView>(R.id.cardStackView)
        stackView.layoutManager = manager
        stackView.adapter = adapter
    }

    private fun initLogOutButton() {
        val logOutButton = findViewById<Button>(R.id.logOutButton)
        logOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initMatchedListButton() {
        val matchedListButton = findViewById<Button>(R.id.matchedListButton)
        matchedListButton.setOnClickListener {
            startActivity(Intent(this, MatchedUserActivity::class.java))
        }
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

    private fun showNameInputPopup() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("이름을 입력해주세요.")
            .setView(editText)
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    saveUserName(editText.text.toString())
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUserName(name: String) {
        val userId = getCurrentUserID()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user[DBKey.USER_ID] = userId
        user[DBKey.NAME] = name
        currentUserDB.updateChildren(user)

        getUnselectedUsers()
    }

    private fun like() {
        val card = cardModels[manager.topPosition - 1]
        cardModels.removeFirst()

        userDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.LIKE)
            .child(getCurrentUserID())
            .setValue(true)

        Toast.makeText(
            this,
            "${card.name}님을 Like 하였습니다.",
            Toast.LENGTH_SHORT
        ).show()

        saveMatchIfOtherUserLikedMe(card.userId)
    }

    private fun dislike() {
        val card = cardModels[manager.topPosition - 1]
        cardModels.removeFirst()

        userDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.DISLIKE)
            .child(getCurrentUserID())
            .setValue(true)

        Toast.makeText(
            this,
            "${card.name}님을 Dislike 하였습니다.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB = userDB
                .child(getCurrentUserID())
                .child(DBKey.LIKED_BY)
                .child(DBKey.LIKE)
                .child(otherUserId)

        otherUserDB.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    userDB.child(getCurrentUserID())
                        .child(DBKey.LIKED_BY)
                        .child(DBKey.MATCH)
                        .child(otherUserId)
                        .setValue(true)

                    userDB.child(otherUserId)
                        .child(DBKey.LIKED_BY)
                        .child(DBKey.MATCH)
                        .child(getCurrentUserID())
                        .setValue(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getUnselectedUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child(DBKey.USER_ID).value != getCurrentUserID()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.LIKE).hasChild(getCurrentUserID()).not()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.DISLIKE).hasChild(getCurrentUserID()).not()
                ) {
                    val userId = snapshot.child(DBKey.USER_ID).value.toString()
                    val name =
                        if (snapshot.child(DBKey.NAME).value != null) snapshot.child(DBKey.NAME).value.toString() else "undecided"

                    cardModels.add(CardModel(userId, name))
                    adapter.submitList(cardModels)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                cardModels.find { it.userId == snapshot.key }?.let {
                    it.name = snapshot.child(DBKey.NAME).value.toString()
                }
                adapter.submitList(cardModels)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> dislike()
            Direction.Right -> like()
            else -> {}
        }
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardDisappeared(view: View?, position: Int) {}
}