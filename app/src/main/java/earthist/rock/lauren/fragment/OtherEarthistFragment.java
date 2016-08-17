package earthist.rock.lauren.fragment;


import android.content.Intent;
import android.net.Uri;
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
import earthist.rock.lauren.datas.CommonDatas;
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
    private Button button_facebook, button_twitter, button_instagram, button_youtube, button_sound;
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
        button_instagram = (Button)rootview.findViewById(R.id.button_instagram);
        button_youtube = (Button)rootview.findViewById(R.id.button_youtube);
        button_sound = (Button)rootview.findViewById(R.id.button_sound);

        button_sound.setBackgroundResource(CommonDatas.SOUND_BUTTONS[mParam1]);
        button_facebook.setBackgroundResource( CommonDatas.FACEBOOK_BUTTONS[mParam1]);
        button_youtube.setBackgroundResource(CommonDatas.YOUTUBE_BUTTONS[mParam1]);
        button_instagram.setBackgroundResource(CommonDatas.INSTAGRAM_BUTTONS[mParam1]);
        button_twitter.setBackgroundResource(CommonDatas.TWITTER_BUTTONS[mParam1]);

        button_facebook.setOnClickListener(this);
        button_twitter.setOnClickListener(this);
        button_instagram.setOnClickListener(this);
        button_youtube.setOnClickListener(this);
        button_sound.setOnClickListener(this);
        initButtonForFacebook();
        initButtonForYoutube();
        initButtonForInstagram();
        initButtonForSoundCloud();
        initButtonForTwitter();

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
    private void initButtonForFacebook() {
        String url_path = CommonDatas.FACEBOOK_LINK_SUPPORT[mParam1];
        if(url_path.trim().isEmpty()){
            button_facebook.setVisibility(View.GONE);
        }
    }
    private void initButtonForYoutube() {
        String url_path = CommonDatas.YOUTUBE_LINK_SUPPORT[mParam1];
        if(url_path.trim().isEmpty()){
            button_youtube.setVisibility(View.GONE);
        }
    }
    private void initButtonForTwitter() {
        String url_path = CommonDatas.TWITTER_LINK_SUPPORT[mParam1];
        if(url_path.trim().isEmpty()){
            button_twitter.setVisibility(View.GONE);
        }
    }
    private void initButtonForInstagram() {
        String url_path = CommonDatas.INSTAGRAM_LINK_SUPPORT[mParam1];
        if(url_path.trim().isEmpty()){
            button_instagram.setVisibility(View.GONE);
        }
    }
    private void initButtonForSoundCloud() {
        String url_path = CommonDatas.SOUND_LINK_SUPPORT[mParam1];
        if(url_path.trim().isEmpty()){
            button_sound.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id){
            case R.id.button_facebook:
                openURL(CommonDatas.FACEBOOK_LINK_SUPPORT[mParam1]);
                break;
            case R.id.button_twitter:
                openURL(CommonDatas.TWITTER_LINK_SUPPORT[mParam1]);
                break;
            case R.id.button_instagram:
                openURL(CommonDatas.INSTAGRAM_LINK_SUPPORT[mParam1]);
                break;
            case R.id.button_youtube:
                openURL(CommonDatas.YOUTUBE_LINK_SUPPORT[mParam1]);
                break;
            case R.id.button_sound:
                openURL(CommonDatas.SOUND_LINK_SUPPORT[mParam1]);
                break;
        }
    }
    private void openURL(String link_url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_url));
        startActivity(browserIntent);
    }
}
