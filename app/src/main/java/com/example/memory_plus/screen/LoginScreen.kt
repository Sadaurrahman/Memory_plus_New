package com.example.memory_plus.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.memory_plus.UsersData
import com.example.memory_plus.ui.theme.Purple700
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun LoginScreen(navController: NavHostController) {
  var showDialog by remember { mutableStateOf(false) }
    var newItem by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<UsersData>()) }
    val database = FirebaseDatabase.getInstance().reference.child("Users")

    // Read data from Firebase Realtime Database


    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {

                navController.navigate("Signup")
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple700
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        LazyColumn {
//            items(items.size) { item ->
//                Text(text = "ID: $items", modifier = Modifier.padding(16.dp))
//            }
//        }
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }

        Text(text = "Login", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if(username.value.text.isNotEmpty() && password.value.text.isNotEmpty()){
                    showDialog=true
                        signIn(username.value.text,password.value.text, onResult = {dialog,success ->
                        showDialog=dialog
                        if(success){
                            navController.navigate("dashboard")
                        }else{

                        }
                    })
                 }
//                    userLogin(navController,username.value.text,password.value.text)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment= Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )
    }
}
fun signIn(email: String, password: String, onResult: (dialog:Boolean,success:Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Authentication successful
                loginEmailPassword(
                    username = email,
                    password = password,
                    onResult = { dialog, success ->
                        onResult(dialog,success)
                    })
            }else{
                onResult(false,false)
            }
        }
}
 fun loginEmailPassword(username: String, password: String, onResult: (dialog:Boolean,success:Boolean) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(username)
    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.children.map {
                if (it.getValue(UsersData::class.java)!!.password.equals(password)) {
                    Log.d("ddd", it.getValue(UsersData::class.java)!!.email)
                    onResult(false,true)
                } else {
                    onResult(false,false)
                    Log.d("ddd", "failed")
                }
            }

        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}
@Composable
fun Loader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x77000000)) // semi-transparent background
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
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
