package com.androho.elektroeibauer.Database

import com.androho.elektroeibauer.Interfaces.FirestoreMaterialFromDBCallback
import com.androho.elektroeibauer.Interfaces.FirestoreTimeCallback
import com.androho.elektroeibauer.Interfaces.FirestoreWokersFromDbCallback
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.Objects.Workers
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class GoogleFirebase {

    companion object {

        @Volatile var loadedUpdatedAtFromDB : Boolean= false

        lateinit var materialLastUpdatedDb: Timestamp

        lateinit var db: FirebaseFirestore

        var valueLock = Object()

        lateinit  var auth :FirebaseAuth


        fun createAuthConnection(){
            auth = Firebase.auth
        }
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
            Material.materials = ArrayList<Material>()
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
                db.collection("Material").document(mat.id.toString()).set(mat)

            }

            val timeTo = hashMapOf(
                "time" to Timestamp.now()
            )

            db.collection("Admin").document("MaterialLastUpdatedAt").set(timeTo)


        }

        fun loadWorkersFromDb(firestoreWokersCallback : FirestoreWokersFromDbCallback) {
            db.collection("Arbeiter").get().addOnSuccessListener {
                result ->
                Workers.workerArray = ArrayList<Workers>()
                for (document in result){
                    var doc = document
                    var worker = doc.toObject<Workers>()
                    Workers.workerArray.add(worker)
                }
            }.addOnCompleteListener {
                firestoreWokersCallback.onSuccessCallback()
            }.addOnFailureListener {
                firestoreWokersCallback.onFailureCallback()
            }
        }

        fun deleteWorkerFromDB(workerName: String){
            db.collection("Arbeiter").document(workerName).delete()
            var timeTo = hashMapOf(
                "time" to Timestamp.now()
            )
            db.collection("Admin").document("MaterialLastUpdatedAt").set(timeTo)
        }

        fun updateWorkersToDB(){
            for (worker in Workers.workerArray){
                db.collection("Arbeiter").document(worker.worker.toString()).set(worker)
            }
            var timeTo = hashMapOf(
                "time" to Timestamp.now()
            )
            db.collection("Admin").document("MaterialLastUpdatedAt").set(timeTo)

        }

        fun deleteWorkerFromDb(workerName : String){
            GoogleFirebase.db.collection("Arbeiter").document(workerName).delete()
        }

    }
}
