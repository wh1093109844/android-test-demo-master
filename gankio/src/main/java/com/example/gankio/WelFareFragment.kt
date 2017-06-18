package com.example.gankio

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide.init
import com.example.gankio.model.ImageSize
import com.example.gankio.model.Welfare
import com.example.gankio.model.WelfareResult
import com.example.gankio.retrofit.RequestWelfareService
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_welfare.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by za-wanghe on 2017/6/16.
 */
class WelFareFragment : Fragment() {

    val TAG = "WelFareFragment"

    var adapter: RecyclerViewAdapter = RecyclerViewAdapter()
    var currPage = 1
    var isLast = false
    lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView")
        return inflater?.inflate(R.layout.fragment_welfare, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onActivityCreated")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        layoutManager = StaggeredGridLayoutManager(Const.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        view?.welfareRecyclerView?.layoutManager = layoutManager
        view?.welfareRecyclerView?.adapter = adapter
        view?.welfareRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.i(TAG, "onScrollStateChanged\tnewState$newState")
                if (newState == 0) {

                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val array = layoutManager.findLastVisibleItemPositions(null)
                if (array[layoutManager.spanCount - 1] + Const.PRE_LOAD_SIZE >= adapter.itemCount && !(view?.swipeRefreshLayout?.isRefreshing ?: true)) {
                    currPage += 1
                    loadImageList()
                }
            }
        })
        view?.swipeRefreshLayout?.setOnRefreshListener {
            currPage = 1
            adapter.reset()
            loadImageList()
        }
        loadImageList()
    }

    fun loadImageList() {
        view?.swipeRefreshLayout?.isRefreshing = true
        var service: RequestWelfareService = Const.retrofit.create(RequestWelfareService::class.java)
        var callback = service.getWelfareList(Const.PAGE_SIZE, currPage)
        callback.enqueue(object : Callback<WelfareResult> {
            override fun onFailure(call: Call<WelfareResult>?, t: Throwable?) {
                Toast.makeText(this@WelFareFragment.context, "获取图片列表失败", Toast.LENGTH_SHORT).show()
                view?.swipeRefreshLayout?.isRefreshing = false
            }

            override fun onResponse(call: Call<WelfareResult>?, response: Response<WelfareResult>?) {
                var result = response?.body()
                Log.i(TAG, "${Gson().toJson(result)}")
                view?.swipeRefreshLayout?.isRefreshing = false
                if (result == null || result.results.isEmpty()) {
                    Toast.makeText(this@WelFareFragment.context, "已经是最后一页", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.append(result.results)
                }
            }

        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: SimpleDraweeView
        var textView: TextView
        var welfare: Welfare? = null

        init {
            imageView = itemView.findViewById<SimpleDraweeView>(R.id.imageView) as SimpleDraweeView
            textView = itemView.findViewById<TextView>(R.id.textView)  as TextView
            itemView.setOnClickListener { view -> welfare?.let {
                Toast.makeText(view.context, "${welfare?._id}", Toast.LENGTH_SHORT).show()
            } }
        }
    }

    class RecyclerViewAdapter() : RecyclerView.Adapter<ViewHolder>() {
        private var imageList: MutableList<Welfare> = mutableListOf()
        private var sizeMap = mutableMapOf<Int, ImageSize>()
        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            var welFare = imageList[position]
            var controller = Fresco.newDraweeControllerBuilder()?.setOldController(holder?.imageView?.controller)?.setUri(welFare.url)?.setTapToRetryEnabled(true)?.setControllerListener(object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                    imageInfo?.let {
                        val qualityInfo = imageInfo?.qualityInfo
                        if (qualityInfo.isOfGoodEnoughQuality) {
                            val imageHeight = imageInfo?.height
                            val imageWidth = imageInfo?.width
                            updateViewHeight(holder, getTargetViewHeight(imageWidth, imageHeight, holder?.imageView, position))
                        }
                    }

                }
            })?.build()
            sizeMap[position]?.let {
                if (it.height > 0) {
                    updateViewHeight(holder, it.height)
                }
            }
            holder?.imageView?.controller = controller
            holder?.textView?.text = "${welFare.desc}"
            holder?.welfare = welFare
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            var itemView = View.inflate(parent?.context, R.layout.item_welfare, null)
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        fun append(list: List<Welfare>) {
            imageList.addAll(list)
            notifyDataSetChanged()
        }

        fun updateViewHeight(holder: ViewHolder?, height: Int?) {
            if (holder?.itemView is ViewGroup) {
                var targetView = holder?.imageView
                var viewGroup = targetView.parent as ViewGroup
                var layoutParams = targetView.layoutParams
                layoutParams?.height = height
                viewGroup.updateViewLayout(targetView, layoutParams)
            }
        }

        fun getTargetViewHeight(width: Int, height:Int, view: View?, position: Int): Int? {
            view?.let {
                if (!sizeMap.containsKey(position) || sizeMap[position]?.width == 0) {
                    var targetWidth = view.measuredWidth
                    var targhtHeight = height * targetWidth / width
                    sizeMap.put(position, ImageSize(targetWidth, targhtHeight))
                }
            }
            return sizeMap[position]?.height
        }

        fun reset() {
            imageList.clear()
//            sizeMap.clear()
        }
    }

}
