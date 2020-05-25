package com.example.freezeappdemo1.backend.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "freeze_tasker",
        foreignKeys = @ForeignKey(entity = AppsCategory.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ))
public class FreezeTasker {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date startTime;
    private Date endTime;

    @ColumnInfo(name = "category_id")
    private long categoryId;

    private String categoryName;

    @Ignore
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public String toString() {
        return "FreezeTasker{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
