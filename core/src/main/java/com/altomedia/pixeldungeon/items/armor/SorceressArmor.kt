package com.altomedia.pixeldungeon.items.armor

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Invisibility
import com.altomedia.pixeldungeon.actors.buffs.Terror
import com.altomedia.pixeldungeon.actors.buffs.Venom
import com.altomedia.pixeldungeon.effects.Flare
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.audio.Sample

class SorceressArmor : ClassArmor() {
    init {
        image = ItemSpriteSheet.DPD_ARMOR_SORCERESS
    }

    override fun doSpecial() {
        for (mob in Dungeon.level.mobs) {
            if (Level.fieldOfView[mob.pos] && Dungeon.level.distance(mob.pos, Item.curUser.pos) < 8) {
                Venom().apply {
                    set(5f, Item.curUser.giveDamage(mob).value / 5 + 2)
                }.attachTo(mob)

                Buff.affect(mob, Terror::class.java, 3f).`object` = Item.curUser.id()
            }
        }

        Flare(8, 48f).color(0xff0000, true).show(Item.curUser.sprite, 3f)
        Sample.INSTANCE.play(Assets.SND_DEGRADE)
        Invisibility.dispel()

        Item.curUser.HP -= Item.curUser.HP / 3
        Item.curUser.spendAndNext(Actor.TICK)
    }
}