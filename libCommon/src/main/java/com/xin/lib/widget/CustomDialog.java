package com.xin.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.xin.lib.mvp.R;
import com.xin.lib.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 默认对话框，包括标题、进度条、提示文本、输入框、确认和取消按钮
 * <br>
 * 通常情况可考虑使用
 * <p>
 *
 * @author zouxinjie on 2019/3/30.
 */
public class CustomDialog extends Dialog {
    private View mLayoutContainer;
    private TextView mContent;
    private TextView mTitle;
    private TextView mConfirm;
    private TextView mCancel;

    private View mProgressBar;
    private ViewGroup mLayoutBtns;
    private EditText mEtInput;
    private Context mContext;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.widget_alert_dialog);
        init(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mLayoutContainer = LayoutInflater.from(context).inflate(R.layout.lib_common_base_dialog, null);
        mContent = mLayoutContainer.findViewById(R.id.tv_content);
        mTitle = mLayoutContainer.findViewById(R.id.tv_title);
        mCancel = mLayoutContainer.findViewById(R.id.btn_cancel);
        mConfirm = mLayoutContainer.findViewById(R.id.btn_confirm);
        mProgressBar = mLayoutContainer.findViewById(R.id.progressbar);
        mLayoutBtns = mLayoutContainer.findViewById(R.id.layout_buttons);
        mEtInput = mLayoutContainer.findViewById(R.id.et_input);
        if (getWindow() != null) {
            getWindow().setWindowAnimations(R.style.widget_alert_dialog_anim);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = (int) (ScreenUtils.getScreenPixelsWidth(getContext()) * 0.8);
        setContentView(mLayoutContainer, new ViewGroup.LayoutParams(width, WRAP_CONTENT));
    }

    /**
     * 隐藏标题
     */
    public CustomDialog hideTitle() {
        mTitle.setVisibility(View.GONE);
        return this;
    }

    /**
     * @param title empty时使用默认标题“提示”，考虑到标题一般情况
     *              下都要显示，如果要隐藏使用{@link #hideTitle}
     */
    public CustomDialog showTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(title);
        }
        return this;
    }

    public CustomDialog showTitle(int title) {
        return showTitle(mContext.getString(title));
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        showTitle(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        showTitle(mContext.getString(titleId));
    }

    /**
     * @param content 当为empty时隐藏内容，否则显示 类似于@setMessage
     */
    public CustomDialog setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(content);
        } else {
            mContent.setVisibility(View.GONE);
        }
        return this;
    }

    public CustomDialog setContent(int content) {
        return setContent(mContext.getString(content));
    }

    public CustomDialog setShowProgressbar(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * @param listener 为null时不显示确认按钮，使用默认按钮文字“确认”
     */
    public CustomDialog setConfirmButton(final OnClickListener listener) {
        return setConfirmButton(null, listener);
    }

    /**
     * @param listener 为null时不显示确认按钮
     * @param text     为null时使用默认按钮文字
     */
    public CustomDialog setConfirmButton(String text, final OnClickListener listener) {
        configureButton(text, listener, mConfirm);
        return this;
    }

    /**
     * @param listener 为null时不显示取消按钮，使用默认按钮文字“取消”
     */
    public CustomDialog setCancelButton(final OnClickListener listener) {
        return setCancelButton(null, listener);
    }

    /**
     * @param listener 为null时不显示取消按钮
     * @param text     为null时使用默认按钮文字
     */
    public CustomDialog setCancelButton(String text, final OnClickListener listener) {
        configureButton(text, listener, mCancel);
        return this;
    }

    public void hideOrShowButtons(boolean show) {
        if (show) {
            if (isAnyVisible(mCancel, mConfirm)) {
                mLayoutBtns.setVisibility(View.VISIBLE);
            }
        } else {
            if (isAllGone(mCancel, mConfirm)) {
                mLayoutBtns.setVisibility(View.GONE);
            }
        }
    }

    private void configureButton(String text, final OnClickListener listener, TextView configureTextView) {
        if (listener == null) {
            hideOrShowButtons(false);
            return;
        }
        configureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listener.onClick(CustomDialog.this)) {
                    dismiss();
                }
            }
        });
        configureTextView.setVisibility(View.VISIBLE);
        hideOrShowButtons(true);
        if (!TextUtils.isEmpty(text)) {
            configureTextView.setText(text);
        }
        adjustBtnLayout();
    }

    private boolean isAnyVisible(View... views) {
        for (View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllGone(View... views) {
        for (View view : views) {
            if (view.getVisibility() != View.GONE) {
                return false;
            }
        }
        return true;
    }

    private void adjustBtnLayout() {
        boolean isFirst = true;
        for (int i = 0; i < mLayoutBtns.getChildCount(); i++) {
            View v = mLayoutBtns.getChildAt(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            lp.leftMargin = isFirst ? 0 : ScreenUtils.dp2px(getContext(), 10);
            v.setLayoutParams(lp);
            isFirst = isFirst && v.getVisibility() == View.GONE;
        }
    }

    /**
     * @see #setCancelable(boolean)
     */
    public CustomDialog setIsCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * @see #setCanceledOnTouchOutside(boolean)
     */
    public CustomDialog setIsCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    public CustomDialog setEnableInput(boolean enableInput) {
        if (enableInput) {
            mEtInput.setVisibility(View.VISIBLE);
        } else {
            mEtInput.setVisibility(View.GONE);
        }
        return this;
    }

    public CustomDialog setInputText(String hint, String text) {
        mEtInput.setText(text);
        mEtInput.setHint(hint == null ? "" : hint);
        return this;
    }

    public EditText getEditText() {
        return mEtInput;
    }

    public String getEditTextStr() {
        return mEtInput.getText() == null ? "" : mEtInput.getText().toString();
    }

    public CustomDialog showDialog() {
        if (mContext != null) {
            show();
        }
        return this;
    }

    public interface OnClickListener {
        /**
         * @return true 不自动隐藏对话框，false 则自动隐藏
         */
        boolean onClick(CustomDialog dialog);
    }

    /**
     * Dialog 监听back键事件
     */
    private DialogOnKeyDownListener dialogOnKeyDownListener;

    public void setDialogOnKeyDownListener(DialogOnKeyDownListener dialogOnKeyDownListener) {
        this.dialogOnKeyDownListener = dialogOnKeyDownListener;
    }

    public interface DialogOnKeyDownListener {
        void onKeyDownListener(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (dialogOnKeyDownListener != null) {
            dialogOnKeyDownListener.onKeyDownListener(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
