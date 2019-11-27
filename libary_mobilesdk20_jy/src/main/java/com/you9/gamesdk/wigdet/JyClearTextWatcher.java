package com.you9.gamesdk.wigdet;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

public class JyClearTextWatcher implements TextWatcher {

	private ImageButton clearButton;

	public JyClearTextWatcher(ImageButton clearButton) {
		// TODO Auto-generated constructor stub
		this.clearButton = clearButton;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.length() == 0) {
			clearButton.setVisibility(View.INVISIBLE);
		} else {
			clearButton.setVisibility(View.VISIBLE);
		}
	}

}
