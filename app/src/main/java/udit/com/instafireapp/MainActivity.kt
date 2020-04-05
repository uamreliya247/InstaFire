package udit.com.instafireapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import udit.com.instafireapp.adapters.PostAdapter
import udit.com.instafireapp.models.Posts

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {


    private lateinit var firebaseDb: FirebaseFirestore
    private lateinit var posts: MutableList<Posts>
    private lateinit var postAdapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        posts = mutableListOf()
        postAdapter = PostAdapter(this, posts)

        rvPost.adapter = postAdapter
        rvPost.layoutManager = LinearLayoutManager(this)


        firebaseDb = FirebaseFirestore.getInstance()

        val postReference = firebaseDb
            .collection("posts")
            .limit(20)
            .orderBy("creationTime", Query.Direction.DESCENDING)

        postReference.addSnapshotListener { snapshot, exception ->

            if (exception != null || snapshot == null) {

                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Posts::class.java)
            posts.clear()
            posts.addAll(postList)
            postAdapter.notifyDataSetChanged()
            for (post in postList) {
                Log.i(TAG, "Posts $post")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuProfile) {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}
