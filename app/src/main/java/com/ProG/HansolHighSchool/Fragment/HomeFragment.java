package com.ProG.HansolHighSchool.Fragment;

import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_HansolHS;
import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_RiroSchool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ProG.HansolHighSchool.Activity.AccountInfoActivity;
import com.ProG.HansolHighSchool.Activity.LoginActivity;
import com.ProG.HansolHighSchool.Data.LoginData;
import com.ProG.HansolHighSchool.R;

public class HomeFragment extends Fragment {

    Button btn_hansolhs, btn_riroschool, btn_account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        btn_hansolhs = view.findViewById(R.id.btn_hansolhs);
        btn_riroschool = view.findViewById(R.id.btn_riroschool);
        btn_account = view.findViewById(R.id.btn_account);

        btn_hansolhs.setOnClickListener(v -> {
            Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_HansolHS));
            startActivity(intentURL);
        });

        btn_riroschool.setOnClickListener(v -> {
            Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_RiroSchool));
            startActivity(intentURL);
        });

        btn_account.setOnClickListener(v -> {

            Intent intentActivity;
            if (LoginData.isLogin) {
                intentActivity = new Intent(getActivity(), AccountInfoActivity.class);
            } else {
                intentActivity = new Intent(getActivity(), LoginActivity.class);
            }
            startActivity(intentActivity);

        });

        return view;
    }

}
