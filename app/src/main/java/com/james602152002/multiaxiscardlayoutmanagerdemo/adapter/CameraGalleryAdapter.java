package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraGalleryAdapter extends RecyclerView.Adapter<CameraGalleryAdapter.GalleryCardViewHolder> {

    private final Cursor cursor;
    private final LayoutInflater inflater;
    private final StringBuilder ratioBuilder;

    public CameraGalleryAdapter(Context context, Cursor cursor) {
        inflater = LayoutInflater.from(context);
        this.cursor = cursor;
        ratioBuilder = new StringBuilder();
    }

    @NonNull
    @Override
    public GalleryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryCardViewHolder(inflater.inflate(R.layout.cell_gallery_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryCardViewHolder holder, int position) {
        holder.initView(position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class GalleryCardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        SimpleDraweeView photo;

        public GalleryCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initView(int position) {
            cursor.moveToPosition(position);
            final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file = new File(path);
            final Uri uri = Uri.fromFile(file);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeFile(path, options);

            final float aspect_ratio = (float) options.outHeight / options.outWidth;
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) photo.getLayoutParams();
            ratioBuilder.delete(0, ratioBuilder.length());
            lp.dimensionRatio = ratioBuilder.append(options.outWidth).append(":").append(options.outHeight).toString();
            photo.setAspectRatio(aspect_ratio);

            final StaggeredGridLayoutManager.LayoutParams managerParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
            final int width = (IPhone6ScreenResizeUtil.getCurrentScreenWidth() >> 1) - managerParams.leftMargin - managerParams.rightMargin;
            ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build();
            final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, (int) (width * aspect_ratio)))
                    .setProgressiveRenderingEnabled(true).setImageDecodeOptions(decodeOptions)
                    .build();
            final DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(photo.getController())
                    .build();
            photo.setController(controller);
        }
    }
}
