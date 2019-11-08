package com.rcaetano.marvelheroes.feature.favourite

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.rcaetano.marvelheroes.data.model.Character
import com.rcaetano.marvelheroes.data.model.Data

object FavouriteHelper {

    private const val SHARED_PREFERENCES = "MarvelSharedPreferences"
    private const val KEY_FAVOURITE = "KEY_FAVOURITE"

    @SuppressLint("ApplySharedPref")
    fun favouriteCharacter(character: Character, context: Context?) {

        if (context == null) return

        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (!sharedPref.contains(KEY_FAVOURITE)) {
            val list = arrayListOf(character)
            val data = Data(0, 0, 0, list, 0)
            editor.putString(KEY_FAVOURITE, Gson().toJson(data))
        } else {
            val savedJson = sharedPref.getString(KEY_FAVOURITE, "")

            val data: Data = Gson().fromJson(savedJson, Data::class.java)

            if (!data.results.any { it.id == character.id }) {
                data.results.add(character)
                editor.putString(KEY_FAVOURITE, Gson().toJson(data))
            }
        }

        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun removeFavouriteCharacter(character: Character, context: Context?) {

        if (context == null) return

        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (!sharedPref.contains(KEY_FAVOURITE)) {
            val list = ArrayList<Character>()
            val data = Data(0, 0, 0, list, 0)
            editor.putString(KEY_FAVOURITE, Gson().toJson(data))
        } else {
            val savedJson = sharedPref.getString(KEY_FAVOURITE, "")
            val data: Data = Gson().fromJson(savedJson, Data::class.java)
            val index = data.results.indexOfFirst { it.id == character.id }

            if (index != -1) {
                data.results.removeAt(index)
                editor.putString(KEY_FAVOURITE, Gson().toJson(data))
            }
        }

        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun getFavourites(context: Context?): List<Character> {

        if (context == null) return ArrayList<Character>()

        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        return if (!sharedPref.contains(KEY_FAVOURITE)) {
            val editor = sharedPref.edit()
            val list = ArrayList<Character>()
            val data = Data(0, 0, 0, list, 0)
            editor.putString(KEY_FAVOURITE, Gson().toJson(data))
            editor.commit()
            list
        } else {
            val savedJson = sharedPref.getString(KEY_FAVOURITE, "")
            val data: Data = Gson().fromJson(savedJson, Data::class.java)
            data.results
        }
    }

    fun isCharacterFavourite(character: Character, context: Context?): Boolean {

        if (context == null) return false

        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        return if (!sharedPref.contains(KEY_FAVOURITE)) {
            false
        } else {
            val savedJson = sharedPref.getString(KEY_FAVOURITE, "")
            val data: Data = Gson().fromJson(savedJson, Data::class.java)
            data.results.firstOrNull { it.id == character.id } != null
        }
    }
}