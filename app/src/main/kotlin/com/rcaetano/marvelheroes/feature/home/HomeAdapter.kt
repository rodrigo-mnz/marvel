package com.rcaetano.marvelheroes.feature.home

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character_home.view.*

private const val ITEM_VIEW_TYPE = 0
private const val LOADING_VIEW_TYPE = 1

class ProposalListAdapter(
    private val loadNextPage: () -> Unit
) :
    RecyclerView.Adapter<ProposalHolder>() {

    private var list: List<Character> = emptyList()
    var showLoadMore = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            ITEM_VIEW_TYPE -> ProposalHolder.ProposalItem(
                parent.inflate(R.layout.item_character_home)
            )

            LOADING_VIEW_TYPE -> ProposalHolder.LoadingItem(parent.inflate(R.layout.item_loading))

            else -> ProposalHolder.ProposalItem(
                parent.inflate(R.layout.item_character_home)
            )
        }

    override fun getItemCount() =
        if (showLoadMore) {
            list.size + 1
        } else {
            list.size
        }

    override fun onBindViewHolder(holder: ProposalHolder, position: Int) {
        when (holder) {
            is ProposalHolder.ProposalItem -> holder.bind(list[position])
            is ProposalHolder.LoadingItem -> loadNextPage()
        }
    }

    override fun getItemViewType(position: Int) =
        if (isLoadMore(position)) {
            LOADING_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }

    fun setData(list: List<Character>) {
        val oldListSize = this.list.size
        val diffSize = list.size - oldListSize

        this.list = list

        val notifyCount = if (showLoadMore) diffSize else diffSize - 1
        notifyItemRangeInserted(oldListSize, notifyCount)
    }

    fun isLoadMore(position: Int) = ((position == (itemCount - 1)) && showLoadMore)
}

sealed class ProposalHolder(view: View) : RecyclerView.ViewHolder(view) {

    class ProposalItem(
        private val view: View
    ) : ProposalHolder(view), View.OnClickListener {

        private var character: Character? = null

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            character?.let {
                //                onItemClick(it)
            }
        }

        fun bind(character: Character) {
            this.character = character

            Log.i("TAG", buildThumbnailUri(character))
            Picasso.get().load(buildThumbnailUri(character))
                .placeholder(R.drawable.portrait_thumb_placeholder)
                .into(view.img_character)

            view.txt_name.text = character.name
        }

        private fun buildThumbnailUri(character: Character) =
            "${character.thumbnail.path}/portrait_xlarge.${character.thumbnail.extension}"
    }

    class LoadingItem(view: View) : ProposalHolder(view)
}
