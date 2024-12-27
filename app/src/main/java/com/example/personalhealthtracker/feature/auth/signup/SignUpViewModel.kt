package com.example.personalhealthtracker.feature.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SignUpViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    private val _dateOfBirth = MutableLiveData<String>()
    val dateOfBirth: LiveData<String> = _dateOfBirth

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> = _gender

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun setDateOfBirth(dateOfBirth: String) {
        _dateOfBirth.value = dateOfBirth
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun signUp() {
        val emailValue = email.value
        val passwordValue = password.value
        val usernameValue = username.value
        val fullNameValue = fullName.value
        val dateOfBirthValue = dateOfBirth.value
        val genderValue = gender.value

        val missingFields = mutableListOf<String>()
        if (emailValue.isNullOrEmpty()) missingFields.add("email")
        if (passwordValue.isNullOrEmpty()) missingFields.add("password")
        if (usernameValue.isNullOrEmpty()) missingFields.add("username")
        if (fullNameValue.isNullOrEmpty()) missingFields.add("fullName")
        if (dateOfBirthValue.isNullOrEmpty()) missingFields.add("dateOfBirth")
        if (genderValue.isNullOrEmpty()) missingFields.add("gender")

        if (missingFields.isNotEmpty()) {
            _signUpResult.postValue(SignUpResult.Error("All fields are required: Missing ${missingFields.joinToString(", ")}"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email.value!!, password.value!!).await()
                Timber.tag("SignUpViewModel").d("User created: ${authResult.user?.uid}")
                authResult.user?.let { firebaseUser ->
                    val userDocument = hashMapOf(
                        "userId" to firebaseUser.uid,
                        "username" to username.value,
                        "email" to email.value,
                        "password" to password.value,
                        "fullName" to fullName.value,
                        "dateOfBirth" to dateOfBirth.value,
                        "gender" to gender.value,
                        "isAccountActive" to true,
                        "isEmailVerified" to false,
                        "description" to "",
                        "profileImageUrl" to "",
                        "coverImageUrl" to "",
                        "activityCount" to 0,
                        "followerCount" to 0,
                        "followingCount" to 0,
                        "createdAt" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    )
                    db.collection("users").document(firebaseUser.uid).set(userDocument).await()
                    _signUpResult.postValue(SignUpResult.Success)
                } ?: throw Exception("User creation failed")
            } catch (e: Exception) {
                _signUpResult.postValue(SignUpResult.Error(e.message ?: "An unknown error occurred"))
            }
        }
    }
}

sealed class SignUpResult {
    object Success : SignUpResult()
    data class Error(val message: String) : SignUpResult()
}

