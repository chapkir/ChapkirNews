package com.example.app_xml.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentNewsfeedBinding
import com.example.app_xml.databinding.ToolbarNewsfeedBinding
import com.example.app_xml.presentation.news_detail.NewsDetailDialogFragment
import com.example.app_xml.presentation.news_detail.NewsDetailSharedViewModel
import com.example.app_xml.presentation.utils.applyWindowInsets
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.newsfeed_toolbar_menu, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = "Поиск новостей"

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

        viewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            newsAdapter.submitData(lifecycle, pagingData)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter.loadStateFlow.collectLatest { loadStates ->
                    binding.progressBarNewsfeed.isVisible = loadStates.refresh is LoadState.Loading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}