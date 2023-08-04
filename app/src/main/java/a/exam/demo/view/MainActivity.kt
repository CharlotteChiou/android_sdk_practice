package a.exam.demo.view

import a.exam.demo.databinding.ActivityMainBinding
import a.exam.demo.model.Articles
import a.exam.demo.model.NewsResponse
import a.exam.demo.networkservice.ApiState
import a.exam.demo.viewmodel.MainActivityVM
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

    private lateinit var mMainListAdapter: MainListRecyclerView
    private var items = arrayListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
        collects()
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


        binding.swipeLayout.setOnRefreshListener {
            mMainActivityVM.getNewsList()
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
                        items.clear()
                        items.addAll(myObj.articles)
                        mMainListAdapter = MainListRecyclerView(items)
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