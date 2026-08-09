package com.altomedia.pixeldungeon.items.artifacts

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog

import java.util.ArrayList

/**
 * Created by 93942 on 9/21/2018.
 */

// check Gold::doPickUp
class GoldPlatedStatue : Artifact() {
    init {
        image = ItemSpriteSheet.GOLD_PLATE_STATUE

        levelCap = 10
    }

    override fun actions(hero: Hero): ArrayList<String> {
        val actions = super.actions(hero)
        if (level() < levelCap && !cursed)
            actions.add(AC_INVEST)

        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == AC_INVEST && level() < levelCap) {
            if (!isEquipped(hero))
                GLog.i(Messages.get(Artifact::class.java, "need_to_equip"))
            else if (cursed)
                GLog.i(Messages.get(this, "cursed"))
            else {
                val goldRequired = (100 * Math.pow(1.27, level().toDouble())).toInt()
                if (Dungeon.gold < goldRequired)
                    GLog.w(Messages.get(GoldPlatedStatue::class.java, "no_enough_gold",
                            goldRequired))
                else {
                    Dungeon.gold -= goldRequired

                    upgrade()
                    GLog.p(Messages.get(GoldPlatedStatue::class.java, "levelup", goldRequired))
                }
            }
        }
    }

    override fun desc(): String {
        var desc = super.desc()

        if (isEquipped(Dungeon.hero)) {
            if (!cursed) {
                if (level() < levelCap)
                    desc += "\n\n" + Messages.get(this, "desc_hint")
            } else {
                desc += "\n\n" + Messages.get(this, "desc_cursed")
            }
        }

        return desc
    }

    override fun passiveBuff(): Artifact.ArtifactBuff? = Greedy()

    inner class Greedy : Artifact.ArtifactBuff() {
        fun extraCollect(gold: Int): Int {
            val ratio = if (cursed) -.3f else level() * .1f

            return (gold * ratio).toInt()
        }
    }

    companion object {
        private const val AC_INVEST = "INVEST"
    }
}
