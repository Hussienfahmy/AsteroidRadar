package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), MenuProvider {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MainFragment.viewModel

            asteroidRecycler.adapter =
                AsteroidAdapter(AsteroidAdapter.OnAsteroidClickListener { asteroid ->
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                })

            swipeRefresh.setOnRefreshListener {
                lifecycleScope.launch {
                    this@MainFragment.viewModel.refreshNextWeekData()
                    swipeRefresh.isRefreshing = false
                }
            }
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.show_week_menu -> {
                viewModel.setFilter(AsteroidFilter.WEEK)
                true
            }
            R.id.show_today_menu -> {
                viewModel.setFilter(AsteroidFilter.TODAY)
                true
            }
            R.id.show_saved_menu -> {
                viewModel.setFilter(AsteroidFilter.SAVED)
                true
            }
            else -> false
        }
    }
}