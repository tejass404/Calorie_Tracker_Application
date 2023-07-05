package com.example.myapplication02

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)


    @Query("SELECT * FROM user")
    fun getAll(): MutableList<User>

    @Query("SELECT user_weight FROM User WHERE user_name = :name")
    suspend fun getUserWeightByName(name: String): Int?

    @Query("SELECT user_bmr FROM User WHERE user_name = :name")
    fun getBMRByName(name: String): Int?

    @Delete
    fun deleteUser(vararg users:User)

}

@Dao
interface ActivityDao {

    @Insert
    fun insertactivityData(vararg activityData: ActivityData)

    @Query("SELECT name, activity_date AS date, SUM(activity_calorieOut) AS totalCalorieOut, 0 AS totalCalorieIn ,activity_bmr AS bmr FROM ActivityData WHERE name=:name  GROUP BY activity_date")
    fun getCalorieOutActivityData(name:String): MutableList<SummarizedViewData>

    @Query("DELETE FROM ActivityData WHERE name = :name")
    fun deleteByName(name:String)
    @Query("SELECT activity FROM ActivityData WHERE name= :name AND activity_date= :date")
    fun getActivityNameByActivity(name: String?, date: String?): String?
    @Query("SELECT * FROM ActivityData WHERE name= :name AND activity_date= :date")
    fun getAll(name: String?, date: String?): MutableList<ActivityData>
}
@Dao
interface FoodDao {

    @Insert
    fun insertFoodData(vararg foodData: FoodData)
    @Query("SELECT * FROM FoodData WHERE name= :name AND food_date= :date")
    fun getAll(name: String?, date: String?): MutableList<FoodData>

    @Query("SELECT name, food_date AS date, SUM(food_calorieIn) AS totalCalorieIn, 0 AS totalCalorieOut,food_bmr AS bmr FROM FoodData WHERE name=:name  GROUP BY food_date")
    fun getCalorieInActivityData(name:String): MutableList<SummarizedViewData>

    @Query("DELETE FROM foodData WHERE name = :name")
    fun deleteByName(name:String)
    @Query("SELECT food_group FROM FoodData WHERE name= :name AND food_date= :date")
    fun getGroupNameByFood(name: String?, date: String?): String?

}
