package com.rcaetano.marvelheroes.feature.common

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rcaetano.marvelheroes.R
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.feature.favourite.FavouriteHelper
import com.rcaetano.marvelheroes.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*

private const val ITEM_VIEW_TYPE = 0
private const val LOADING_VIEW_TYPE = 1

class CharacterAdapter(
    private val loadNextPage: () -> Unit,
    private val onItemClick: (character: Character, imageView: ImageView) -> Unit
) :
    RecyclerView.Adapter<ItemHolder>() {

    private var list: List<Character> = emptyList()
    var showLoadMore = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            ITEM_VIEW_TYPE -> ItemHolder.CharacterItem(
                parent.inflate(R.layout.item_character),
                onItemClick
            )

            LOADING_VIEW_TYPE -> ItemHolder.LoadingItem(
                parent.inflate(R.layout.item_loading)
            )

            else -> ItemHolder.CharacterItem(
                parent.inflate(R.layout.item_character),
                onItemClick
            )
        }

    override fun getItemCount() =
        if (showLoadMore) {
            list.size + 1
        } else {
            list.size
        }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        when (holder) {
            is ItemHolder.CharacterItem -> holder.bind(list[position])
            is ItemHolder.LoadingItem -> loadNextPage()
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

sealed class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

    class CharacterItem(
        private val view: View,
        private val onItemClick: (character: Character, imageView: ImageView) -> Unit
    ) : ItemHolder(view) {

        fun bind(character: Character) {
            view.setOnClickListener {
                ViewCompat.setTransitionName(view.img_character, "image$adapterPosition")
                onItemClick(character, view.img_character)
            }

            Picasso.get().load(buildThumbnailUri(character))
                .placeholder(R.drawable.portrait_thumb_placeholder)
                .into(view.img_character)

            view.txt_name.text = character.name

            if (FavouriteHelper.isCharacterFavourite(character, view.context)) {
                view.img_favourite.setImageResource(R.drawable.ic_favorite_full)
            } else {
                view.img_favourite.setImageResource(R.drawable.ic_favorite_outline)
            }

            view.img_favourite.setOnClickListener {
                if (FavouriteHelper.isCharacterFavourite(character, view.context)) {
                    FavouriteHelper.removeFavouriteCharacter(character, view.context)
                    view.img_favourite.setImageResource(R.drawable.ic_favorite_outline)
                } else {
                    FavouriteHelper.favouriteCharacter(character, view.context)
                    view.img_favourite.setImageResource(R.drawable.ic_favorite_full)
                }
            }
        }

        private fun buildThumbnailUri(character: Character) =
            "${character.thumbnail.path}/portrait_xlarge.${character.thumbnail.extension}"
    }

    class LoadingItem(view: View) : ItemHolder(view)
}
