package com.xuexiang.xuidemo.fragment.expands.materialdesign.behavior;

import android.os.Handler;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.NewsCardViewListAdapter;
import com.xuexiang.xuidemo.adapter.entity.NewInfo;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.widget.MaterialLoadMoreView;
import com.xuexiang.xutil.tip.ToastUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/5/9 9:08
 */
@Page(name = "RecyclerView + CardView\n常见的列表展示组合")
public class RecyclerViewBehaviorFragment extends BaseFragment {
    @BindView(R.id.toolbar_recycler_view)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab_recycler_view)
    FloatingActionButton fab;

    private NewsCardViewListAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_behavior_recyclerview;
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }

    @Override
    protected void initListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("新建");
            }
        });
        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<NewInfo>() {
            @Override
            public void onItemClick(View itemView, NewInfo item, int position) {
                Utils.goWeb(getContext(), item.getDetailUrl());
            }
        });
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
    }

    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                enableLoadMore();
            }
        }, 1000);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        MaterialLoadMoreView loadMoreView = new MaterialLoadMoreView(getContext());
        recyclerView.addFooterView(loadMoreView);
        recyclerView.setLoadMoreView(loadMoreView);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                    if (recyclerView != null) {
                        recyclerView.loadMoreFinish(false, true);
                    }
                }
            }, 1000);
        }
    };

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

}
