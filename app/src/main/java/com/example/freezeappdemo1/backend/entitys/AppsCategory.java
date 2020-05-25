package com.example.freezeappdemo1.backend.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "apps_category", indices = @Index(value = "category_name", unique = true))
public class AppsCategory {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "category_name")
    private String categoryName;



    @Override
    public String toString() {
        return categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
