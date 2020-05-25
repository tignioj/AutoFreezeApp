package com.example.freezeappdemo1.backend.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.freezeappdemo1.backend.entitys.FreezeTasker;

import java.util.List;

@Dao
public interface FreezeTaskerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(FreezeTasker... myTasks);

    @Update
    void updateTasks(FreezeTasker... myTasks);

    @Delete
    void deleteTasks(FreezeTasker... myTasks);

    @Query("DELETE FROM freeze_tasker")
    void deleteAllTasks();

    @Query("SELECT * FROM freeze_tasker ORDER BY id")
    LiveData<List<FreezeTasker>> getAllTasksLive();


    @Query("SELECT * FROM freeze_tasker WHERE category_id=:categoryId ORDER BY id ")
    LiveData<List<FreezeTasker>> getAllTasksLiveByCategoryId(long categoryId);

}
