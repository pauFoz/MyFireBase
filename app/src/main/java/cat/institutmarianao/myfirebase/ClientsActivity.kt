package cat.institutmarianao.myfirebase


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ClientsActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clients)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {

            val client = hashMapOf("name" to nameEditText.text.toString(), "email" to emailEditText.text.toString(), "age" to ageEditText.text.toString())

            db.collection("clients").add(client).addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document added with ID: ${documentReference.id}")
            }   .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }

        }







    }


}