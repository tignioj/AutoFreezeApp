package com.tignioj.freezeapp.backend.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tignioj.freezeapp.backend.entitys.AppsCategory;

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


    @Query("SELECT * FROM apps_category WHERE category_name=:categoryName")
    AppsCategory getAppsCategoryByCategoryName(String categoryName);

    @Query("SELECT * FROM apps_category WHERE id=:id ORDER BY id ")
    LiveData<List<AppsCategory>> getAllAppsCategoryLiveByCategoryId(long id);

    @Query("SELECT * FROM apps_category WHERE category_name like  :pattern  ORDER BY id ")
    LiveData<List<AppsCategory>> findAppCategorysLiveWithPattern(String pattern);
}
