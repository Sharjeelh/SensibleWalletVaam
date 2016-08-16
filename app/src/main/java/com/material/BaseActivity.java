package com.material;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cashmobi.R;


/**
 * <p> This is a base class for activities that defines a set of methods and modifications to properly customize
 * all activites in the app with material design guidelines and give them the same overall look and feel for a unified
 * user experience on all app sections </p>
 * @author Miki Mendelson-Mints
 */
public abstract class BaseActivity extends AppCompatActivity {

	private static final int ANIM_NEXT_ACTIVITY_IN = R.anim.push_right_in;
	private static final int ANIM_CURRENT_ACTIVITY_OUT = R.anim.push_left_out;
	private static final int ANIM_NEXT_ACTIVITY_OUT = R.anim.push_right_out;
	private static final int ANIM_CURRENT_ACTIVITY_IN = R.anim.push_left_in;

	/**
	 * The toolbar used as the action bar in all activities that extends {@link BaseActivity}
	 */
	private Toolbar mToolbar;

	/** The toolbar wrapper layout */
	private RelativeLayout mToolbarWrapperLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initAppCompatDesign();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Helper method to initiate all the material design components with backwards compatibility
	 */
	protected void initAppCompatDesign() {
		setStatusBarColor(R.color.material_color_primary);
        mToolbarWrapperLayout = (RelativeLayout) findViewById(R.id.toolbar);
        if (mToolbarWrapperLayout != null) {
            mToolbar = (Toolbar) mToolbarWrapperLayout.findViewById(R.id.toolbarInner);
            setSupportActionBar(mToolbar);
        }
	}

	/**
	 * Sets the status bar color for devices that run version {@value Build.VERSION_CODES#LOLLIPOP} and up
	 * @param colorResId The resource id the color
	 */
	public void setStatusBarColor(int colorResId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(colorResId));
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(ANIM_NEXT_ACTIVITY_IN, ANIM_CURRENT_ACTIVITY_OUT);
	}

	/**
	 * gets this activity's toolbar, or null if one isn't defined
	 * @return The activity toolbar that is also used as the action bar
	 */
    @Nullable
	public Toolbar getToolbar() { return mToolbar; }

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(ANIM_CURRENT_ACTIVITY_IN, ANIM_NEXT_ACTIVITY_OUT);
	}

    /**
     * Enable or disable the up button on the toolbar with some customization
     * @param enabled Set the up button to be enabled or disabled
     * @param drawableResId A resource id for the button {@link Drawable}
     * @param drawableColorFilter A color resource to use as a filter for the drawable
     */
    public void setDisplayHomeAsUpEnabled(boolean enabled, int drawableResId, int drawableColorFilter) {
        final Drawable upArrow = getResources().getDrawable(drawableResId);
        if(upArrow != null) {
            upArrow.setColorFilter(getResources().getColor(drawableColorFilter), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setHomeButtonEnabled(enabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        }
    }

    /**
     * Enable or disable the up button on the toolbar with some customization.
     * @param enabled Set the up button to be enabled or disabled
     * @param drawableResId A resource id for the button {@link Drawable}
     */
    public void setDisplayHomeAsUpEnabled(boolean enabled, int drawableResId) {
        setDisplayHomeAsUpEnabled(enabled, drawableResId, R.color.md_white_1000);
    }

	/**
	 * Initiate all the views in this activity, right after calling setContentView
	 */
	protected abstract void initViews();

}
