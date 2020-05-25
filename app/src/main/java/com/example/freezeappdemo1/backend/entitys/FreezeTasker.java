package com.example.freezeappdemo1.backend.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "freeze_tasker")
public class FreezeTasker {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date startTime;
    private Date endTime;

    private long categoryId;

    @Override
    public String toString() {
        return "FreezeTasker{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", categoryId=" + categoryId +
                '}';
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
