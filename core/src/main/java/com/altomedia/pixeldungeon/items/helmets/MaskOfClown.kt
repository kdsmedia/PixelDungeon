package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.items.inscriptions.good.AntiMagic
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class MaskOfClown : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_CLOWN
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }

    override fun procGivenDamage(dmg: Damage) {
        if (!cursed && Random.Float() < 0.1f)
            dmg.addFeature(Damage.Feature.PURE)
    }
}