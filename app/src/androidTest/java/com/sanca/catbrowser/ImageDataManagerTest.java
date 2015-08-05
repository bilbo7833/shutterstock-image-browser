package com.sanca.catbrowser;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;

import com.sanca.catbrowser.data.ImageAdapter;
import com.sanca.catbrowser.logic.ImageDataManager;
import com.sanca.catbrowser.ui.BrowserActivity;

/**
 * Created by sanca on 05.08.15.
 */
public class ImageDataManagerTest extends ActivityInstrumentationTestCase2<BrowserActivity> {


    public ImageDataManagerTest () {
        super(BrowserActivity.class);
    }

    public ImageDataManagerTest(Class<BrowserActivity> activityClass) {
        super(activityClass);
    }

    Activity mActivity;
    RecyclerView mRecyclerView;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.recycler_images);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void testFirstImageDataLoaded() throws Exception {
        final View decorView = mActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, mRecyclerView);
        ImageAdapter mImageAdapter = ImageDataManager.getInstance().getImageAdapter();
        Thread.sleep(5000); // sleep to wait for request for image data to finish

        // Test First round of ImageData is retrieved
        int initialItemCount = mImageAdapter.getItemCount();
        assertTrue(initialItemCount > 0);
        Log.d("Test", "initialItemCount = " + initialItemCount);
    }

    public void testImageDataLoadsOnScroll() throws Exception {
        ImageAdapter mImageAdapter = ImageDataManager.getInstance().getImageAdapter();
        final int initialItemCount = mImageAdapter.getItemCount();
        Log.d("Test", "initialItemCount = " + initialItemCount);
        // scroll to end of list - this should fire next request for image data
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                mLayoutManager.scrollToPosition(initialItemCount - 1);
            }
        });
        getInstrumentation().waitForIdleSync();
        Thread.sleep(10000); // sleep to wait for request for new image data to finish

        // Test new image items have been loaded in the adapter
        assertTrue(mImageAdapter.getItemCount() > initialItemCount);
    }

    public void testScreenOrientationChange() {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        // Scroll to image 42
        final int position = 42;
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mLayoutManager.scrollToPosition(position);
            }
        });
        getInstrumentation().waitForIdleSync();

        // Change screen orientation
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

        // Test image 42 still visible in landscape mode
        assertTrue(firstVisibleItemPosition - 1 <= position);
        assertTrue(position <= lastVisibleItemPosition + 1);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
