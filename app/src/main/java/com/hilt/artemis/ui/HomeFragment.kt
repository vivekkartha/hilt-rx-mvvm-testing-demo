package com.hilt.artemis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hilt.artemis.R
import com.hilt.artemis.data.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

       private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.home_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFetchButtonClickListener()
        observeOnResponse()
    }

    private fun setFetchButtonClickListener() {
        btnFetchAll.setOnClickListener {
            makeRequests()
        }
    }

    private fun makeRequests() {
        homeViewModel.request10thCharacter()
        homeViewModel.requestEvery10thCharacter()
        homeViewModel.requestWordCounter()
    }

    private fun observeOnResponse() {
        listenOn10thCharacter()
        listenOnEvery10thCharacter()
        listenOnWordCounter()
    }

    private fun listenOn10thCharacter() {
        homeViewModel.tenthCharacterLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> tvTenthChar.text = getString(R.string.loading)

                is Resource.Loaded -> tvTenthChar.text =
                    getString(R.string.tenth_char, result.data.toString())

                is Resource.Failed -> tvTenthChar.text = result.error
            }
        })
    }

    private fun listenOnEvery10thCharacter() {
        homeViewModel.every10thLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> tvEveryTenth.text = getString(R.string.loading)

                is Resource.Loaded -> tvEveryTenth.text =
                    getString(R.string.every_tenth_char, result.data.toString())

                is Resource.Failed -> tvEveryTenth.text = result.error
            }
        })
    }

    private fun listenOnWordCounter() {
        homeViewModel.wordCounterLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> tvWordCounter.text = getString(R.string.loading)

                is Resource.Loaded -> tvWordCounter.text =
                    getString(R.string.word_counter, result.data.toString())

                is Resource.Failed -> tvWordCounter.text = result.error
            }
        })
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
