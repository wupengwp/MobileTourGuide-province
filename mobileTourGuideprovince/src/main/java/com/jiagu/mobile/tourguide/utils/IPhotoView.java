/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jiagu.mobile.tourguide.utils;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;


public interface IPhotoView {
    /**
     * Returns true if the PhotoView is set to allow zooming of Photos.
     * 如果PhotoView设置为允许放大照片,返回true
     * @return true if the PhotoView allows zooming.
     */
    boolean canZoom();

    /**
     * Gets the Display Rectangle of the currently displayed Drawable. The
     * Rectangle is relative to this View and includes all scaling and
     * translations.
     * 获取当前显示的显示矩形可拉的。矩形是相对于这一观点,包括所有的扩展和翻译。
     * @return - RectF of Displayed Drawable
     */
    RectF getDisplayRect();

    /**
     * @return The current minimum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 目前的最低规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }
     */
    float getMinScale();

    /**
     * @return The current middle scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 当前中等规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }。
     */
    float getMidScale();

    /**
     * @return The current maximum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 当前高等规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }。
     */
    float getMaxScale();

    /**
     * Returns the current scale value
     * 返回当前的刻度值
     *
     * @return float - current scale value
     */
    float getScale();

    /**
     * Return the current scale type in use by the ImageView.
     * 返回当前规模类型ImageView使用。
     */
    ImageView.ScaleType getScaleType();

    /**
     * Whether to allow the ImageView's parent to intercept the touch event when the photo is scroll to it's horizontal edge.
     * 是否允许ImageView拦截触摸事件照片时滚动的水平边缘。
     */
    void setAllowParentInterceptOnEdge(boolean allow);

    /**
     * Sets the minimum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 设置最低规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }。
     */
    void setMinScale(float minScale);

    /**
     * Sets the middle scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 设置中等规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }。
     */
    void setMidScale(float midScale);

    /**
     * Sets the maximum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     * 设置高等规模水平。这个值代表什么取决于当前{ @link android.widget.ImageView.ScaleType }。
     */
    void setMaxScale(float maxScale);

    /**
     * Register a callback to be invoked when the Photo displayed by this view is long-pressed.
     * 注册一个回调函数被调用时所显示的照片这个观点是长期呼吁。
     *
     * @param listener - Listener to be registered.
     */
    void setOnLongClickListener(View.OnLongClickListener listener);

    /**
     * Register a callback to be invoked when the Matrix has changed for this
     * View. An example would be the user panning or scaling the Photo.
     *
     * @param listener - Listener to be registered.
     */
    void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener);

    /**
     * Register a callback to be invoked when the Photo displayed by this View
     * is tapped with a single tap.
     *
     * @param listener - Listener to be registered.
     */
    void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener);

    /**
     * Register a callback to be invoked when the View is tapped with a single
     * tap.
     *
     * @param listener - Listener to be registered.
     */
    void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener);

    /**
     * Controls how the image should be resized or moved to match the size of
     * the ImageView. Any scaling or panning will happen within the confines of
     * this {@link android.widget.ImageView.ScaleType}.
     *
     * @param scaleType - The desired scaling mode.
     */
    void setScaleType(ImageView.ScaleType scaleType);

    /**
     * Allows you to enable/disable the zoom functionality on the ImageView.
     * When disable the ImageView reverts to using the FIT_CENTER matrix.
     *
     * @param zoomable - Whether the zoom functionality is enabled.
     */
    void setZoomable(boolean zoomable);

    /**
     * Zooms to the specified scale, around the focal point given.
     * 缩放到指定的规模,在给定的焦点。
     *
     * @param scale  - Scale to zoom to
     * @param focalX - X Focus Point
     * @param focalY - Y Focus Point
     */
    void zoomTo(float scale, float focalX, float focalY);
}
