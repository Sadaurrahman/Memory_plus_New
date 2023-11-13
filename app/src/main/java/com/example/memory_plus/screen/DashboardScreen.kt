package com.example.memory_plus.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.memory_plus.MemoryData
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun DashboardScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var newItem by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<MemoryData>()) }
    val database = FirebaseDatabase.getInstance().reference.child("MemoryData")

    // Read data from Firebase Realtime Database
    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map {
                    Log.d("ddd", it.toString())
                    it.getValue(MemoryData::class.java)
                }
                items = data

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }

        }
        database.addValueEventListener(listener)
        onDispose {
            database.removeEventListener(listener)
        }
    }

    Scaffold(
        // in scaffold we are specifying top bar.
        topBar = {
            // inside top bar we are specifying background color.
            TopAppBar(backgroundColor = MaterialTheme.colors.primary,
                // along with that we are specifying title for our top bar.
                title = {
                    // in the top bar we are specifying tile as a text
                    Text(
                        // on below line we are specifying
                        // text to display in top app bar.
                        text = "My Memories",

                        // on below line we are specifying
                        // modifier to fill max width.
                        modifier = Modifier.fillMaxWidth(),

                        // on below line we are
                        // specifying text alignment.
                        textAlign = TextAlign.Center,

                        // on below line we are
                        // specifying color for our text.
                        color = Color.White
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        LazyColumn {
            items(items.size) { item ->
                DataCard(dataItem = items[item])
            }

        }

    }

}

@Composable
fun DataCard(dataItem: MemoryData) {
    Card(
        backgroundColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(dataItem.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = dataItem.title,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = dataItem.description,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date and Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = dataItem.dateTime)

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = dataItem.location)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
        }


    }
}


//fun userLogin( navController: NavHostController, username: String, password: String) {
////    val firebaseDatabase = FirebaseApp.getInstance();
////    val databaseReference = firebaseDatabase.applicationContext.getDatabasePath("EmployeeInfo");
//
////    val empObj =
////        User(username, password)
//    // we are use add value event listener method
//    // which is called with database reference.
////    databaseReference.addValueEventListener(object : ValueEventListener {
////        override fun onDataChange(snapshot: DataSnapshot) {
////            // inside the method of on Data change we are setting
////            // our object class to our database reference.
////            // data base reference will sends data to firebase.
////            databaseReference.setValue(empObj)
////            // after adding this data we
////            // are showing toast message.
//////            Toast.makeText(
//////                context,
//////                "Data added to Firebase Database",
//////                Toast.LENGTH_SHORT
//////            ).show()
////        }
////
////        override fun onCancelled(error: DatabaseError) {
////            // if the data is not added or it is cancelled then
////            // we are displaying a failure toast message.
//////            Toast.makeText(
//////                context,
//////                "Fail to add data $error",
//////                Toast.LENGTH_SHORT
//////            ).show()
////        }
////    })
//     val query: Query = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("email")
//            .equalTo(username)
//
//    query.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            for (childSnapshot in dataSnapshot.children) {
//                val signUpModel: String = childSnapshot.child("password").getValue(String::class.java)!!
////                if (signUpModel == password) {
////                    Log.d("dfff","success")
////                    navController.navigate("Signup")
////                    // Here is your desired location
//////                        AppData.userId = childSnapshot.getKey()
//////                        AppData.userType = "user"
////                    //                                AppData.phoneNo = childSnapshot.child("phoneNumber").getValue(String.class);
//////                    progressDialog!!.dismiss()
//////                    Toast.makeText(application, "Success", Toast.LENGTH_SHORT).show()
//////                    startActivity(Intent(this@com.example.memory_plus.MainActivity, MainDashboard::class.java))
//////                    finish()
////                } else {
////                    Log.d("dfff","failed")
//
////                    progressDialog!!.dismiss()
////                    Toast.makeText(this@com.example.memory_plus.MainActivity, "failed", Toast.LENGTH_SHORT).show()
////                }
//            }
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            Log.d("dfff","success$databaseError")
//
//        }
//    })
//}
