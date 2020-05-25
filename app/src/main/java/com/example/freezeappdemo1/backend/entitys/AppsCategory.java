package com.example.freezeappdemo1.backend.entitys;

import android.os.Build;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "apps_category", indices = @Index(value = "category_name", unique = true))
public class AppsCategory {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "category_name")
    private String categoryName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppsCategory that = (AppsCategory) o;
        return id == that.id &&
                categoryName.equals(that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryName);
    }

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
