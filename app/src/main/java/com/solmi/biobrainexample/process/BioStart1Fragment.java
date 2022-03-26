package com.solmi.biobrainexample.process;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.solmi.biobrainexample.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BioStart1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BioStart1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static BioStart1Fragment instance;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btn_start;
    private Button btn_stop;
    private Button btn_go_next;
    private Button btn_go_prev;


    private BioStart1Fragment() {
        // Required empty public constructor
    }

    public static BioStart1Fragment getInstance(){
        if(instance==null){
            instance = new BioStart1Fragment();
        }
        return instance;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BioStart1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BioStart1Fragment newInstance(String param1, String param2) {
        BioStart1Fragment fragment = new BioStart1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //view 를 그리기 전에 컴포넌트에 접근하지 못함. inflater로 그린 후 접근해준다.
        View v = inflater.inflate(R.layout.frag_bio_start1, container, false);

        //BiostartActivity의 함수를 실행시킴
        btn_start = (Button) v.findViewById(R.id.btn_Start);
        btn_stop = (Button) v.findViewById(R.id.btn_Stop);
        btn_go_next = (Button) v.findViewById(R.id.btn_frag1_go_next);
        btn_go_prev = (Button) v.findViewById(R.id.btn_frag1_go_prev);

        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((BioStartActivity)BioStartActivity.mContext).onClickStart();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BioStartActivity)BioStartActivity.mContext).onClickStop();
            }
        });

        btn_go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BioStartActivity)BioStartActivity.mContext).setFrag(2);
            }
        });
        btn_go_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BioStartActivity)BioStartActivity.mContext).setFrag(0);
            }
        });

        return v;
    }
}