package com.exmaple.breakingbadcharacters.characterList

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses


@RunWith(Suite::class)
@SuiteClasses(CharacterListActivityTest::class,
                CharacterListActivitySeasonTest::class,
                CharacterListActivityFilterTest::class,
                CharacterDetailsActivityTest::class)
class BBTests_start