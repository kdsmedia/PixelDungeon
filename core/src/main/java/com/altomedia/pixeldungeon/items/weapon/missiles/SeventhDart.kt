package com.altomedia.pixeldungeon.items.weapon.missiles

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Bleeding
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class SeventhDart(number: Int = 1) : MissileWeapon(3) {
    init {
        image = ItemSpriteSheet.DART_SEVENTH

        quantity = number
    }

    override fun max(lvl: Int): Int = 2 + 2 * tier

    override fun breakChance(): Float = super.breakChance() * 0.8f

    override fun proc(dmg: Damage): Damage {
        Buff.affect(dmg.to as Char, Bleeding::class.java).set((dmg.to as Char).HT / 10)
        return super.proc(dmg)
    }
}