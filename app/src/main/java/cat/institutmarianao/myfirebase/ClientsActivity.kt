package cat.institutmarianao.myfirebase


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    private val clientList = mutableListOf<client>()


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

        addButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val client = hashMapOf("name" to name, "email" to email, "age" to age)

            db.collection("clients")
                .document(email) // Fem servir l'email com a ID del document
                .set(client)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "Document saved with ID: ${email}")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }

            db.collection("clients")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("Firestore", "${document.id} => ${document.data}")
                    }
                }
        }


    }

    private fun loadClientsFromFirestore() {
        // * Firestore get all documents from collection * //
        db.collection("clients").get().addOnSuccessListener { result ->
            clientList.clear() // clear list before get new data
            for (document in result) {
                val client = document.toObject(Api.Client::class.java)
                clientList.add(client)
            }
            clientAdapter.notifyDataSetChanged() // refresh UI
        }.addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents.", exception)
        }
    }


}