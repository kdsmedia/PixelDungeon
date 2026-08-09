package com.altomedia.pixeldungeon.items.unclassified

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.artifacts.CloakOfShadows
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.items.weapon.missiles.Boomerang
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndBag
import com.watabou.noosa.audio.Sample
import java.util.ArrayList

class GreatBlueprint : Item() {
    init {
        image = ItemSpriteSheet.GREAT_BLUEPRINT

        unique = true
    }

    override fun isUpgradable(): Boolean = false

    override fun isIdentified(): Boolean = true

    override fun actions(hero: Hero): ArrayList<String> = super.actions(hero).apply { add(AC_USE) }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)

        if (action == AC_USE) {
            curUser = hero

            GameScene.selectItem(itemSelector, M.L(this, "prompt"), filter)
        }
    }

    // todo...
    private val filter = WndBag.Filter { it is Enchantable }

    private val itemSelector = WndBag.Listener {
        if (it != null && filter.enable(it)) {
            enhance(it)
        }
    }

    private fun enhance(item: Item) {
        detach(Dungeon.hero.belongings.backpack)

        // enhance
        (item as Enchantable).enchantByBlueprint()

        GLog.w(M.L(this, "enhanced", item.name()))
        with(curUser) {
            sprite.centerEmitter().start(Speck.factory(Speck.KIT), 0.05f, 10)
            spend(10f)
            busy()
            sprite.operate(pos)
        }
        Sample.INSTANCE.play(Assets.SND_EVOKE)
        updateQuickslot()
    }

    companion object {
        private const val AC_USE = "USE"
    }
    
    interface Enchantable{
        fun enchantByBlueprint()
    }

}