package com.neu.youpin.location

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import kotlin.math.abs


/**
 * Description:左滑删除按钮
 *
 * Time:2021/7/1-20:37
 * Author:我叫PlusPlus
 */
class LeftDeleteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    /**
     * 上下文
     */
    private val mContext = context
    /**
     * 最小触摸距离
     */
    private var mMinTouchDistance : Int = 0
    /**
     * 右边可滑动距离
     */
    private var mRightCanDistance :Int = 0
    /**
     * 按下时 x
     */
    private var mInitX : Float = 0f
    /**
     * 按下y
     */
    private var mInitY : Float = 0f
    /**
     * 属性动画
     */
    private var mValueAnimator: ValueAnimator? = null
    /**
     * 动画时长
     */
    private var mAnimDuring = 300
    /**
     * RecyclerView
     */
    private var mRecyclerView: RecyclerView? = null
    /**
     * 是否重新计算
     */
    private var needResetCompute = true
    /**
     * 状态监听
     */
    var mStatusChangeLister : ((Boolean)-> Unit) ? = null

    init {
        //通过自定义属性获取删除按钮的宽度
        val array = mContext.obtainStyledAttributes(attrs, R.styleable.LeftDeleteView)
        val delWidth = array.getFloat(R.styleable.LeftDeleteView_deleteBtnWidth, 0f)
        array.recycle()
        mMinTouchDistance = ViewConfiguration.get(mContext).scaledTouchSlop
        //将dp转成PX
        val var2: Float = mContext.resources.displayMetrics.density
        //计算向右可以滑动的距离，单位PX
        mRightCanDistance = (delWidth * var2 + 0.5f).toInt()
        setBackgroundColor(Color.TRANSPARENT)
        orientation = HORIZONTAL
    }

    /**
     * 设置RecyclerView
     */
    fun setRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
    }


    /**
     * 处理触摸事件
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val actionMasked = ev.actionMasked
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mInitX = ev.rawX + scrollX
                mInitY = ev.rawY
                clearAnim()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mInitX - ev.rawX < 0) {
                    // mRecyclerView拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                    return false
                }
                // y轴方向上达到滑动最小距离, x 轴未达到
                if (abs(ev.rawY - mInitY) >= mMinTouchDistance
                    && abs(ev.rawY - mInitY) > abs(mInitX - ev.rawX - scrollX)) {
                    // mRecyclerView拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                    return false
                }
                // x轴方向达到了最小滑动距离，y轴未达到
                if (abs(mInitX - ev.rawX - scrollX) >= mMinTouchDistance
                    && abs(ev.rawY - mInitY) <= abs(mInitX - ev.rawX - scrollX)) {
                    // mRecyclerView拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mRecyclerView?.let {
                    it.requestDisallowInterceptTouchEvent(false)
                    needResetCompute = true
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 处理触摸事件
     * 需要注意:何时处理左滑，何时不处理
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mInitX = ev.rawX + scrollX
                mInitY = ev.rawY
                clearAnim()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mInitX - ev.rawX < 0) {
                    // 让父级容器拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                }
                // y轴方向上达到滑动最小距离, x 轴未达到
                if (abs(ev.rawY - mInitY) >= mMinTouchDistance
                    && abs(ev.rawY - mInitY) > abs(mInitX - ev.rawX - scrollX)) {
                    // mRecyclerView拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                }
                // x轴方向达到了最小滑动距离，y轴未达到
                if (abs(mInitX - ev.rawX - scrollX) >= mMinTouchDistance
                    && abs(ev.rawY - mInitY) <= abs(mInitX - ev.rawX - scrollX)) {
                    // mRecyclerView拦截
                    mRecyclerView?.let {
                        if (needResetCompute) {
                            it.requestDisallowInterceptTouchEvent(false)
                            needResetCompute = false
                        }
                    }
                }
                //手指移动距离超过最小距离
                var translationX = mInitX - ev.rawX
                //滑动距离已经大于右边可伸缩的距离后, 重新设置
                if (translationX > mRightCanDistance) {
                    mInitX = ev.rawX + mRightCanDistance
                }
                //互动距离小于0，那么重新设置初始位置
                if (translationX < 0) {
                    mInitX = ev.rawX
                }
                translationX = if (translationX > mRightCanDistance) mRightCanDistance.toFloat() else translationX
                translationX = if (translationX < 0) 0f else translationX
                // 向左滑动
                if (translationX <= mRightCanDistance && translationX >= 0) {
                    scrollTo(translationX.toInt(), 0)
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mRecyclerView?.let {
                    it.requestDisallowInterceptTouchEvent(false)
                    needResetCompute = true
                }
                upFingerAnim()
                return true
            }
            else -> {
            }
        }
        return true
    }


    /**
     * 清理动画
     */
    private fun clearAnim() {
        mValueAnimator?.let {
            it.end()
            it.cancel()
            mValueAnimator = null
        }
    }

    /**
     * 手指抬起开始执行动画
     */
    private fun upFingerAnim() {
        val scrollX = scrollX
        if (scrollX == mRightCanDistance || scrollX == 0) {
            //回调显示状态
            mStatusChangeLister?.invoke(scrollX == mRightCanDistance)
            return
        }
        clearAnim()
        // 如果显出一半松开手指，那么自动完全显示。否则完全隐藏
        if (scrollX >= mRightCanDistance / 3) {
            mValueAnimator = ValueAnimator.ofInt(scrollX, mRightCanDistance)
            //进行滑动
            mValueAnimator?.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                scrollTo(value, 0)
            }
            mValueAnimator?.duration = mAnimDuring.toLong()
            mValueAnimator?.start()
            //回调显示的状态
            mStatusChangeLister?.invoke(true)
        } else {
            mValueAnimator = ValueAnimator.ofInt(scrollX, 0)
            //进行滑动
            mValueAnimator?.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                scrollTo(value, 0)
            }
            mValueAnimator?.duration = mAnimDuring.toLong()
            mValueAnimator?.start()
            //回调关闭的状态
            mStatusChangeLister?.invoke(false)
        }
    }

    /**
     * 重置按钮删除状态
     */
    fun resetDeleteStatus() {
        val scrollX = scrollX
        if (scrollX == 0) {
            return
        }
        clearAnim()
        mValueAnimator = ValueAnimator.ofInt(scrollX, 0)
        //进行滑动
        mValueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            scrollTo(value, 0)
        })
        mValueAnimator?.duration = mAnimDuring.toLong()
        mValueAnimator?.start()
        //回调关闭的状态
        mStatusChangeLister?.invoke(false)
    }
}