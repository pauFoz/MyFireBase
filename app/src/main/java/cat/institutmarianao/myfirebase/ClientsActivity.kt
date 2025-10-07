package cat.institutmarianao.myfirebase


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Api
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ClientsActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter
    private val clientList = mutableListOf<Client>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clients)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        recyclerView = findViewById(R.id.clientsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        clientAdapter = ClientAdapter(clientList)
        recyclerView.adapter = clientAdapter

        loadClientsFromFirestore()
        addButton.setOnClickListener {
            saveUserOnClickListener(emailEditText, nameEditText, ageEditText)
        }


    }
    private fun saveUserOnClickListener(
        emailEditText: EditText, nameEditText: EditText, ageEditText: EditText
    ) {
        val email = emailEditText.text.toString()
        val name = nameEditText.text.toString()
        val ageText = ageEditText.text.toString()
        val age = ageText.toIntOrNull()

        if (email.isEmpty() || name.isEmpty() || ageText.isEmpty()) {
            Toast.makeText(
                this, "Email, name and age required", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (age == null) {
            Toast.makeText(this, "Age must be a valid number", Toast.LENGTH_SHORT).show()
            return
        }

        val client = hashMapOf(
            "name" to name, "email" to email, "age" to age
        )

        // * Firestore add document with documentId to collection * //
        db.collection("clients").document(email).set(client).addOnSuccessListener {
            loadClientsFromFirestore()
            Log.d("Firestore", "Document saved with ID: ${email}")
            Log.d("Firestore", "Email: $email | Name: $name | Age: $age")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding document", e)
        }

    }
    private fun loadClientsFromFirestore() {
        // * Firestore get all documents from collection * //
        db.collection("clients").get().addOnSuccessListener { result ->
            clientList.clear() // clear list before get new data
            for (document in result) {
                val client = document.toObject(Client::class.java)
                clientList.add(client)
            }
            clientAdapter.notifyDataSetChanged() // refresh UI
        }.addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents.", exception)
        }
    }

}