package earthist.rock.lauren.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import earthist.rock.lauren.R;
import earthist.rock.lauren.datas.color_list_based_cause;
import earthist.rock.lauren.datas.support_profile_list;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherEarthistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherEarthistFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private TextView txt_support_name, txt_support_job;
    private TextView txt_support_profile;
    private ImageView img_support_photo;
    private Button button_facebook, button_twitter, button_photo, button_youtube;
    public OtherEarthistFragment() {
        // Required empty public constructor
    }

    public static OtherEarthistFragment newInstance(int index) {
        OtherEarthistFragment fragment = new OtherEarthistFragment();
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
       View rootview = inflater.inflate(R.layout.fragment_other_earthist_detail,container,false);
        mParam1 -= 10;
        txt_support_job = (TextView) rootview.findViewById(R.id.txt_support_job);
        txt_support_profile = (TextView)rootview.findViewById(R.id.txt_support_profile);
        txt_support_name = (TextView)rootview.findViewById(R.id.txt_support_name);
        img_support_photo = (ImageView)rootview.findViewById(R.id.image_support_photo);

        button_facebook = (Button)rootview.findViewById(R.id.button_facebook);
        button_twitter = (Button)rootview.findViewById(R.id.button_twitter);
        button_photo = (Button)rootview.findViewById(R.id.button_photo);
        button_youtube = (Button)rootview.findViewById(R.id.button_youtube);

        txt_support_job.setText(support_profile_list.support_title_array[mParam1]);
        txt_support_job.setTextColor(getResources().getColor(color_list_based_cause.cause_theme_color[mParam1]));
        txt_support_profile.setText(support_profile_list.support_profile_list_array[mParam1]);
        txt_support_name.setText(support_profile_list.support_name_array[mParam1]);
        txt_support_name.setTextColor(getResources().getColor(color_list_based_cause.cause_theme_color[mParam1]));

        Picasso.with(rootview.getContext())
                .load(support_profile_list.support_profile_image[mParam1])
                .error(R.drawable.image_holder)
                .placeholder(R.drawable.image_holder)
                .into(img_support_photo);
        return rootview;
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id){
            case R.id.button_facebook:
                break;
            case R.id.button_twitter:
                break;
            case R.id.button_photo:
                break;
            case R.id.button_youtube:
                break;
        }
    }
}
