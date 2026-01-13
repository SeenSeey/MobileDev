package com.example.hoops_mobile_7.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoops_mobile_7.databinding.ItemCharacterBinding;
import com.example.hoops_mobile_7.network.model.character.Character;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private final List<Character> characters = new ArrayList<>();

    public void addItems(List<Character> newItems) {
        int start = characters.size();
        characters.addAll(newItems);
        notifyItemRangeInserted(start, newItems.size());
    }

    public List<Character> getAllCharacters() {
        return characters;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterBinding binding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CharacterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        holder.bind(characters.get(position));
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {

        private final ItemCharacterBinding binding;

        public CharacterViewHolder(ItemCharacterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Character character) {
            binding.nameText.setText(character.getName());
            binding.speciesText.setText(character.getSpecies());
            binding.statusText.setText("Status: " + character.getStatus());
            binding.genderText.setText("Gender: " + character.getGender());

            if (character.getOrigin() != null) {
                binding.originText.setText("Origin: " + character.getOrigin().getName());
            } else {
                binding.originText.setText("Origin: Unknown");
            }

            if (character.getLocation() != null) {
                binding.locationText.setText("Location: " + character.getLocation().getName());
            } else {
                binding.locationText.setText("Location: Unknown");
            }

            Picasso.get()
                    .load(character.getImage())
                    .into(binding.imageView);
        }
    }

    public void setItems(List<Character> newItems) {
        characters.clear();
        characters.addAll(newItems);
        notifyDataSetChanged();
    }
}


