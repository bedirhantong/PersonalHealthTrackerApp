package com.example.personalhealthtracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
private val dbHealthyActivities = FirebaseDatabase.getInstance().getReference(
    NODE_HEALTHY_ACTIVITIES)
class HealthyActivitiesViewModel : ViewModel() {

    private val _result = MutableLiveData<Exception?>()
    // to access _result to make liveData
    val result :LiveData<Exception?>
        get() = _result

    fun addActivity(healthyActivity: HealthyActivity){
        // Create a unique key to set the id of the activity
        healthyActivity.id = dbHealthyActivities.push().key

        // to save the Activity inside of DB just call set func
        dbHealthyActivities.child(healthyActivity.id!!).setValue(healthyActivity)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }

        println("healthyActivity.id : ${healthyActivity.id}")
        println("healthyActivity.activityName : ${healthyActivity.activityName}")
        println("healthyActivity.energyCons : ${healthyActivity.energyCons}")
        println("healthyActivity.roadTravelled : ${healthyActivity.roadTravelled}")
        println("healthyActivity.timeElapsed : ${healthyActivity.timeElapsed}")

    }


}