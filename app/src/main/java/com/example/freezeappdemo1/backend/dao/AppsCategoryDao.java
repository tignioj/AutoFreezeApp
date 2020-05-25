package com.example.freezeappdemo1.backend.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;

import java.util.List;

@Dao
public interface AppsCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAppsCategory(AppsCategory... AppsCategory);

    @Update
    void updateAppsCategory(AppsCategory... AppsCategory);

    @Delete
    void deleteAppsCategory(AppsCategory... AppsCategory);

    @Query("DELETE FROM apps_category")
    void deleteAllAppsCategory();


    @Query("SELECT * FROM apps_category ORDER BY id")
    LiveData<List<AppsCategory>> getAllAppsCategoryLive();
    @Query("SELECT * FROM apps_category ORDER BY id ")
    List<AppsCategory> getAllAppsCategory();


    @Query("SELECT * FROM apps_category WHERE category_name=:categoryName ORDER BY id ")
    LiveData<List<AppsCategory>> getAllAppsCategoryLiveByCategoryName(String categoryName);

    @Query("SELECT * FROM apps_category WHERE id=:id ORDER BY id ")
    LiveData<List<AppsCategory>> getAllAppsCategoryLiveByCategoryId(long id);

}
