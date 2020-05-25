package com.example.freezeappdemo1.backend.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.freezeappdemo1.entity.AppInfo;

@Entity(
        tableName = "freeze_app",
        indices = {@Index("id"), @Index(value = "package_name", unique = true)},
        foreignKeys = @ForeignKey(entity = AppsCategory.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class FreezeApp {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "category_id")
    private long categoryId;

    private boolean isFrozen;

    @ColumnInfo(name = "package_name")
    private String packageName;

    private String appName;

    private int icon;

    public FreezeApp() {
    }


    @Ignore
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "FreezeApp{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", isFrozen=" + isFrozen +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", icon=" + icon +
                ", isSelect=" + isSelect +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
