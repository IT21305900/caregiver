package com.example.caregiver.ui.entries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caregiver.R
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caregiver.databinding.ActivityAllEntriesBinding
import com.example.caregiver.ui.model.EntryData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllEntries : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataList: ArrayList<EntryData>
    private lateinit var adapter: MyEntryAdapter
    private lateinit var binding: ActivityAllEntriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@AllEntries, 1)
        binding.recyclerView.layoutManager = gridLayoutManager
        val builder = AlertDialog.Builder(this@AllEntries)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
        dataList = ArrayList()
        adapter = MyEntryAdapter(this@AllEntries, dataList)
        binding.recyclerView.adapter = adapter
        databaseReference =
            FirebaseDatabase.getInstance()
                .getReference("Entry Info")
        dialog.show()
        firebaseAuth = FirebaseAuth.getInstance()
        var user = firebaseAuth?.currentUser
        val desiredUserID = user?.uid

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(EntryData::class.java)
                    if (dataClass != null && dataClass.userId == desiredUserID) {
                        dataList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })
    }


}