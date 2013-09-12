
package com.example.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyListview extends ListView implements OnScrollListener {

    private static final int NORMAL = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    private OnRefreshListener mOnRefreshListener;
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private RelativeLayout mRefreshView;
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    private int mCurrentScrollState;
    private int mRefreshState;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

    private int mRefreshViewHeight;
    private int mRefreshOriginalTopPadding;
    private int mLastMotionY;

    private boolean mBounceHack;

    public MyListview(Context context) {
        super(context);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Load all of the animations we need in code rather than through XML
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);
    	
    	//LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
		mRefreshView = (RelativeLayout) mInflater.inflate(
				R.layout.pull_to_refresh_header, this, false);
		
        mRefreshViewText =
            (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =
            (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =
            (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated =
            (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

		//在初始化时记录下listview现在距离顶部的距离，之后还原时根据这个数据设置listview的pPaddingTop
        mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

        mRefreshState = NORMAL;

        //addHeaderView:在ListView组件上方添加上其他组件
        addHeaderView(mRefreshView);

        super.setOnScrollListener(this);

        measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
    }

    //onAttachedToWindow在onDraw方法之前调用，也就是view还没有画出来的时候，可以在此方法中去执行一些初始化的操作
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSelection(1);	//setSelection:让ListView定位到指定的位置
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mOnRefreshListener = refreshListener;
    }

    //显示最近刷新的时间
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mBounceHack = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            	mLastMotionY=0;
            	//将垂直滚动条设置为可用状态 
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                
                //getFirstVisiblePosition:获取listview中第一个可见项目的在listview中的坐标
                //为0时说明listview已经到最顶端，此时再往下拉就要出现下拉刷新的提示
                if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
                	
                	//1.mRefreshView底部的位置已经大于mRefreshView的高度
                	//2.mRefreshView的顶部已经出现在屏幕上
                	//以上两种情况都是说明，mRefreshView已经完全显示在屏幕上，此时可以开始刷新
                    if ((mRefreshView.getBottom() >= mRefreshViewHeight
                            || mRefreshView.getTop() >= 0)
                            && mRefreshState == RELEASE_TO_REFRESH) {
                    	
                        mRefreshState = REFRESHING;
                        prepareForRefresh();
                        onRefresh();
                        
                    } else if (mRefreshView.getBottom() < mRefreshViewHeight
                            || mRefreshView.getTop() <= 0) {
                    	
                        resetHeader();
                        setSelection(1);
                        
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                applyHeaderPadding(event);
                break;
        }
        return super.onTouchEvent(event);
    }

    //在拖动listview的过程中，根据拖动的距离设置listview PaddingTop的距离
    private void applyHeaderPadding(MotionEvent ev) {
        int pointerCount = ev.getHistorySize();

        for (int p = 0; p < pointerCount; p++) {
            if (mRefreshState == RELEASE_TO_REFRESH) {
            	
            	//ListView顶部和底部的过渡阴影
                if (isVerticalFadingEdgeEnabled()) {
                    setVerticalScrollBarEnabled(false);
                }

                int MotionY = (int) ev.getHistoricalY(p);
                if(mLastMotionY==0){
                	mLastMotionY = MotionY;
                }
                	
                int topPadding = mRefreshView.getPaddingTop() + (int)((MotionY - mLastMotionY)/1.7);
                mRefreshView.setPadding(
                        mRefreshView.getPaddingLeft(),
                        topPadding,
                        mRefreshView.getPaddingRight(),
                        mRefreshView.getPaddingBottom());
            	mLastMotionY = MotionY;
            }
        }
    }

    //将listview重置到原始位置
    private void resetHeaderPadding() {
        mRefreshView.setPadding(
                mRefreshView.getPaddingLeft(),
                mRefreshOriginalTopPadding,
                mRefreshView.getPaddingRight(),
                mRefreshView.getPaddingBottom());
    }
    
    private void resetHeader() {
        if (mRefreshState != NORMAL) {
            mRefreshState = NORMAL;

            resetHeaderPadding();

            mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
            mRefreshViewImage.setImageResource(R.drawable.ic_pulltorefresh_arrow);
            mRefreshViewImage.clearAnimation();
            mRefreshViewProgress.setVisibility(View.GONE);
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
                && mRefreshState != REFRESHING) {
            if (firstVisibleItem == 0) {
                if (mRefreshState == PULL_TO_REFRESH && mRefreshView.getBottom() >= mRefreshViewHeight) {
                	//在已经下拉listview的状态下，下拉到可以刷新的状态

                	//显示松开可以刷新的提示
                    mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;

                } else if ((mRefreshState == NORMAL)||(mRefreshState == RELEASE_TO_REFRESH && mRefreshView.getBottom() <= mRefreshViewHeight)) {
                	//1.从正常状态开始下拉
                	//2.在可以刷新的状态下将listview上移到不能刷新的状态
                	
                	//因为mRefreshView在第一次加载后有可能会加一行文字显示最后更新时间，所以在normal状态下时需要重新获取mRefreshView的高度
//                	if(mRefreshState == NORMAL){
//                        mRefreshViewHeight = mRefreshView.getHeight();
//                        Log.d("wy","mRefreshViewHeight = "+mRefreshViewHeight);
//                	}

					if (mRefreshState != NORMAL) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    //显示下拉可以刷新的提示
                	mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {
                resetHeader();
            }
        } else if (mCurrentScrollState == SCROLL_STATE_FLING
                && firstVisibleItem == 0
                && mRefreshState != REFRESHING) {
            setSelection(1);
            mBounceHack = true;
        } else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
            setSelection(1);
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mCurrentScrollState == SCROLL_STATE_IDLE) {
            mBounceHack = false;
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void prepareForRefresh() {
        resetHeaderPadding();
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);
        mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);
        mRefreshViewLastUpdated.setVisibility(View.GONE);

        mRefreshState = REFRESHING;
    }
    
    public void clickRefresh(){
        if (mRefreshState != REFRESHING) {
            prepareForRefresh();
            onRefresh();
        }
    }

    public void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void onRefreshComplete(CharSequence lastUpdated) {
        //更新完成后，记录下最后更新的时间
        setLastUpdated(lastUpdated);
        resetHeader();
        
        if (mRefreshView.getBottom() > 0) {
        	//通知listview重绘
            invalidateViews();
            setSelection(1);
        }
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }
}