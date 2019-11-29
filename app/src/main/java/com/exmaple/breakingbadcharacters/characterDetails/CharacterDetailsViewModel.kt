package com.exmaple.breakingbadcharacters.characterDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.exmaple.breakingbadcharacters.data.Character
import com.exmaple.breakingbadcharacters.repo.BreakingBadRepository
import com.exmaple.breakingbadcharacters.utils.DataRequestState

class CharacterDetailsViewModel(application: Application) : AndroidViewModel(application) {

    fun getCharacterDetails(charId: Int): MutableLiveData<DataRequestState<Character?>> {
        return BreakingBadRepository.getCharacterById(charId)
    }
}