package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class MaskOfHorror : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_HORROR
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(this, "cursed-desc")
        }

        return desc
    }

    override fun procGivenDamage(dmg: Damage) {
        if (!cursed && Random.Float() < 0.1f)
            dmg.addFeature(Damage.Feature.ACCURATE)
    }
}