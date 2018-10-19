package com.zzc.baselib.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.baselib.ui.listener.IProgressDialog;

public class BaseFragment extends Fragment implements IProgressDialog {

    protected BaseActivity activity;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            if (layoutId() > 0) {
                rootView = LayoutInflater.from(getContext()).inflate(layoutId(), null);
            }
        } else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    public int layoutId() {
        return 0;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onBaseInitView();

        initView();

        initData();

        onDataInited();
    }

    public void onDataInited() {
    }

    public void onBaseInitView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(willRetainInstance());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (BaseActivity) getActivity();
    }

    public void initView() {

    }

    public void initData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected boolean willRetainInstance() {
        return true;
    }

    public <T extends View> T findView(int id) {
        if (getView() == null)
            return null;
        return (T) getView().findViewById(id);
    }

    @Override
    public void showLoading(String text) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag("progressDialog");
        if (fragment != null && ((DialogFragment) fragment).getDialog() != null) {
            ((ProgressDialog) ((DialogFragment) fragment).getDialog()).setMessage(text);
        } else {
            AppCompatDialogFragment dialog = new ProgressDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", text);
            dialog.setArguments(args);
            if (getActivity() == null || getActivity().isFinishing())
                return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (getActivity().isDestroyed())
                    return;
            }
            if (getChildFragmentManager() == null || getChildFragmentManager().isDestroyed())
                return;
            try {
                dialog.show(getChildFragmentManager(), "progressDialog");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hideLoading() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag("progressDialog");
        if (fragment != null) {
            DialogFragment df = (DialogFragment) fragment;
            try {
                df.dismissAllowingStateLoss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProgressDialogFragment extends AppCompatDialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            Bundle args = getArguments();
            if (args != null) {
                String message = args.getString("message");
                progressDialog.setMessage(message);
            }
            progressDialog.setCanceledOnTouchOutside(false);
            return progressDialog;
        }
    }

}
