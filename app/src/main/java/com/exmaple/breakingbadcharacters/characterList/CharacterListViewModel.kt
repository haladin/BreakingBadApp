package com.exmaple.breakingbadcharacters.characterList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.exmaple.breakingbadcharacters.data.Character
import com.exmaple.breakingbadcharacters.repo.BreakingBadRepository
import com.exmaple.breakingbadcharacters.utils.DataRequestState

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {

    val openDetails = MutableLiveData<Int>()

    fun allCharacters(force: Boolean): MutableLiveData<DataRequestState<List<Character>?>> {
        return BreakingBadRepository.getCharactersLiveData(this.getApplication(), force)
    }

    fun getCharactersFromSeason(season: Int = 1): MutableLiveData<DataRequestState<List<Character>?>> {
        return BreakingBadRepository.getCharactersFromSeason(this.getApplication(), season)
    }

    fun getCharactersByName(season: Int = 1, name: String): MutableLiveData<DataRequestState<List<Character>?>> {
        return BreakingBadRepository.getCharactersByName(season, name)
    }

    fun openDetails(charId: Int){
        openDetails.value = charId
    }

    fun getCharacterDetails(charId: Int): MutableLiveData<DataRequestState<Character?>> {
        return BreakingBadRepository.getCharacterById(charId)
    }

}