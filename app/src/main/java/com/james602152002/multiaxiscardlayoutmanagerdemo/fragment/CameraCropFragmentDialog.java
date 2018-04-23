package com.james602152002.multiaxiscardlayoutmanagerdemo.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.interfaces.CameraCropListener;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraCropFragmentDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.crop)
    AppCompatButton crop;
    @BindView(R.id.send)
    AppCompatButton send;
    Unbinder unbinder;
    private CameraCropListener listener;
    private boolean dismiss = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialg_camera_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        initPadding();
        return view;
    }

    private void initPadding() {
        final int margin = IPhone6ScreenResizeUtil.getPxValue(30);
        ConstraintLayout.LayoutParams remarkParams = (ConstraintLayout.LayoutParams) remark.getLayoutParams();
        remarkParams.setMargins(margin, margin, margin, margin);
        IPhone6ScreenResizeUtil.set28PT_TextSize(remark);
        IPhone6ScreenResizeUtil.set24PT_TextSize(crop);
        IPhone6ScreenResizeUtil.set24PT_TextSize(send);
    }

    public void setAction(@Nullable String remark, @Nullable String crop, @Nullable String send, @NonNull CameraCropListener listener) {
        if (!TextUtils.isEmpty(remark))
            this.remark.setText(remark);
        if (!TextUtils.isEmpty(crop))
            this.crop.setText(crop);
        if (!TextUtils.isEmpty(send))
            this.send.setText(send);
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dismiss = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        listener = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null && dismiss) {
            listener.onDismiss();
        }
    }

    @OnClick({R.id.crop, R.id.send})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crop:
                if (listener != null)
                    listener.onCrop();
                dismiss = false;
                dismiss();
                break;
            case R.id.send:
                if (listener != null)
                    listener.onSend();
                dismiss = false;
                dismiss();
                break;
        }
    }
}
