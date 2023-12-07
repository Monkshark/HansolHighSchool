package com.ProG.HansolHighSchool.Fragment;

import static com.ProG.HansolHighSchool.Adapter.CalendarUtil.selectedDate;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ProG.HansolHighSchool.Adapter.CalendarAdapter;
import com.ProG.HansolHighSchool.Adapter.CalendarUtil;
import com.ProG.HansolHighSchool.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class NoticeFragment extends Fragment {

    private TextView tv_monthYear;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_flDate;
    private RecyclerView recyclerView;
    protected ImageButton btn_next, btn_pre;
    FirebaseDatabase firebaseRead;
    DatabaseReference firebaseWrite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment, container, false);

        initializeFirebase();
        initializeViews(view);

        CalendarUtil.selectedDate = Calendar.getInstance();
        setMonthView();

        btn_pre.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, - 1);
            setMonthView();
        });

        btn_next.setOnClickListener(v -> {
            selectedDate.add(Calendar.MONTH, 1);
            setMonthView();
        });

        return view;
    }

    private String MonthYearFromDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + " - " + month;
    }

    private void setMonthView() {
        tv_monthYear.setText(MonthYearFromDate(selectedDate));
        ArrayList<Date> dayList = daysInMonthArray(selectedDate);
        CalendarAdapter adapter = new CalendarAdapter(dayList);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 7);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Date> daysInMonthArray(Calendar date) {
        ArrayList<Date> dayList = new ArrayList<>();
        Calendar monthCalendar = (Calendar) selectedDate.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        while (dayList.size() < 42) {
            dayList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dayList;
    }

    private void initializeViews(View rootView) {
        tv_monthYear = rootView.findViewById(R.id.tv_monthYear);
        tv_flDate = rootView.findViewById(R.id.tv_flDate);
        btn_pre = rootView.findViewById(R.id.btn_pre);
        btn_next = rootView.findViewById(R.id.btn_next);
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    private void initializeFirebase() {
        firebaseRead = FirebaseDatabase.getInstance();
        firebaseWrite = FirebaseDatabase.getInstance().getReference();
    }


}
