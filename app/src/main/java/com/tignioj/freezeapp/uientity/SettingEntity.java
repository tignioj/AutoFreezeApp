package com.tignioj.freezeapp.uientity;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SettingEntity {


    private Drawable drawable;
    private String text;
    private int action;

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
