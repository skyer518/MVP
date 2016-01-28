package cn.com.u2be.mvptest.hlkwifi.widget;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.u2be.mvptest.hlkwifi.R;

/**
 * Created by alek on 2016/1/28.
 */
public class WifiConnectDialogView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.et_password)
    EditText etPassword;

    @Bind(R.id.cb_showPassword)
    CheckBox cbShowPassword;

    public WifiConnectDialogView(Context context) {
        super(context);
        View view = inflate(context, R.layout.dialog_wifi_input, this);
        ButterKnife.bind(this, view);
        cbShowPassword.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }


    public String getPassword(){
        return etPassword.getEditableText().toString().trim();
    }
}
