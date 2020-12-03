package com.fury.game.world.map

object Area {
    val list = listOf(
            RectangleArea(3200, 3840, 3390, 3967),//Wilderness
            RectangleArea(2835, 5905, 2880, 5950),
            RectangleArea(2835, 5905, 2880, 5950),
            RectangleArea(2992, 3912, 3007, 3967),
            RectangleArea(2946, 3816, 2959, 3831),
            RectangleArea(3008, 3856, 3199, 3903),
            RectangleArea(3008, 3600, 3071, 3711),
            RectangleArea(3270, 3532, 3346, 3625),
            RectangleArea(2965, 3904, 3050, 3959),//End wilderness
            RectangleArea(2815, 5240, 2966, 5375),
            RectangleArea(2840, 5190, 2950, 5230),//Godwars
            RectangleArea(3547, 9690, 3555, 9699),//Zaros
            RectangleArea(2250, 4675, 2292, 4710),//King Black Dragon lai
            RectangleArea(2560, 5710, 2630, 5753),//Tormented demon's ar
            RectangleArea(2970, 4365, 3000, 4400),//Corporeal beast altar
            RectangleArea(2879, 4368, 2962, 4406),//Corporeal beast
            RectangleArea(3136, 3520, 3327, 3970),
            //RectangleArea(2376, 2422, 5127, 5168),
            //RectangleArea(2374, 2424, 5129, 5168),//Pits
            RectangleArea(2368, 2431, 3072, 3135),//Castlewars
            RectangleArea(3526, 5185, 3550, 5215),//Phoenix
            RectangleArea(2368, 3072, 2431, 3135),//Castle wars
            RectangleArea(2365, 9470, 2436, 9532),//Castlewars dungeon
            RectangleArea(3462, 9481, 3511, 9521),//Kalphite queen
            RectangleArea(3261, 3136, 3328, 3200),//Al kharid
            RectangleArea(3081, 3140, 3128, 3175),//Wizards tower
            RectangleArea(3109, 3233, 3135, 3263),//Draynor village
            RectangleArea(3152, 2960, 3190, 2999),//Bandit camp
            RectangleArea(2793, 3432, 2879, 3528),//White wolf mountain
            RectangleArea(2930, 3512, 2943, 3519),//Jug of wine place
            RectangleArea(2928, 3306, 3068, 3393),//Falador
            RectangleArea(3072, 3392, 3135, 3455),//Barbarian village pt1
            RectangleArea(3056, 3384, 3072, 3440),//Barbarian village pt2
            RectangleArea(2688, 2688, 2816, 2816),//Ape Atoll
            RectangleArea(2649, 3408, 2687, 3448),//Ranging Guild
            RectangleArea(2688, 9088, 2815, 9151),//Chaos Tunnels
            RectangleArea(2891, 4427, 2937, 4472),//Dks
            RectangleArea(1721, 5123, 1791, 5249),//mole
            RectangleArea(2802, 3526, 2848, 3932),//Death plateau
            RectangleArea(1488, 4694, 1520, 4714),//Dwarf battlefields
            RectangleArea(3015, 10061, 3133, 10164),//Forinthry dungeon
            RectangleArea(2440, 10122, 2551, 10169),//Rock lobster area
            RectangleArea(2502, 10006, 2538, 10039),//Lighthouse dagannoths
            RectangleArea(3010, 4802, 3070, 4861)//Abyss */
    )

    @JvmStatic
    fun isMulti(tile: Position): Boolean {
        return list.any { tile.x in (it.nwX)..(it.seX) && tile.y in (it.nwY)..(it.seY) }
    }
}