package devilsen.me.emojicreator.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import devilsen.me.emojicreator.R;


/**
 * author : dongSen
 * date : 2016-06-02 11:19
 * desc : 着色器util(兼容低版本)
 */
public class TintUtil {

    public static void setDrawableTint(Context context, TextView view, int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        int[] colors = new int[]{ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.unenable)};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{};

        ColorStateList colorList = new ColorStateList(states, colors);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], drawable);
        stateListDrawable.addState(states[1], drawable);
        stateListDrawable.addState(states[2], drawable);
        stateListDrawable.addState(states[3], drawable);

        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable, colorList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
        }
    }


}
