package a.exam.demo.view

import a.exam.coresdk.init.ExamCoreSDK
import a.exam.coresdk.model.AdListener
import a.exam.coresdk.model.AdView
import a.exam.coresdk.network.AdRequest
import a.exam.coresdk.utility.Utility
import a.exam.demo.R
import a.exam.demo.databinding.ActivityMainBinding
import a.exam.demo.model.DemoData
import a.exam.demo.model.MovieResponse
import a.exam.demo.networkservice.ApiState
import a.exam.demo.viewmodel.MainActivityVM
import android.graphics.Rect
import android.os.Bundle
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
    private var mListItems: MutableList<DemoData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ExamCoreSDK.initialize(this, object : ExamCoreSDK.OnInitializationCompleteListener {
            override fun onInitializationComplete(state: ExamCoreSDK.InitState) {
                when (state) {
                    ExamCoreSDK.InitState.SUCCESS -> {
                        initView()
                        initViewModel()
                        collects()
                    }

                    ExamCoreSDK.InitState.FAILED -> {
                        // Maybe re-init
                    }

                    ExamCoreSDK.InitState.OTHER -> {
                        // Maybe check other setting
                    }

                    else -> {}
                }
            }
        })
    }

    override fun onDestroy() {
        Utility.onDestroy()

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
                Utility.checkRecyclerViewAdState(recyclerView, mListItems)
                onlyForDemo(recyclerView)
            }
        })

        binding.swipeLayout.setOnRefreshListener {
            mMainActivityVM.getMovieList()
        }

        val adView = AdView(this, binding.adView)
        adView.setAdSize(320, 100)
        adView.loadAd(AdRequest(), mListener)
    }

    private val mListener = object : AdListener {
        override fun onAdClicked() {

        }

        override fun onAdClosed() {

        }

        override fun onAdFailedToLoad() {

        }

        override fun onAdImpression() {

        }

        override fun onAdLoaded() {

        }

        override fun onAdOpened() {

        }
    }

    // This fun only for demo
    // I hope this app can show view percent, but I couldn't find better way to solve it now.
    private fun onlyForDemo(recyclerView: RecyclerView) {
        val linearLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()

        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)
        for (pos in firstPosition..lastPosition) {
            val view = linearLayoutManager.findViewByPosition(pos)
            if (view != null) {
                val percentage = Utility.getVisibleHeightPercentage(view)
                mListItems[pos].percent = percentage.toInt()
                mMainListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initViewModel() {
        mMainActivityVM = ViewModelProvider(this)[MainActivityVM::class.java]

        mMainActivityVM.getMovieList()
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
                        makeFakeData()
                    }

                    is ApiState.Success -> {
                        binding.swipeLayout.isRefreshing = false

                        val movieResponse = it.data as MovieResponse
                        mListItems.clear()

                        var counter = 0
                        val distance = 2
                        for (item in movieResponse.results) {
                            if (counter == distance) {
                                mListItems.add(DemoData("AD", "This is AD", 100, true))
                                counter = 0
                            }
                            mListItems.add(DemoData(item.title, item.overview, 100, false))
                            ++counter
                        }

                        mMainListAdapter = MainListRecyclerViewAdapter(mListItems)
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

    private fun makeFakeData() {
        for (fakeDataIndex in 0..100) {
            mListItems.clear()

            var counter = 0
            val distance = 2
            for (index in 0..50) {
                if (counter == distance) {
                    mListItems.add(DemoData("AD", "This is AD", 100, true))
                    counter = 0
                }
                mListItems.add(
                    DemoData(
                        getString(R.string.list_fake_title),
                        getString(R.string.list_fake_content),
                        100,
                        false
                    )
                )
                ++counter
            }

            mMainListAdapter = MainListRecyclerViewAdapter(mListItems)
            mRecyclerView.adapter = mMainListAdapter

            mMainListAdapter.notifyDataSetChanged()
        }
    }
}