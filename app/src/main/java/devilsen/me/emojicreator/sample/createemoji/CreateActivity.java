package devilsen.me.emojicreator.sample.createemoji;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import devilsen.me.emojicreator.R;
import devilsen.me.emojicreator.sample.BaseActivity;
import devilsen.me.emojicreator.util.ActivityUtils;
import devilsen.me.emojicreator.util.EmojiUtil;
import devilsen.me.emojicreator.util.ShareUti;
import devilsen.me.emojicreator.util.TintUtil;
import devilsen.me.emojicreator.util.analyze.Umeng;
import devilsen.me.emojicreator.widget.DrawEdxtView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description :  创建emoj
 * author : dongsen
 * Time : 2016-02-25
 */
public class CreateActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int DEFAULT_FONT_SIZE = 20;
    private static final int MAX_FONT_SIZE = 100;

    private boolean isEdit = true;

    private ImageView sourceImg;
    private DrawEdxtView jokeEdit;
    private Button doneBtn;
    private Button saveBtn;
    private Button reDoBtn;
    private CheckBox fontSizeCb;
    private CheckBox selectColorCb;
    private LinearLayout progressWheelLayout;

    private float txtX, txtY;

    private int imgHeight;
    private int imgWidth;
    private Bitmap sourceBitmap;
    private Bitmap tempBitmap;

    private Canvas canvas;
    private Paint textPaint;
    private Paint canvasPaint;
    private RecyclerView selectColorRv;
    private AppCompatSeekBar fontSizeSeekBar;
    private int textPaintColor = Color.BLACK;
    private SelectColorAdapter colorAdapter;

    private EmojiUtil saveUtil = EmojiUtil.getInstance();
    private LinearLayout loadingLayout;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ActivityUtils.initToolbar(this);

        sourceImg = (ImageView) findViewById(R.id.create_source_img);
        jokeEdit = (DrawEdxtView) findViewById(R.id.create_source_txt);
        doneBtn = (Button) findViewById(R.id.create_done_btn);
        saveBtn = (Button) findViewById(R.id.create_save_btn);
        reDoBtn = (Button) findViewById(R.id.create_redo_btn);
        fontSizeCb = (CheckBox) findViewById(R.id.create_paint_size_btn);
        selectColorCb = (CheckBox) findViewById(R.id.create_paint_color_btn);
        fontSizeSeekBar = (AppCompatSeekBar) findViewById(R.id.create_font_size_sb);
        selectColorRv = (RecyclerView) findViewById(R.id.create_select_color_rv);
        progressWheelLayout = (LinearLayout) findViewById(R.id.progress_wheel_layout);
        loadingLayout = (LinearLayout) findViewById(R.id.create_loading_layout);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);
        selectColorRv.setLayoutManager(layoutManager);
        colorAdapter = new SelectColorAdapter();
        selectColorRv.setAdapter(colorAdapter);


        fontSizeSeekBar.setMax(MAX_FONT_SIZE);
        fontSizeSeekBar.setProgress(DEFAULT_FONT_SIZE);


        initSource();
        initListener();
        initPaint();
        initTxtPosition();

//        ViewCompat.setTransitionName(sourceImg, IntentUtil.ELEMENT_NAME);

        TintUtil.setDrawableTint(this, fontSizeCb, R.mipmap.ic_format_shapes_black_24dp);
        TintUtil.setDrawableTint(this, selectColorCb, R.mipmap.ic_color_lens_black_24dp);
        TintUtil.setDrawableTint(this, doneBtn, R.mipmap.ic_check_black_24dp);
        TintUtil.setDrawableTint(this, saveBtn, R.mipmap.ic_folder_open_black_24dp);

    }


    /**
     * 获取图片
     */
    private void initSource() {
        name = getIntent().getStringExtra("name");
        String path = getIntent().getStringExtra("path");
        imgWidth = getIntent().getIntExtra("width", 100);
        imgHeight = getIntent().getIntExtra("height", 100);

        if (imgHeight <= 300) {
            imgWidth = imgWidth * 4;
            imgHeight = imgHeight * 4;
        } else if (imgHeight > 300 && imgHeight < 500) {
            imgWidth = imgWidth * 2;
            imgHeight = imgHeight * 2;
        }

        if (!TextUtils.isEmpty(name) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }

        jokeEdit.setBoundary(imgWidth, imgHeight);

        if (path != null) {
            // To get image using Fresco
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(path))
                    .setProgressiveRenderingEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource =
                    imagePipeline.fetchDecodedImage(imageRequest, this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    // You can use the bitmap in only limited ways
                    // No need to do any cleanup.
                    sourceImg.setImageBitmap(bitmap);
                    sourceBitmap = bitmap;
                    initCanvas();
                    loadingLayout.setVisibility(View.GONE);
                    jokeEdit.setVisibility(View.VISIBLE);
                    fontSizeCb.setEnabled(true);
                    selectColorCb.setEnabled(true);
                    doneBtn.setEnabled(true);
                    saveBtn.setEnabled(true);
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    // No cleanup required here.
                }

            }, CallerThreadExecutor.getInstance());


//            Glide.with(this)
//                    .load(path)
//                    .asBitmap()
//                    .dontAnimate()
//                    .error(R.mipmap.emoji_creator_icon_2)
//                    .fitCenter()
//                    .into(new SimpleTarget<Bitmap>(imgWidth, imgHeight) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            sourceImg.setImageBitmap(resource);
//                            sourceBitmap = resource;
//                            initCanvas();
//                            loadingLayout.setVisibility(View.GONE);
//                            jokeEdit.setVisibility(View.VISIBLE);
//                            fontSizeCb.setEnabled(true);
//                            selectColorCb.setEnabled(true);
//                            doneBtn.setEnabled(true);
//                            saveBtn.setEnabled(true);
//                        }
//                    });
        } else {
            Toast.makeText(this, "载入图片错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListener() {
        doneBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        fontSizeCb.setOnCheckedChangeListener(this);
        selectColorCb.setOnCheckedChangeListener(this);
        reDoBtn.setOnClickListener(this);
        fontSizeSeekBar.setOnSeekBarChangeListener(new FontSizeListener(new FontSizeListener.FontSizeChangeListener() {
            @Override
            public void getSizeProgress(int progress) {
                jokeEdit.setTextSize(progress);
                textPaint.setTextSize(jokeEdit.getTextSize());

                if (!isEdit) {
                    drawTxt();
                }

            }
        }));
        colorAdapter.setColorListener(new SelectColorAdapter.SelectColorListener() {
            @Override
            public void getSelectColor(int colorId) {
                textPaintColor = getResources().getColor(colorId);
                jokeEdit.setTextColor(textPaintColor);
                textPaint.setColor(textPaintColor);

                if (!isEdit) {
                    drawTxt();
                }
            }
        });

        jokeEdit.setTxtLocationListener(new DrawEdxtView.TxtLocationListener() {
            @Override
            public void getTxtLocation(float txtPX, float txtPY) {
                txtX = txtPX;
                txtY = txtPY;
            }
        });
    }


    /**
     * author : dongSen
     * date : 16/5/31 下午3:53
     * desc : 初始化画布
     */
    private void initCanvas() {
        if (tempBitmap == null)
            tempBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.RGB_565);
        if (canvas == null)
            canvas = new Canvas(tempBitmap);

        resetCanvas();
    }

    /**
     * author : dongSen
     * date : 16/5/31 下午3:43
     * desc : 重置画布
     */
    private void resetCanvas() {
        canvas.drawBitmap(sourceBitmap, 0, 0, canvasPaint);
    }

    /**
     * author : dongSen
     * date : 16/5/31 下午3:57
     * desc : 初始化画笔
     */
    private void initPaint() {
        canvasPaint = new Paint();
        canvasPaint.setFilterBitmap(true);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(false);
        textPaint.setTextSize(jokeEdit.getTextSize());
    }

    private void initTxtPosition() {
        //初始化文字位置
        txtX = (imgWidth + jokeEdit.getMeasuredWidth() - jokeEdit.getPaddingLeft() * 2) / 2;
        txtY = (imgHeight + jokeEdit.getLineHeight()) / 2;
    }

    private void drawTxt() {
        resetCanvas();

        String jokeText = jokeEdit.getText().toString();
        int lineHeight = jokeEdit.getLineHeight();

        if (TextUtils.isEmpty(jokeText)) {
//            jokeText = "(>_<)";
            jokeText = " ";
        }

        String[] jokeString = jokeText.split("\\n");
        for (int i = 0; i < jokeString.length; i++) {
            canvas.drawText(jokeString[i], txtX, txtY + lineHeight * i, textPaint);
        }

        sourceImg.setImageBitmap(tempBitmap);
    }


    private void changeEditState() {
        if (isEdit) {
            doneBtn.setText(getString(R.string.rebuild));
            jokeEdit.setVisibility(View.GONE);
            reDoBtn.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.GONE);
        } else {
            doneBtn.setText(getString(R.string.done));
            jokeEdit.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.VISIBLE);
            reDoBtn.setVisibility(View.GONE);
        }
        isEdit = !isEdit;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_done_btn://完成
                Umeng.clickEvent(this, Umeng.EventId.DOWN);
                changeEditState();
                drawTxt();
                break;
            case R.id.create_redo_btn://重做
                Umeng.clickEvent(this, Umeng.EventId.REDO);
                changeEditState();
                resetCanvas();
                break;
            case R.id.create_save_btn://保存文件
                Umeng.clickEvent(this, Umeng.EventId.SAVE);
                if (isEdit) {
                    changeEditState();
                    drawTxt();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    requestPermission();
                } else {
                    saveImg(false);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.create_paint_size_btn) {
            if (isChecked) {
                fontSizeSeekBar.setVisibility(View.VISIBLE);
                selectColorCb.setChecked(false);
            } else {
                fontSizeSeekBar.setVisibility(View.GONE);
            }
        }

        if (id == R.id.create_paint_color_btn) {
            if (isChecked) {
                selectColorRv.setVisibility(View.VISIBLE);
                fontSizeCb.setChecked(false);
            } else {
                selectColorRv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * author : dongSen
     * date : 16/5/30 下午7:13
     * desc :  保存文件
     */
    private void saveImg(final boolean share) {
        Observable.just(saveUtil.saveEmoji(getApplicationContext(), name, tempBitmap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onStart() {
                        saveEmojiView(true);
                    }

                    @Override
                    public void onCompleted() {
                        if (share) {
                            ShareUti.shareImg(CreateActivity.this, saveUtil.getFilePath(), name);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        saveEmojiView(false);

                        Toast.makeText(CreateActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        saveEmojiView(false);

                        Toast.makeText(CreateActivity.this, "成功保存至createEmoji", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void saveEmojiView(boolean isSaving) {
        if (isSaving) {
            saveBtn.setVisibility(View.GONE);
            progressWheelLayout.setVisibility(View.VISIBLE);
        } else {
            progressWheelLayout.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Umeng.clickEvent(this, Umeng.EventId.SHARE);

        if (item.getItemId() == R.id.menu_create_share && fontSizeCb.isEnabled()) {
            if (isEdit) {
                changeEditState();
                drawTxt();
            }

            if (saveUtil.getFilePath() != null) {
                ShareUti.shareImg(this, saveUtil.getFilePath(), name);
            } else {
                saveImg(true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_PERMISSION = 1;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            int grant = grantResults[0];
            boolean granted = grant == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                saveImg(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUtil.resetFile();
        tempBitmap = null;
        sourceBitmap = null;
        canvas = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Umeng.activityResume(this, "create_activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Umeng.activityPause(this, "create_activity");
    }
}
