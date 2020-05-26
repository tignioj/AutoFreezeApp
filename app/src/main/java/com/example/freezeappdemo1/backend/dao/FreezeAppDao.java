package com.example.freezeappdemo1.backend.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;
import com.example.freezeappdemo1.entity.AppInfo;

import java.util.List;

@Dao
public interface FreezeAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertApps(FreezeApp... freezeApps);

    @Update
    void updateApps(FreezeApp... freezeApps);

    @Delete
    void deleteApps(FreezeApp... freezeApps);

    @Query("DELETE FROM freeze_app")
    void deleteAllFreezeApps();

    @Query("SELECT * FROM freeze_app ORDER BY id")
    LiveData<List<FreezeApp>> getAllAppsLive();


    @Query("SELECT * FROM freeze_app WHERE category_id=:categoryId ORDER BY id ")
    List<FreezeApp> getAllAppsByCategoryId(long categoryId);

    @Query("SELECT * FROM freeze_app WHERE category_id=:categoryId ORDER BY id ")
    LiveData<List<FreezeApp>> getAllAppsLiveByCategoryId(long categoryId);

    @Query("SELECT * FROM freeze_app WHERE category_id <>:categoryId ORDER BY id ")
    LiveData<List<FreezeApp>> getAllAppsLiveNotInCategory(long categoryId);

    @Query("SELECT * FROM freeze_app WHERE isFrozen=1 ORDER BY id ")
    List<FreezeApp> getAllFrozenApps();

    @Query("SELECT * FROM freeze_app WHERE appName LIKE :pattern OR package_name LIKE :pattern AND isFrozen=1 AND category_id=:categoryId  ORDER BY id ")
    LiveData<List<FreezeApp>> getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(long categoryId, String pattern);
}
