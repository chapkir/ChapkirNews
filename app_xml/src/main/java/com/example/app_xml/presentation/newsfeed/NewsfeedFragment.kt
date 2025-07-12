package com.example.app_xml.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentNewsfeedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsfeedFragment : Fragment() {

    private var _binding: FragmentNewsfeedBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding NewsfeedFragment null")

    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: NewsfeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsfeedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        newsAdapter = NewsAdapter { article ->
            viewModel.toggleFavorite(article)
        }

        binding.recyclerNews.adapter = newsAdapter
        binding.recyclerNews.layoutManager = LinearLayoutManager(requireContext())

        viewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            newsAdapter.submitData(lifecycle, pagingData)
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter.loadStateFlow.collectLatest { loadStates ->
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                }
            }
        }

        viewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            newsAdapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}