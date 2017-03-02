package devilsen.me.emojicreator.sample.createemoji;

import android.widget.SeekBar;

/**
 * Description : 字体大小seekBar控制
 * author : dongsen
 * Time : 2016-02-27
 */
public class FontSizeListener implements SeekBar.OnSeekBarChangeListener {

    private FontSizeChangeListener changeListener;

    public FontSizeListener(FontSizeChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (changeListener != null)
            changeListener.getSizeProgress(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface FontSizeChangeListener{
        void getSizeProgress(int progress);
    }
}
