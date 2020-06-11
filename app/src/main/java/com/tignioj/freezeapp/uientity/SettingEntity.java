package com.tignioj.freezeapp.uientity;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SettingEntity {


    private Drawable drawable;
    private String text;
    private String description;
    private int action;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SettingEntity(Drawable drawable, String text, int action, String description) {
        this.drawable = drawable;
        this.text = text;
        this.action = action;
        this.description = description;
    }

    public SettingEntity(Drawable drawable, String text, int action) {
        this.text = text;
        this.drawable = drawable;
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
