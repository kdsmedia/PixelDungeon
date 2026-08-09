package com.altomedia.pixeldungeon.items.weapon.curses

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Pressure
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.weapon.Weapon
import com.altomedia.pixeldungeon.sprites.ItemSprite
import com.watabou.utils.Random

/**
 * Created by 93942 on 10/13/2018.
 */

class Arrogant : Weapon.Enchantment() {

    override fun proc(weapon: Weapon, damage: Damage): Damage {
        if (Random.Int(10) == 0)
            (damage.from as Char).buff(Pressure::class.java)?.upPressure(Random.Int(1, 3).toFloat())
        
        return damage
    }

    override fun curse(): Boolean = true

    override fun glowing(): ItemSprite.Glowing = BLACK

    companion object {
        private val BLACK = ItemSprite.Glowing(0x000000)
    }
}
