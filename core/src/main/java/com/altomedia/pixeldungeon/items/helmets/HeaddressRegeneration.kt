package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class HeaddressRegeneration: Helmet() {
    init {
        image = ItemSpriteSheet.HEADDRESS_OF_REGENERATION
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(this, "cursed_desc")
        }

        return desc
    }
}