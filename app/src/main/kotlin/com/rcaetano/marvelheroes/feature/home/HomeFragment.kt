package com.rcaetano.marvelheroes.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.subscribe
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private val adapter = ProposalListAdapter(::loadNextPage)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupLiveData()
    }

    private fun setupLiveData() {
        viewModel.characterList.subscribe(this) { characterList ->
            adapter.setData(characterList)
            adapter.notifyDataSetChanged()
        }

        viewModel.shouldLoadMore.subscribe(this) { shouldLoadMore ->
            adapter.showLoadMore = shouldLoadMore
        }

        viewModel.fetchCharacters()
    }

    private fun setupViews() {
        val manager = GridLayoutManager(context, 4)
        recycler_view.layoutManager = manager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                if (adapter.isLoadMore(position)) 4 else 1
        }
    }

    private fun loadNextPage() {
        viewModel.loadNextPage()
    }
}
