package com.altomedia.pixeldungeon.items.helmets

import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet

class WizardHat : Helmet() {
    init {
        image = ItemSpriteSheet.WIZARD_HAT
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
}