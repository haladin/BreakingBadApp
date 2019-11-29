package com.exmaple.breakingbadcharacters.characterList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.exmaple.breakingbadcharacters.R
import com.exmaple.breakingbadcharacters.characterDetails.CharacterDetailsActivity
import com.exmaple.breakingbadcharacters.utils.CHARACTER_KEY
import com.exmaple.breakingbadcharacters.utils.DataRequestState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_character_list.*

class CharacterListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var isLoading = false
    private val viewModel = lazy { ViewModelProviders.of(this).get(CharacterListViewModel::class.java) }
    private val characterDataAdapter = lazy { CharacterListAdapter(viewModel.value) }
    private var season = 0
    private var gettingDetails = false
    private var chDetailsStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        rv_characters_list.layoutManager = LinearLayoutManager(this)
        rv_characters_list.setHasFixedSize(true)

        rv_characters_list.adapter = characterDataAdapter.value
        swiperefresh.setOnRefreshListener(this)

        getCharacters()

        et_Filter.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrBlank()) {
                        clear_txt.visibility = View.GONE
                        if (!isLoading) {
                            getCharactersForSeason(season)
                        }
                    } else {
                        clear_txt.visibility = View.VISIBLE
                        getCharactersByName(p0.toString())
                    }
                }
            }
        )
        clear_txt.setOnClickListener {
            et_Filter.text.clear()
        }
        clickDetails()
    }

    override fun onRefresh() {
        if (!this.isLoading) {
            title = if (season == 0){
                getString(R.string.app_name)
            } else {
                "Breaking Bad Season $season"
            }
            et_Filter.text = et_Filter.text
            // getCharactersForSeason(season)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actions_ch_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.refresh-> {
            this.onRefresh()
            true
        }
        R.id.all-> setSeason(item,0)
        R.id.season1 -> setSeason(item,1)
        R.id.season2 -> setSeason(item,2)
        R.id.season3 -> setSeason(item,3)
        R.id.season4 -> setSeason(item,4)
        R.id.season5 -> setSeason(item,5)
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        chDetailsStarted = false
    }

    private fun clickDetails() {
        viewModel.value.openDetails.observe(this,
            Observer { id ->
                // Log.d("StationListActivity", id.toString())
                if (!gettingDetails) {
                    viewModel.value.getCharacterDetails(id).observe(
                        this,
                        Observer {
                            when (it) {
                                is DataRequestState.Start -> {
                                    swiperefresh.isRefreshing = true
                                    gettingDetails = true
                                }
                                is DataRequestState.Success -> {
                                    swiperefresh.isRefreshing = false
                                    gettingDetails = false

                                    if (!chDetailsStarted) {
                                        val intent =
                                            Intent(this, CharacterDetailsActivity::class.java)
                                        intent.putExtra(CHARACTER_KEY, it.data)
                                        startActivity(intent)
                                        chDetailsStarted = true
                                    }
                                    Log.d("CharacterListActivity", "gettingDetails")
                                }
                                is DataRequestState.NoData -> {
                                    swiperefresh.isRefreshing = false
                                    gettingDetails = false
                                    Snackbar.make(
                                        this@CharacterListActivity.rv_characters_list,
                                        it.message,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                is DataRequestState.Error -> {
                                    swiperefresh.isRefreshing = false
                                    gettingDetails = false
                                    Snackbar.make(
                                        this@CharacterListActivity.rv_characters_list,
                                        it.error,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                }
            }
        )
    }

    private fun setSeason(item: MenuItem, season: Int): Boolean {
        item.isChecked = true
        this.season = season
        this.getCharactersForSeason(this.season)
        et_Filter.text = et_Filter.text
        title = if (season == 0){
            getString(R.string.app_name)
        } else {
            "Breaking Bad Season $season"
        }
        return true
    }

    private fun getCharacters(force: Boolean = false) {
        viewModel.value.allCharacters(force).observe(this,
            Observer {
                it?.let {
                    when (it) {
                        is DataRequestState.Start -> {
                            swiperefresh.isRefreshing = true
                            this.isLoading = true
                            tv_empty.visibility = View.GONE
                            Log.d("CharacterListActivity", "Start")
                        }
                        is DataRequestState.Success -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.GONE
                            this.isLoading = false
                            characterDataAdapter.value.setCharactersList(it.data)
                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                "${it.data?.size.toString()} characters returned.",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is DataRequestState.NoData -> {
                            swiperefresh.isRefreshing = false
                            this.isLoading = false
                            tv_empty.visibility = View.VISIBLE
                            Log.d("CharacterListActivity", "getCharacters NoData")

                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.message,
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is DataRequestState.Error -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.VISIBLE
                            this.isLoading = false
                            Log.d("StationListActivity", "getCharacters Error")

                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.error,
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                    }
                }
            })
    }

    private fun getCharactersForSeason(season: Int) {
        viewModel.value.getCharactersFromSeason(season).observe(this,
            Observer {
                it?.let {
                    when (it) {
                        is DataRequestState.Start -> {
                            swiperefresh.isRefreshing = true
                            this.isLoading = true
                            tv_empty.visibility = View.GONE

                        }
                        is DataRequestState.Success -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.GONE
                            this.isLoading = false
                            characterDataAdapter.value.setCharactersList(it.data)
                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                "${it.data?.size.toString()} characters returned.",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is DataRequestState.NoData -> {
                            swiperefresh.isRefreshing = false
                            this.isLoading = false
                            tv_empty.visibility = View.VISIBLE
                            Log.d("CharacterListActivity", "getCharactersForSeason NoData")

                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.message,
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is DataRequestState.Error -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.VISIBLE
                            this.isLoading = false
                            Log.d("StationListActivity", "getCharactersForSeason Error")

                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.error,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
    }

    private fun getCharactersByName(name: String) {
        viewModel.value.getCharactersByName(this.season, name).observe(this,
            Observer {
                it?.let {
                    when (it) {
                        is DataRequestState.Start -> {
                            swiperefresh.isRefreshing = true
                            this.isLoading = true
                            tv_empty.visibility = View.GONE

                        }
                        is DataRequestState.Success -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.GONE
                            this.isLoading = false
                            characterDataAdapter.value.setCharactersList(it.data)
                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                "${it.data?.size.toString()} characters returned.",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is DataRequestState.NoData -> {
                            swiperefresh.isRefreshing = false
                            this.isLoading = false
                            tv_empty.visibility = View.VISIBLE
                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.message,
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is DataRequestState.Error -> {
                            swiperefresh.isRefreshing = false
                            tv_empty.visibility = View.VISIBLE
                            this.isLoading = false

                            Snackbar.make(
                                this@CharacterListActivity.rv_characters_list,
                                it.error,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
    }


}
