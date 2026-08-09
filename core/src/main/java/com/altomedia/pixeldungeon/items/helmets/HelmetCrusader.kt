package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.actors.Char
import com.watabou.utils.Random

class HelmetCrusader : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_CRUSADER
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect_desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }

    override fun procTakenDamage(dmg: Damage) {
        if (dmg.isFeatured(Damage.Feature.RANGED) && Random.Float() < 0.2f)
            dmg.value = 0
    }

    override fun viewAmend(): Int = -1
}