package com.example.app_xml.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_xml.databinding.FragmentNewsfeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsfeedFragment : Fragment() {

    private var _binding: FragmentNewsfeedBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding NewsfeedFragment null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsfeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}