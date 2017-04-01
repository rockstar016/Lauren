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
import earthist.rock.lauren.Wander_GypCActivity;
import earthist.rock.lauren.datas.CommonDatas;
import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.star_datas_list;

public class WanderinGypCFragment extends Fragment implements View.OnClickListener{
    Button button_facebook, button_twitter, button_instagram, button_youtube,button_sound;
    Button main_cause_bio1, main_cause_bio2;
    private static final String ARG_PARAM1 = "param1";
    private int param;
    ImageView star_image, star_circle_image;
    TextView star_name;
    TextView star_profile;
    public WanderinGypCFragment() {
        // Required empty public constructor
    }
  public static WanderinGypCFragment newInstance(int param) {
        WanderinGypCFragment fragment = new WanderinGypCFragment();
      Bundle args = new Bundle();
      args.putSerializable(ARG_PARAM1, param);
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.param = (int) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.fragment_star_detail_other,container,false);
        button_facebook = (Button)rootview.findViewById(R.id.button_facebook);
        button_twitter = (Button)rootview.findViewById(R.id.button_twitter);
        button_instagram = (Button)rootview.findViewById(R.id.button_instagram);
        button_youtube = (Button)rootview.findViewById(R.id.button_youtube);
        button_sound = (Button)rootview.findViewById(R.id.button_sound);
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
        main_cause_bio1 = (Button)rootview.findViewById(R.id.button_cause_bio1);
        main_cause_bio2 = (Button)rootview.findViewById(R.id.button_cause_bio2);

        star_name = (TextView)rootview.findViewById(R.id.txt_star_name);
        star_profile = (TextView)rootview.findViewById(R.id.txt_star_profile);
        star_image = (ImageView)rootview.findViewById(R.id.image_support_photo);
        star_circle_image = (ImageView)rootview.findViewById(R.id.image_profile_photo);
        main_cause_bio1.setOnClickListener(this);
        main_cause_bio2.setOnClickListener(this);
        if(param == 0){
            main_cause_bio1.setText(cause_data_list.cause_title_array[param] + " Cause Bio");
            main_cause_bio2.setVisibility(View.VISIBLE);
            main_cause_bio2.setText(cause_data_list.cause_title_array[param+1] + " Cause Bio");
        }
        else{
            main_cause_bio1.setText(cause_data_list.cause_title_array[param+1] + " Cause Bio");
            main_cause_bio2.setVisibility(View.GONE);
            main_cause_bio2.setText("");
        }
        Picasso.with(rootview.getContext())
                        .load(star_datas_list.star_profile_image_array_firstscreen[param])
                        .error(R.drawable.image_holder)
                        .placeholder(R.drawable.image_holder)
                        .into(star_image);
        Picasso.with(rootview.getContext())
                .load(star_datas_list.star_profile_circle_array_detailscreen[param])
                .error(R.drawable.image_holder)
                .placeholder(R.drawable.image_holder)
                .into(star_circle_image);
        star_profile.setText(star_datas_list.starprofile_list_array[param]);
        star_name.setText(star_datas_list.star_name_array[param]);
        return rootview;
    }

    private void initButtonForFacebook() {
        String url_path = CommonDatas.FACEBOOK_LINK_STAR[param];
        if(url_path.trim().isEmpty()){
            button_facebook.setVisibility(View.GONE);
        }
    }
    private void initButtonForYoutube() {
        String url_path = CommonDatas.YOUTUBE_LINK_STAR[param];
        if(url_path.trim().isEmpty()){
            button_youtube.setVisibility(View.GONE);
        }
    }
    private void initButtonForTwitter() {
        String url_path = CommonDatas.TWITTER_LINK_STAR[param];
        if(url_path.trim().isEmpty()){
            button_twitter.setVisibility(View.GONE);
        }
    }
    private void initButtonForInstagram() {
        String url_path = CommonDatas.INSTAGRAM_LINK_STAR[param];
        if(url_path.trim().isEmpty()){
            button_instagram.setVisibility(View.GONE);
        }
    }
    private void initButtonForSoundCloud() {
        String url_path = CommonDatas.SOUND_LINK_STAR[param];
        if(url_path.trim().isEmpty()){
            button_sound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        if(view_id == R.id.button_cause_bio1){
            onClickCauseBio();
        }
        else if(view_id == R.id.button_cause_bio2){
            onClickCauseBio2();
        }


        else if(view_id == R.id.button_facebook){
           openURL(CommonDatas.FACEBOOK_LINK_STAR[param]);
        }
        else if(view_id == R.id.button_twitter){
            openURL(CommonDatas.TWITTER_LINK_STAR[param]);
        }
        else if(view_id == R.id.button_instagram){
            openURL(CommonDatas.INSTAGRAM_LINK_STAR[param]);
        }
        else if(view_id == R.id.button_youtube){
            openURL(CommonDatas.YOUTUBE_LINK_STAR[param]);
        }
        else if(view_id == R.id.button_sound){
            openURL(CommonDatas.SOUND_LINK_STAR[param]);
        }
    }
    private void openURL(String link_url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_url));
        startActivity(browserIntent);
    }
    private void onClickCauseBio(){
        if(param == 0) {
            ((Wander_GypCActivity) getActivity()).FragmentSetupCauseIdx(param + 7);
            ((Wander_GypCActivity) getActivity()).setActonBarTextCauseIdx(param);
        }
        else{
            ((Wander_GypCActivity) getActivity()).FragmentSetupCauseIdx(param + 8);
            ((Wander_GypCActivity) getActivity()).setActonBarTextCauseIdx(param+1);
        }
    }
    private void onClickCauseBio2(){
        ((Wander_GypCActivity)getActivity()).FragmentSetupCauseIdx(param + 8);
        ((Wander_GypCActivity)getActivity()).setActonBarTextCauseIdx(param + 1);
    }
}
