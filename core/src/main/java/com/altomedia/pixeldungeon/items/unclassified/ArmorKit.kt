package com.altomedia.pixeldungeon.items.unclassified

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.effects.Speck
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.armor.Armor
import com.altomedia.pixeldungeon.items.armor.ClassArmor
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.HeroSprite
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndBag
import com.watabou.noosa.audio.Sample
import java.util.ArrayList

class ArmorKit : Item() {
    init {
        image = ItemSpriteSheet.KIT
        unique = true
    }

    override fun actions(hero: Hero): ArrayList<String> = super.actions(hero).apply { add(AC_APPLY) }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)
        if (action == AC_APPLY) {
            curUser = hero
            GameScene.selectItem(armorSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt"))
        }
    }

    override fun isUpgradable(): Boolean = false

    override fun isIdentified(): Boolean = true

    private fun upgrade(armor: Armor) {
        detach(curUser.belongings.backpack)

        val classArmor = ClassArmor.upgrade(curUser, armor)
        if (curUser.belongings.armor == armor) {
            curUser.belongings.armor = classArmor
            (curUser.sprite as HeroSprite).updateArmor()
        } else {
            armor.detach(curUser.belongings.backpack)
            classArmor.collect(curUser.belongings.backpack)
        }

        GLog.w(Messages.get(this, "upgraded", armor.name()))
        with(curUser) {
            sprite.centerEmitter().start(Speck.factory(Speck.KIT), 0.05f, 10)
            spend(10f)
            busy()
            sprite.operate(pos)
        }

        Sample.INSTANCE.play(Assets.SND_EVOKE)
    }

    private val armorSelector = WndBag.Listener {
        if (it != null)
            upgrade(it as Armor)
    }

    companion object {
        private const val TIME_TO_UPGRADE = 2f
        private const val AC_APPLY = "APPLY"
    }

}
