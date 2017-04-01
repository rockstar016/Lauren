package earthist.rock.lauren.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import earthist.rock.lauren.R;
import earthist.rock.lauren.datas.star_datas_list;

public class fragment_star_profile_view extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    private TextView txt_star_name;
    private ImageView img_star_photo;
    public fragment_star_profile_view() {
        // Required empty public constructor
    }
    public static fragment_star_profile_view newInstance(int param1) {
        fragment_star_profile_view fragment = new fragment_star_profile_view();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (int) getArguments().getSerializable(ARG_PARAM1);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_content_show,container,false);
        txt_star_name =(TextView) rootview.findViewById(R.id.txt_support_name);
        img_star_photo = (ImageView)rootview.findViewById(R.id.img_star_photo_first);
        txt_star_name.setText(star_datas_list.star_name_array[mParam1]);
        Picasso.with(rootview.getContext())
                .load(star_datas_list.star_profile_image_array_firstscreen[mParam1])
                .error(R.drawable.image_holder)
                .placeholder(R.drawable.image_holder)
                .into(img_star_photo);
       //  img_star_photo.setImageResource(star_datas_list.star_profile_image_array_firstscreen[mParam1]);
        return rootview;
    }
}
