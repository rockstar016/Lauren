package earthist.rock.lauren.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import earthist.rock.lauren.R;
import earthist.rock.lauren.datas.cause_data_list;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CauseBioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CauseBioFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private TextView txt_cause_title,txt_cause_content;
    private ImageView image_cause_symbol;
    public CauseBioFragment() {
        // Required empty public constructor
    }

    public static CauseBioFragment newInstance(int index) {
        CauseBioFragment fragment = new CauseBioFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, index);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 =  getArguments().getInt(ARG_PARAM1);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cause_bio_detail,container,false);
        txt_cause_content = (TextView)rootview.findViewById(R.id.txt_cause_content);
        txt_cause_title = (TextView)rootview.findViewById(R.id.txt_cause_name);
        image_cause_symbol = (ImageView)rootview.findViewById(R.id.image_cause_photo);
        txt_cause_content.setText(cause_data_list.cause_content_array[mParam1-7]);
        txt_cause_title.setText(cause_data_list.cause_title_array[mParam1-7] + " Cause Bio");
        image_cause_symbol.setImageResource(cause_data_list.cause_symbol_image[mParam1-7]);
        return rootview;
    }
}
