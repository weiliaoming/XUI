/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xui.adapter.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.utils.DensityUtils;

/**
 * 可自定义分割线样式
 *
 * @author XUE
 * @date 2017/9/10 15:24
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;
    private int mSpanCount;
    /**
     * 分割线宽度，默认为1dp
     */
    private int mDividerWidth;
    /**
     * 画笔
     */
    private Paint mPaint;

    public GridDividerItemDecoration(Context context, int spanCount) {
        mSpanCount = spanCount;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        if (mDivider != null) {
            mDividerWidth = mDivider.getIntrinsicHeight();
        } else {
            mDividerWidth = DensityUtils.dp2px(1);
        }
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param spanCount
     * @param dividerWidth
     */
    public GridDividerItemDecoration(Context context, int spanCount, int dividerWidth) {
        this(context, spanCount);
        mDividerWidth = dividerWidth;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param spanCount
     * @param dividerWidth
     * @param dividerColor
     */
    public GridDividerItemDecoration(Context context, int spanCount, int dividerWidth, int dividerColor) {
        this(context, spanCount);
        mDividerWidth = dividerWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(child);
            int column = (position + 1) % 3;
            column = column == 0 ? mSpanCount : column;

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mDividerWidth;
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDividerWidth;

            if (mPaint != null) {
                mDivider.setBounds(child.getLeft(), top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(child.getLeft(), top, right, bottom, mPaint);
            }

            if (column < mSpanCount) {
                if (mPaint != null) {
                    mDivider.setBounds(left, child.getTop(), right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, child.getTop(), right, bottom, mPaint);
                }
            }

        }
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if ((position + 1) % mSpanCount > 0) {
            outRect.set(0, 0, mDividerWidth, mDividerWidth);
        } else {
            outRect.set(0, 0, 0, mDividerWidth);
        }
    }
}
