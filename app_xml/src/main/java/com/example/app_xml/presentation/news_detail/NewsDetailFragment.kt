package com.example.app_xml.presentation.news_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentFavoritesBinding
import com.example.app_xml.databinding.FragmentNewsDetailBinding
import com.example.app_xml.databinding.ToolbarFavoritesBinding
import com.example.app_xml.presentation.utils.applyWindowInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val viewModel: NewsDetailSharedViewModel by activityViewModels()

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding NewsDetailFragment null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyWindowInsets(
            activity = requireActivity(),
            targetView = binding.fragmentNewsDetail,
            insetTypes = WindowInsetsCompat.Type.statusBars(),
            applyTop = true
        )

        viewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.tvTitle.text = it.title
                binding.tvDescription.text = it.description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}