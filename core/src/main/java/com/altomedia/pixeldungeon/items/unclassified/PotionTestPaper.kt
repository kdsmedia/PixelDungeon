package com.altomedia.pixeldungeon.items.unclassified

import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.potions.PotionOfLiquidFlame
import com.altomedia.pixeldungeon.items.potions.PotionOfParalyticGas
import com.altomedia.pixeldungeon.items.potions.PotionOfToxicGas
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndBag
import java.util.ArrayList

class PotionTestPaper : Item() {
    init {
        image = ItemSpriteSheet.DPD_TEST_PAPER
        unique = false

        defaultAction = AC_TEST
        stackable = true
    }

    override fun actions(hero: Hero): ArrayList<String> = super.actions(hero).apply { add(AC_TEST) }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)

        if (action == AC_TEST) {
            curUser = hero
            GameScene.selectItem(potionSelector, WndBag.Mode.POTION, Messages.get(this, "prompt"))
        }
    }

    override fun isUpgradable(): Boolean = false
    override fun isIdentified(): Boolean = true
    override fun price(): Int = 12 * quantity

    private fun test(item: Item) {
        if (item.isIdentified)
            GLog.i(Messages.get(this, "tip"))
        else {
            detach(curUser.belongings.backpack)
            if (item is PotionOfLiquidFlame || item is PotionOfToxicGas ||
                    item is PotionOfParalyticGas)
                GLog.w(Messages.get(this, "test_succeed", item.name()))
            else
                GLog.i(Messages.get(this, "test_failed"))
        }
    }

    private val potionSelector = WndBag.Listener {
        if (it != null)
            test(it)
    }

    companion object {
        private const val TIME_TO_TEST = 1f
        private const val AC_TEST = "TEST"

    }
}