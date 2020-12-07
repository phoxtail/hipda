package net.jejer.hipda.bean;

import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by GreenSkinMonster on 2016-07-21.
 */
public class Forum {
    private final String mName;
    private final int mId;
    private final IIcon mIcon;

    public Forum(int id, String name, IIcon icon) {
        mIcon = icon;
        mId = id;
        mName = name;
    }

    public IIcon getIcon() {
        return mIcon;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}
