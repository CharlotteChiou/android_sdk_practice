package a.exam.demo.view

import a.exam.coresdk.Utility
import a.exam.demo.databinding.ActivityMainBinding
import a.exam.demo.model.NewsData
import a.exam.demo.model.NewsResponse
import a.exam.demo.networkservice.ApiState
import a.exam.demo.viewmodel.MainActivityVM
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
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

    private lateinit var mMainListAdapter: MainListRecyclerViewAdapter
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
                Utility.checkAdState(recyclerView, mNewsItems)
                onlyForDemo(recyclerView, dx, dy)
            }
        })

        binding.swipeLayout.setOnRefreshListener {
            mMainActivityVM.getNewsList()
        }
    }

    // I hope this app can show view percent, but I can't find better way to solve it now.
    private fun onlyForDemo(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val linearLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()

        val globalVisibleRect = Rect()
        mRecyclerView.getGlobalVisibleRect(globalVisibleRect)
        for (pos in firstPosition..lastPosition) {
            val view = linearLayoutManager.findViewByPosition(pos)
            if (view != null) {
                val percentage = Utility.getVisibleHeightPercentage(view)
                mNewsItems[pos].percent = percentage.toInt()
                mMainListAdapter.notifyDataSetChanged()
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

                        mMainListAdapter = MainListRecyclerViewAdapter(mNewsItems)
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