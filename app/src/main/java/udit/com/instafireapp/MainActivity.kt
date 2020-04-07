package udit.com.instafireapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import udit.com.instafireapp.adapters.PostAdapter
import udit.com.instafireapp.models.Post
import udit.com.instafireapp.models.User

private const val TAG = "MainActivity"
private const val EXTRA_USERNAME = "EXTRAUSERNAME"

open class MainActivity : AppCompatActivity() {

    private var signedInUser: User? = null
    private lateinit var firebaseDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var postAdapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        posts = mutableListOf()
        postAdapter = PostAdapter(this, posts)

        rvPost.adapter = postAdapter
        rvPost.layoutManager = LinearLayoutManager(this)

        firebaseDb = FirebaseFirestore.getInstance()

        firebaseDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG, "Signed in user $signedInUser")
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, exception.localizedMessage)
            }

        var postReference = firebaseDb
            .collection("posts")
            .limit(20)
            .orderBy("creationTime", Query.Direction.DESCENDING)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            supportActionBar?.title = username
            postReference = postReference.whereEqualTo("user.username", username)
        }

        postReference.addSnapshotListener { snapshot, exception ->

            if (exception != null || snapshot == null) {

                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            postAdapter.notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuProfile) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
