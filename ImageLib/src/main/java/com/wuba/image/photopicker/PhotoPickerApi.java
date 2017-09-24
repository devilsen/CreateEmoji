package com.wuba.image.photopicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.wuba.image.photopicker.activity.BGAPhotoPickerActivity;
import com.wuba.image.photopicker.activity.BGAPhotoPickerPreviewActivity;
import com.wuba.image.photopicker.activity.BGAPhotoPreviewActivity;

import java.io.File;
import java.util.ArrayList;

import static com.wuba.image.photopicker.activity.PhotoPickerConstant.*;

/**
 * author : dongSen
 * date : 2017/9/7
 * desc : 调用图片选择api
 */
public class PhotoPickerApi {

    private static File photoDir;

    private static File saveDir;

    public static void init(@NonNull String photoDirPath, @NonNull String saveDirPath) {
        if (photoDir == null)
            photoDir = new File(photoDirPath);

        if (saveDir == null)
            saveDir = new File(saveDirPath);
    }

    //-------------------------------------选取图片---------------------

    /**
     * 此方法只能生成Intent调用图片选择界面，在onActivityResult()方法中获取拿到的图片，图片的格式是 ArrayList<String> selectedImages
     *
     * @param context        应用程序上下文
     * @param maxChooseCount 图片选择张数的最大值  默认1
     * @param selectedImages 当前已选中的图片路径集合，可以传null
     * @param camera         是否有拍照item
     * @param cropMode       是否是裁剪模式
     * @return intent
     */
    public static Intent pickerIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages, boolean camera, boolean cropMode) {
        Intent intent = new Intent(context, BGAPhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, camera ? photoDir : null);//拍照后图片保存的目录。如果传null表示没有拍照功能，如果不为null则具有拍照功能，
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putExtra(EXTRA_CROP_MODE, cropMode);
        return intent;
    }

    /**
     * 简单的选取一张图片
     */
    public static Intent pickerIntent(Context context) {
        return pickerIntent(context, 1, null, false, false);
    }

    /**
     * 简单的选取一张图片,可拍照
     */
    public static Intent pickerIntent(Context context, boolean camera) {
        return pickerIntent(context, 1, null, camera, false);
    }

    /**
     * 选取图片，有最大控制
     */
    public static Intent pickerIntent(Context context, int maxChooseCount) {
        return pickerIntent(context, maxChooseCount, null, false, false);
    }

    /**
     * 用于用户选取头像
     */
    public static Intent pickerIntent(Context context, boolean camera, boolean cropMode) {
        return pickerIntent(context, 1, null, camera, cropMode);
    }

    /**
     * 选取图片，有最大控制,可拍照
     */
    public static Intent pickerIntent(Context context, int maxChooseCount, boolean camera) {
        return pickerIntent(context, maxChooseCount, null, camera, false);
    }

    /**
     * 选取图片，有最大控制，传入已经选择的图片
     */
    public static Intent pickerIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages) {
        return pickerIntent(context, maxChooseCount, selectedImages, false, false);
    }


    //-------------------------------------预览图片---------------------

    /**
     * 获取查看多张图片的intent
     *
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @return intent
     */
    public static Intent previewIntent(Context context, ArrayList<String> previewImages, int currentPosition) {
        Intent intent = new Intent(context, BGAPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveDir);//保存图片的目录，如果传null，则没有保存图片功能
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        return intent;
    }

    public static Intent previewIntent(Context context, ArrayList<String> previewImages) {
        return previewIntent(context, previewImages, 0);
    }

    /**
     * 获取查看单张图片的intent
     *
     * @param photoPath 图片路径
     * @param isSelf    是否是自己（展示不同的菜单，自己可以更换头像）
     * @return intent
     */
    public static Intent previewIntent(Context context, String photoPath, boolean isSelf) {
        Intent intent = new Intent(context, BGAPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveDir);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        intent.putExtra(EXTRA_IS_SELF, isSelf);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        return intent;
    }

    /**
     * 获取查看单张图片的intent
     *
     * @param photoPath 图片路径
     * @return intent
     */
    public static Intent previewIntent(Context context, String photoPath) {
        return previewIntent(context, photoPath, false);
    }

    //-------------------------------------预览并选择图片（选择时内部使用）---------------------

    /**
     * @param context         应用程序上下文
     * @param maxChooseCount  图片选择张数的最大值
     * @param selectedImages  当前已选中的图片路径集合，可以传null
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @param isFromTakePhoto 是否是拍完照后跳转过来
     * @return intent
     */
    public static Intent pickPreviewIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages, ArrayList<String> previewImages, int currentPosition, boolean isFromTakePhoto) {
        Intent intent = new Intent(context, BGAPhotoPickerPreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, isFromTakePhoto);
        return intent;
    }

    /**
     * 预览一张图片
     *
     * @param context context
     * @param path    图片路径
     * @return intent
     */
    public static Intent pickPreviewIntent(Context context, String path) {
        Intent intent = new Intent(context, BGAPhotoPickerPreviewActivity.class);
        ArrayList<String> list = new ArrayList<>();
        list.add(path);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, list);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, list);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        intent.putExtra(EXTRA_CURRENT_POSITION, 1);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        return intent;
    }

}
