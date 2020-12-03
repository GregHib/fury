package com.fury

import com.fury.game.GameSettings

object Config {
    const val DEBUG = true
    const val TEST = false
    const val MAX_PLAYERS = 2048
    const val MAX_MOBS = 10240

    var BUILD_DIR = if(GameSettings.HOSTED) "./build/classes/" else "./out/production/classes/"
}