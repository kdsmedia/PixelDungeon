package com.altomedia.pixeldungeon.items.food

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Badges
import com.altomedia.pixeldungeon.Statistics
import com.altomedia.pixeldungeon.actors.buffs.Hunger
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.hero.HeroLines
import com.altomedia.pixeldungeon.actors.hero.perks.GoodAppetite
import com.altomedia.pixeldungeon.effects.BubbleText
import com.altomedia.pixeldungeon.effects.SpellSprite
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Random
import java.util.ArrayList

open class Food(val enery: Float = Hunger.HUNGRY,
                val hornValue: Int = 3) : Item() {
    init {
        stackable = true
        image = ItemSpriteSheet.RATION

        bones = true
        defaultAction = AC_EAT
    }

    val message: String = Messages.get(this, "eat_msg")

    override fun actions(hero: Hero?): ArrayList<String> = super.actions(hero).apply { add(AC_EAT) }

    override fun execute(hero: Hero, action: String) {
        super.execute(hero, action)

        if (action == AC_EAT) {
            detach(hero.belongings.backpack)
            hero.buff(Hunger::class.java)!!.satisfy(enery)

            hero.heroPerk.get(GoodAppetite::class.java)?.onFoodEaten(hero, this)

            hero.recoverSanity(Random.Float(2f, 7f))
            hero.sprite.operate(hero.pos)
            hero.busy()
            SpellSprite.show(hero, SpellSprite.FOOD)
            Sample.INSTANCE.play(Assets.SND_EAT)

            hero.spend(TIME_TO_EAT)

            Statistics.FoodEaten++
            Badges.validateFoodEaten()

            hero.say(message)
        }
    }

    override fun isUpgradable(): Boolean = false

    override fun isIdentified(): Boolean = true

    override fun price(): Int = 10 * quantity

    companion object {
        private const val TIME_TO_EAT = 3f

        const val AC_EAT = "EAT"
    }

}