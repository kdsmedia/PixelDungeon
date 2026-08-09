package com.altomedia.pixeldungeon.items.unclassified

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.helmets.MaskOfMadness
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog

import java.util.ArrayList

/**
 * Created by 93942 on 6/18/2018.
 */

class DemonicSkull : Item() {

    init {
        image = ItemSpriteSheet.DEMONIC_SKULL

        levelKnown = true
        cursedKnown = levelKnown
        unique = true
    }

    override fun isUpgradable(): Boolean = false

    override fun actions(hero: Hero): ArrayList<String> {
        val actions = super.actions(hero)
        
        hero.belongings.getItem(UnholyBlood::class.java)?.run { 
            actions.add(AC_SMEAR)
        }

        return actions
    }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)

        if (action === AC_SMEAR) {
            detach(hero.belongings.backpack)
            hero.belongings.getItem(UnholyBlood::class.java)!!.detach(hero.belongings.backpack)

            val mom = MaskOfMadness()
            mom.identify().collect()

            GLog.w(Messages.get(Dungeon.hero, "you_now_have", mom.name()))
        }
    }

    companion object {

        private const val AC_SMEAR = "SMEAR"
    }
}
