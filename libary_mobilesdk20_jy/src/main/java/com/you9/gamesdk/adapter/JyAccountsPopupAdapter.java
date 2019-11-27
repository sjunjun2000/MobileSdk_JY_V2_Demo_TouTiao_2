package com.you9.gamesdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class JyAccountsPopupAdapter extends BaseAdapter {

	private Context context;
	private List<JyUser> accounts;
	private OnAccountDeleteListener onAccountDeleteListener;
	private OnAccountSelectedListener onAccountSelectedListener;

	public JyAccountsPopupAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.accounts = new ArrayList<JyUser>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accounts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return accounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		JyUser account = accounts.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					ResourceUtil.getLayoutId(context, "jy_item_accounts"), null);
			holder.account = (TextView) convertView
					.findViewById(ResourceUtil.getId(context, "text_account"));
			holder.account.setId(position);
			holder.delete = (ImageButton) convertView
					.findViewById(ResourceUtil.getId(context, "button_delete"));
			convertView.setTag(holder);
			holder.delete.setId(position);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.account.setText(account.getUsername());
		holder.account.setOnClickListener(new OnClickListener() {

			private OnAccountSelectedListener l = onAccountSelectedListener;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (this.l != null)
					l.onAccountSelected(position);
			}
		});

		holder.delete.setOnClickListener(new OnClickListener() {
			private OnAccountDeleteListener l = onAccountDeleteListener;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (this.l != null)
					l.onAccountdelete(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView account;
		public ImageButton delete;
	}

	public void refrash(List<JyUser> accounts) {
		this.accounts = accounts;
		notifyDataSetChanged();
	}

	public interface OnAccountSelectedListener {
		public void onAccountSelected(int position);
	}

	public interface OnAccountDeleteListener {
		public void onAccountdelete(int position);
	}

	public void setOnAccountDeleteListener(
			OnAccountDeleteListener onAccountDeleteListener) {
		this.onAccountDeleteListener = onAccountDeleteListener;
	}

	public void setOnAccountSelectedListener(
			OnAccountSelectedListener onAccountSelectedListener) {
		this.onAccountSelectedListener = onAccountSelectedListener;
	}
}
