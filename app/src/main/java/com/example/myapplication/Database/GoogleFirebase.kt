package com.example.myapplication.Database

import com.example.myapplication.Interfaces.FirestoreMaterialFromDBCallback
import com.example.myapplication.Interfaces.FirestoreTimeCallback
import com.example.myapplication.Objects.Material
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Objects
import javax.security.auth.callback.Callback

class GoogleFirebase {

    companion object {

        @Volatile var loadedUpdatedAtFromDB : Boolean= false

        lateinit var materialLastUpdatedDb: Timestamp

        lateinit var db: FirebaseFirestore

        var valueLock = Object()
          fun createDBConnectionAndLoadMaterialUpdatedAt(firestoreTime: FirestoreTimeCallback)   {



                db = Firebase.firestore
               db.collection("Admin").document("MaterialLastUpdatedAt").get()
                    .addOnSuccessListener { document ->

                        if (document != null) {
                            var asdf = document.get("time") as Timestamp
                            materialLastUpdatedDb = asdf


                        }
                    }
                    .addOnCompleteListener {
                        loadedUpdatedAtFromDB = true
                        firestoreTime.onCallback()

                    }
                   .addOnFailureListener {
                       loadedUpdatedAtFromDB = false
                       firestoreTime.onFailureCallback()
                   }




        }

        fun loadMaterialsFromDb(firestoreMaterialFromDBCallback: FirestoreMaterialFromDBCallback) {
            db.collection("Material").get().addOnSuccessListener { result ->
                for (document in result) {
                    var doc = document
                    var mat = doc.toObject<Material>()
                    Material.materials.add(mat)
                }

            }.addOnCompleteListener {
                firestoreMaterialFromDBCallback.onSuccessCallback()

            }.addOnFailureListener {
                firestoreMaterialFromDBCallback.onFailureCallback()
            }
        }

        fun updateMaterialToDatabase() {
            for (mat in Material.materials) {
                db.collection("Material").document(mat.id.toString()).set(mat, SetOptions.merge())

            }

            val timeTo = hashMapOf(
                "time" to Timestamp.now()
            )

            db.collection("Admin").document("MaterialLastUpdatedAt").set(timeTo)


        }


    }
}
