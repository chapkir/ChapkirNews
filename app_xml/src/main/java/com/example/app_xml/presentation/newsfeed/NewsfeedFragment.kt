package com.example.app_xml.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentNewsfeedBinding
import com.example.app_xml.databinding.ToolbarNewsfeedBinding
import com.example.app_xml.presentation.news_detail.NewsDetailDialogFragment
import com.example.app_xml.presentation.news_detail.NewsDetailSharedViewModel
import com.example.app_xml.presentation.utils.applyWindowInsets
import com.example.app_xml.presentation.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsfeedFragment : Fragment() {

    private var _binding: FragmentNewsfeedBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding NewsfeedFragment null")

    private lateinit var toolbarBinding: ToolbarNewsfeedBinding

    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: NewsfeedViewModel by viewModels()
    private val sharedViewModel: NewsDetailSharedViewModel by activityViewModels()

    private var newsDialog: NewsDetailDialogFragment? = null

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsfeedBinding.inflate(inflater, container, false)
        toolbarBinding = ToolbarNewsfeedBinding.bind(
            binding.appBarNewsfeedLayout.findViewById(R.id.newsfeedToolbar)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbarBinding.newsfeedToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        applyWindowInsets(
            activity = requireActivity(),
            targetView = toolbarBinding.newsfeedToolbar,
            insetTypes = WindowInsetsCompat.Type.statusBars(),
            applyTop = true
        )

        applyWindowInsets(
            activity = requireActivity(),
            targetView = binding.errorBlockLayout,
            insetTypes = WindowInsetsCompat.Type.ime(),
            applyBottom = true
        )

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.newsfeed_toolbar_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = "Поиск новостей"

                val searchEditText = searchView.findViewById<AutoCompleteTextView>(
                    androidx.appcompat.R.id.search_src_text
                )

                with(searchEditText){
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_background))
                    setHintTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface))
                }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchJob?.cancel()
                        searchJob = lifecycleScope.launch {
                            delay(500L)
                            viewModel.onSearchQueryChange(newText.orEmpty())
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        newsAdapter = NewsAdapter(
            onFavoriteClick = { article -> viewModel.toggleFavorite(article) },
            onArticleClick = { article ->
                sharedViewModel.selectArticle(article)
                newsDialog = NewsDetailDialogFragment()
                newsDialog?.show(parentFragmentManager, "NewsDetailDialog")
            }
        )

        binding.recyclerNews.adapter = newsAdapter
        binding.recyclerNews.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    hideKeyboard()
                }
            }
        })

        binding.swipeRefreshNewsfeed.setOnRefreshListener {
            newsAdapter.refresh()
        }

        viewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            newsAdapter.submitData(lifecycle, pagingData)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter.loadStateFlow.collectLatest { loadState ->

                    val isInitialLoading =
                        loadState.source.refresh is LoadState.Loading && newsAdapter.itemCount == 0
                    val isPullToRefresh =
                        loadState.refresh is LoadState.Loading && !isInitialLoading

                    binding.progressBarNewsfeed.isVisible = isInitialLoading
                    binding.swipeRefreshNewsfeed.isRefreshing = isPullToRefresh

                    val refreshError = loadState.refresh as? LoadState.Error
                    if (refreshError != null && newsAdapter.itemCount == 0) {
                        binding.errorBlockLayout.visibility = View.VISIBLE
                        binding.tvErrorMessage.text = getString(R.string.error_load_news)
                    } else {
                        binding.errorBlockLayout.visibility = View.GONE
                    }

                    val isEmpty =
                        loadState.refresh is LoadState.NotLoading && newsAdapter.itemCount == 0
                    if (isEmpty) {
                        binding.errorBlockLayout.visibility = View.VISIBLE
                        binding.tvErrorMessage.text = getString(R.string.error_search_news)
                    }
                }
            }
        }
    }

    fun scrollToTopAndRefresh() {
        binding.recyclerNews.smoothScrollToPosition(0)
        newsAdapter.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}