package com.example.weather_app.data.firebase

import com.example.weather_app.domain.model.FavoriteCity
import com.google.firebase.database.*

class FirebaseFavoritesRepository {

    private val db = FirebaseDatabase.getInstance()

    fun observeFavorites(userId: String, onResult: (List<FavoriteCity>) -> Unit) {
        val ref = db.getReference("favorites").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(FavoriteCity::class.java)
                }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addFavorite(userId: String, city: FavoriteCity) {
        db.getReference("favorites")
            .child(userId)
            .child(city.id)
            .setValue(city)
    }

    fun deleteFavorite(userId: String, id: String) {
        db.getReference("favorites")
            .child(userId)
            .child(id)
            .removeValue()
    }

    fun updateNote(userId: String, id: String, note: String) {
        db.getReference("favorites")
            .child(userId)
            .child(id)
            .child("note")
            .setValue(note)
    }
}
