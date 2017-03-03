package devilsen.me.emojicreator.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import devilsen.me.emojicreator.util.KeyboardUtil;

/**
 * Description :
 * author : dongsen
 * Time : 2016-02-25
 */
public class DrawEdxtView extends AppCompatEditText implements OnTouchListener {

    private Context mContext;

    private int imgWidth, imgHeight;
    private TxtLocationListener txtLocationListener;

    private int lastX, lastY;

    private int downX, downY; // 按下View的X，Y坐标
    private int upX, upY; // 放手View的X,Y坐标
    private int rangeDifferenceX, rangeDifferenceY; // 放手和按下X,Y值差
    private static final int mDistance = 10; // 设定点击事件的移动距离值
    private int mL, mB, mR, mT;//重绘时layout的值

    public DrawEdxtView(Context context) {
        super(context);
        mContext = context;
        setOnTouchListener(this);
    }

    public DrawEdxtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnTouchListener(this);
    }

    public DrawEdxtView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOnTouchListener(this);
    }

    /**
     * 获取当前view的位置
     */
    public void getTxtLocation() {
        if (txtLocationListener != null)
            txtLocationListener.getTxtLocation(mL, mB - getLineHeight() / 5);
    }

    public void setTxtLocationListener(TxtLocationListener listener) {
        this.txtLocationListener = listener;
    }

    public void setBoundary(int imgWidth, int imgHeight) {
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public interface TxtLocationListener {
        void getTxtLocation(float txtX, float txtY);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = (int) event.getRawX();
                lastY = downY = (int) event.getRawY();

                v.setFocusable(false);
                v.setFocusableInTouchMode(false);
                KeyboardUtil.hideSoftInput(mContext, v);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                mL = v.getLeft() + dx;
                mB = v.getBottom() + dy;
                mR = v.getRight() + dx;
                mT = v.getTop() + dy;

                if (mL < 0) {
                    mL = 0;
                    mR = mL + v.getWidth();
                }

                if (mT < 0) {
                    mT = 0;
                    mB = mT + v.getHeight();
                }

                if (mR > imgWidth) {
                    mR = imgWidth;
                    mL = mR - v.getWidth();
                }

                if (mB > imgHeight) {
                    mB = imgHeight;
                    mT = mB - v.getHeight();
                }
                v.layout(mL, mT, mR, mB);

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getRawX();
                upY = (int) event.getRawY();

                rangeDifferenceX = upX - downX;
                rangeDifferenceY = upY - downY;
                if (rangeDifferenceX > 0 && rangeDifferenceX <= mDistance) {
                    if (rangeDifferenceY >= 0 && rangeDifferenceY <= mDistance) {
                        v.setFocusable(true);
                        v.setFocusableInTouchMode(true);
                    } else {
                        if (rangeDifferenceY <= 0 && rangeDifferenceY >= -mDistance) {
                            v.setFocusable(true);
                            v.setFocusableInTouchMode(true);
                        } else {
                            v.setFocusable(false);
                            v.setFocusableInTouchMode(false);
                            getTxtLocation();
                        }
                    }
                } else {
                    if (rangeDifferenceX <= 0 && rangeDifferenceX >= -mDistance) {
                        v.setFocusable(true);
                        v.setFocusableInTouchMode(true);
                    } else {
                        v.setFocusable(false);
                        v.setFocusableInTouchMode(false);
                        getTxtLocation();
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

}
