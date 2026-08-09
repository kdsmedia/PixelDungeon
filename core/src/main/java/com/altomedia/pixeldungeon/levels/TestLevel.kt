package com.altomedia.pixeldungeon.levels

import com.altomedia.pixeldungeon.levels.diggers.Digger
import com.altomedia.pixeldungeon.levels.diggers.specials.CryptDigger
import com.altomedia.pixeldungeon.levels.diggers.specials.GardenDigger
import com.altomedia.pixeldungeon.levels.diggers.specials.MagicWellDigger
import com.altomedia.pixeldungeon.levels.diggers.specials.WandDigger

class TestLevel : SewerLevel() {
    override fun chooseDiggers(): ArrayList<Digger> {
        val diggers = selectDiggers(0, 10)
        // diggers.add(WandDigger())
        // diggers.add(CryptDigger())
        // diggers.add(GardenDigger())
//        diggers.add(MagicWellDigger())
//        diggers.add(MagicWellDigger())
        
        return diggers
    }
}