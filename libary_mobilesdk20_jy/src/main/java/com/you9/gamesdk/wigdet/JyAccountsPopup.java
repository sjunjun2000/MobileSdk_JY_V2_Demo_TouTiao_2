package com.you9.gamesdk.wigdet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.you9.gamesdk.adapter.JyAccountsPopupAdapter;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.util.ResourceUtil;

import java.util.List;

/**
 * 登录显示登录过的账户列表
 * 
 * @author guangqing_lu
 * 
 */
public class JyAccountsPopup extends PopupWindow {

	private OnShowListener onShowListener;
	private OnDismissListener onDismissListener;
	private ListView mAccountsList;
	private JyAccountsPopupAdapter accountsAdapter;
	public JyAccountsPopup(Context context, List<JyUser> accounts) {
		View view = LayoutInflater.from(context).inflate(
				ResourceUtil.getLayoutId(context, "jy_popup_accounts"), null);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setContentView(view);
		setFocusable(true);// 获取焦点
		setOutsideTouchable(true);// 外部可以点击
//		setBackgroundDrawable(context.getResources().getDrawable(
//				ResourceUtil.getDrawableId(context, "jy_bg_accounts_popup")));

		accountsAdapter = new JyAccountsPopupAdapter(context);
		mAccountsList = (ListView) view.findViewById(ResourceUtil.getId(context, "list_accounts"));
		mAccountsList.setAdapter(accountsAdapter);

		accountsAdapter.refrash(accounts);
	}

	public JyAccountsPopup(Context context, AttributeSet attributeSet) {

	}

	public void refreshView(List<JyUser> accounts) {
		accountsAdapter.refrash(accounts);
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		// TODO Auto-generated method stub
		super.showAsDropDown(anchor, xoff, yoff);
		if (this.onShowListener != null)
			onShowListener.onAccountsPopupShow();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (this.onDismissListener != null)
			onDismissListener.onAccountsPopupDismiss();
	}

	public void setOnAccountDeleteListener(
			JyAccountsPopupAdapter.OnAccountDeleteListener onAccountDeleteListener) {
		accountsAdapter.setOnAccountDeleteListener(onAccountDeleteListener);
	}

	public void setOnAccountSelectedListener(
			JyAccountsPopupAdapter.OnAccountSelectedListener onAccountSelectedListener) {
		accountsAdapter.setOnAccountSelectedListener(onAccountSelectedListener);
	}

	public void setOnShowListener(OnShowListener listener) {
		this.onShowListener = listener;
	}

	public void setOnDismissListener(OnDismissListener listener) {
		this.onDismissListener = listener;
	}

	public interface OnShowListener {
		public void onAccountsPopupShow();
	}

	public interface OnDismissListener {
		public void onAccountsPopupDismiss();
	}

}
