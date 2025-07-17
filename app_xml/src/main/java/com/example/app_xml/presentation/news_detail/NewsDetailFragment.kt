package com.example.app_xml.presentation.news_detail

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.app_xml.R
import com.example.app_xml.databinding.FragmentNewsDetailBinding
import com.example.app_xml.presentation.utils.applyWindowInsets
import com.example.app_xml.presentation.utils.openChromeCustomTab
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

        binding.tvReadMore.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        viewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.tvTitle.text = it.title
                binding.tvDescription.text = it.description
                binding.tvAurhor.text = it.author
                Glide.with(binding.imgNews)
                    .load(article.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(binding.imgNews)

                binding.tvReadMore.setOnClickListener {
                    val url = article.url
                    if (url.isNotEmpty()) {
                        openChromeCustomTab(requireContext(), url)
                    } else {
                        Toast.makeText(requireContext(), "Ссылка недоступна", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}