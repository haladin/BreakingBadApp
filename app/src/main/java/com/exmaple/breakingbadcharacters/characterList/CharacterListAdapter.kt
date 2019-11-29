package com.exmaple.breakingbadcharacters.characterList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exmaple.breakingbadcharacters.data.Character
import com.exmaple.breakingbadcharacters.databinding.CharacterItemBinding

class CharacterListAdapter(private val viewModel: CharacterListViewModel): RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

        private var characters: List<Character>? = null

        override fun getItemCount(): Int {
            return characters?.size ?: 0
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.from(viewGroup)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            characters?.let {
                val item = it[position]
                holder.bind(viewModel, item)
            }
        }

        fun setCharactersList(trains: List<Character>?) {
            this.characters = trains
            notifyDataSetChanged()
        }

        class ViewHolder private constructor(val binding: CharacterItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: CharacterListViewModel, character: Character) {
                binding.character = character
                binding.viewmodel = viewModel
                binding.executePendingBindings()
            }

            companion object {
                fun from(parent: ViewGroup): ViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = CharacterItemBinding.inflate(layoutInflater, parent, false)

                    return ViewHolder(binding)
                }
            }
        }

}