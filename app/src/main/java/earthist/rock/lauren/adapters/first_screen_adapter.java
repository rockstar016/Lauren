package earthist.rock.lauren.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

import earthist.rock.lauren.fragment.fragment_star_profile_view;

/**
 * Created by RockStar-0116 on 2016.07.14.
 */
public class first_screen_adapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 6;
        public first_screen_adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return fragment_star_profile_view.newInstance(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
}
