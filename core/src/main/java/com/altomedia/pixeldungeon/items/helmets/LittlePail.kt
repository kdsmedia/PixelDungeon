package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class LittlePail : Helmet() {
    init {
        image = ItemSpriteSheet.LITTLE_PAIL
    }

    override fun procTakenDamage(dmg: Damage) {
        dmg.value -= Random.NormalIntRange(0, 2)
    }

    override fun random(): Item = this.apply { cursed = Random.Float() < 0.1f }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }
}