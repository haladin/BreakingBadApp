package com.exmaple.breakingbadcharacters.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.exmaple.breakingbadcharacters.data.Character
import com.exmaple.breakingbadcharacters.utils.DataRequestState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object BreakingBadRepository {
    private const val baseURL = "https://breakingbadapi.com/api/"
    private val mutableCharactersListLiveData = MutableLiveData<DataRequestState<List<Character>?>>()
    private val mutableCharactersListSeasonLiveData = MutableLiveData<DataRequestState<List<Character>?>>()
    private val mutableCharactersListNameLiveData = MutableLiveData<DataRequestState<List<Character>?>>()
    private val mutableCharacterByIdLiveData = MutableLiveData<DataRequestState<Character?>>()
    private var characterListCache: List<Character>? = null

    fun getCharactersLiveData(context: Context, force: Boolean): MutableLiveData<DataRequestState<List<Character>?>> {
        mutableCharactersListLiveData.value = DataRequestState.Start()

        if (this.characterListCache == null || force) {
            val dataService = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Client.getClient(context))
                .build()

            val call = dataService.create(BreakingBadService::class.java).getCharacters()

            call.enqueue(object : Callback<List<Character>> {
                override fun onResponse(
                    call: Call<List<Character>?>,
                    response: Response<List<Character>?>
                ) {
                    val charList: List<Character>? = response.body()
                    charList?.let {
                        mutableCharactersListLiveData.value = DataRequestState.Success(charList)
                        //val data = it.filter { station -> station.stationLatitude != 0f && station.stationLongitude != 0f}
                        if (charList.isNotEmpty()) {
                            mutableCharactersListLiveData.value = DataRequestState.Success(charList)
                            characterListCache = charList
                        } else {
                            mutableCharactersListLiveData.value =
                                DataRequestState.NoData("No data.")
                        }
                    } ?: run {
                        mutableCharactersListLiveData.value = DataRequestState.NoData("No data.")
                    }
                }
                override fun onFailure(p0: Call<List<Character>>?, t: Throwable) {
                    mutableCharactersListLiveData.value = DataRequestState.Error("Network error.")
                }
            })
        } else {
            mutableCharactersListLiveData.value = DataRequestState.Success(this.characterListCache)
        }
        return mutableCharactersListLiveData
    }

    fun getCharactersFromSeason(context: Context, season: Int): MutableLiveData<DataRequestState<List<Character>?>>{

        if (this.characterListCache == null ) {
            val dataService = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Client.getClient(context))
                .build()

            val call = dataService.create(BreakingBadService::class.java).getCharacters()

            call.enqueue(object : Callback<List<Character>> {
                override fun onResponse(
                    call: Call<List<Character>?>,
                    response: Response<List<Character>?>
                ) {
                    val charList: List<Character>? = response.body()
                    charList?.let {
                        if (charList.isNotEmpty()) {
                            characterListCache = charList
                            mutableCharactersListSeasonLiveData.value = DataRequestState.Success(filterData(season, charList))
                            Log.d("tttt", charList.toString())
                        } else {
                            mutableCharactersListSeasonLiveData.value = DataRequestState.NoData("No data.")
                        }
                    } ?: run {
                        mutableCharactersListSeasonLiveData.value = DataRequestState.NoData("No data.")
                    }
                }
                override fun onFailure(p0: Call<List<Character>>?, t: Throwable) {
                    mutableCharactersListSeasonLiveData.value = DataRequestState.Error("Network error.")
                }
            })
        } else {
            Log.d("tttt", characterListCache.toString())
            this.characterListCache?.let {
                mutableCharactersListSeasonLiveData.value =
                    DataRequestState.Success(filterData(season, it))
                Log.d("tttt", it.toString())
            }
        }
        Log.d("tttt", season.toString())
        return mutableCharactersListSeasonLiveData
    }

    fun getCharactersByName(season: Int, name: String): MutableLiveData<DataRequestState<List<Character>?>>{
        characterListCache?.let {

            val data = filterData(season, it).filter { character -> character.name?.let{itName -> itName.toLowerCase().contains(name.toLowerCase())}?: run { false } }
            if (data.isNotEmpty()){
                mutableCharactersListNameLiveData.value = DataRequestState.Success(data)
            } else {
                mutableCharactersListNameLiveData.value = DataRequestState.NoData("Not found.")
            }
        } ?: run {
            mutableCharactersListNameLiveData.value = DataRequestState.NoData("Not found.")
        }
        return mutableCharactersListNameLiveData
    }

    fun getCharacterById(charId: Int): MutableLiveData<DataRequestState<Character?>>{
        characterListCache?.let {

            val data = it.filter { character -> character.charId?.let{id -> id == charId}?: run { false }}
            if (data.isNotEmpty()){
                mutableCharacterByIdLiveData.value = DataRequestState.Success(data[0])
                Log.d("hhhh",data[0].getSeasons())
            } else {
                mutableCharacterByIdLiveData.value = DataRequestState.NoData("Not found.")
            }
        } ?: run {
            mutableCharacterByIdLiveData.value = DataRequestState.NoData("Not found.")
        }
        return mutableCharacterByIdLiveData
    }

    private fun filterData(season: Int, data: List<Character>): List<Character>{
        return if (season == 0) {
            data
        } else {
            data.filter { it.appearance?.let{item -> item.contains(season)} ?: run { false }}
        }
    }
}