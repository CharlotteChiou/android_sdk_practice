package a.exam.demo.view

import a.exam.demo.databinding.ActivityMainBinding
import a.exam.demo.model.NewsData
import a.exam.demo.model.NewsResponse
import a.exam.demo.networkservice.ApiState
import a.exam.demo.viewmodel.MainActivityVM
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMainActivityVM: MainActivityVM
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mMainListAdapter: MainListRecyclerView
    private var mNewsItems = arrayListOf<NewsData>()
    private var mAdTimerMap = HashMap<String, CountDownTimer>()

    private val logTag = "ExamDemo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
        collects()
    }

    override fun onDestroy() {
        for (timer in mAdTimerMap) {
            timer.value.cancel()
        }

        super.onDestroy()
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView = binding.recyclerView
        mRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            mRecyclerView.context,
            layoutManager.orientation
        )
        mRecyclerView.addItemDecoration(dividerItemDecoration)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager
                val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
                val lastPosition = linearLayoutManager.findLastVisibleItemPosition()

                val globalVisibleRect = Rect()
                mRecyclerView.getGlobalVisibleRect(globalVisibleRect)
                for (pos in firstPosition..lastPosition) {
                    val view = linearLayoutManager.findViewByPosition(pos)
                    if (view != null) {
                        val percentage = getVisibleHeightPercentage(view)
                        mNewsItems[pos].percent = percentage.toInt()
                        mMainListAdapter.notifyDataSetChanged()

                        // TODO: remove this, only for test
//                        if (pos % 2 == 0) {
//                            mNewsItems[pos].isAd = true
//                        }
                        handleTimerForAd(pos)
                    }
                }
            }
        })

        binding.swipeLayout.setOnRefreshListener {
            mMainActivityVM.getNewsList()
        }
    }

    // TODO: move to sdk
    private fun getVisibleHeightPercentage(view: View): Double {

        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

        // Find the height of the item.
        val visibleHeight = itemRect.height().toDouble()
        val height = view.measuredHeight

        val viewVisibleHeightPercentage = visibleHeight / height * 100

        return if (isParentViewEmpty) {
            viewVisibleHeightPercentage
        } else {
            0.0
        }
    }

    // TODO: move to SDK
    private fun handleTimerForAd(pos: Int) {
        val mapKey = pos.toString()

        if (mNewsItems[pos].percent >= 50 && mNewsItems[pos].isAd) {
            if (!mAdTimerMap.containsKey(mapKey)) {
                Log.d(logTag, "pos: $pos, is start.")
                val timer = object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        // TODO: send data to server
                        Log.d(logTag, "pos: $pos, is finish.")
                    }
                }
                timer.start()
                mAdTimerMap[mapKey] = timer
            }
        } else {
            if (mAdTimerMap.containsKey(mapKey)) {
                val timer = mAdTimerMap[pos.toString()]
                timer?.cancel()
                mAdTimerMap.remove(mapKey)
                Log.d("TAG", "pos: $pos, is cancel.")
            }
        }
    }

    private fun initViewModel() {
        mMainActivityVM = ViewModelProvider(this)[MainActivityVM::class.java]

        mMainActivityVM.getNewsList()
    }

    private fun collects() {
        lifecycleScope.launchWhenCreated {
            mMainActivityVM.wMessage.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.swipeLayout.isRefreshing = true
                    }

                    is ApiState.Failure -> {
                        it.e.printStackTrace()
                        binding.swipeLayout.isRefreshing = false
                    }

                    is ApiState.Success -> {
                        binding.swipeLayout.isRefreshing = false

                        val myObj = it.data as NewsResponse
                        mNewsItems.clear()

                        for (item in myObj.articles) {
                            mNewsItems.add(NewsData(item.title, item.summary, 100))
                        }

                        mMainListAdapter = MainListRecyclerView(mNewsItems)
                        mRecyclerView.adapter = mMainListAdapter

                        mMainListAdapter.notifyDataSetChanged()
                    }

                    is ApiState.Empty -> {
                        println("Empty...")
                    }
                }
            }
        }
    }
}