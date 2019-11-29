package com.exmaple.breakingbadcharacters.characterDetails

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.exmaple.breakingbadcharacters.R
import com.exmaple.breakingbadcharacters.data.Character
import com.exmaple.breakingbadcharacters.databinding.ActivityCharacterDetailsBinding
import com.exmaple.breakingbadcharacters.utils.CHARACTER_KEY


class CharacterDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_character_details)

        //this.charID = intent.getIntExtra(CHARACTER_KEY, 0)


        // Log.d("CharacterDetails", character.toString())


        val character = intent.getParcelableExtra<Character>(CHARACTER_KEY)
        val binding = DataBindingUtil.setContentView<ActivityCharacterDetailsBinding>(this, R.layout.activity_character_details)
        binding.character = character

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}
