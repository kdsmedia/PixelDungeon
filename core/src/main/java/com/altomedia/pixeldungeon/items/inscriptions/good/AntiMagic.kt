package com.altomedia.pixeldungeon.items.inscriptions.good

import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.items.EquipableItem
import com.altomedia.pixeldungeon.items.inscriptions.Inscription
import com.altomedia.pixeldungeon.sprites.ItemSprite

class AntiMagic : Inscription() {
    override fun procTakenDamage(equipment: EquipableItem, dmg: Damage) {
        if (dmg.type == Damage.Type.MAGICAL)
            dmg.value = (dmg.value.toFloat() * 0.75f).toInt()
    }
    
    override fun glowing(): ItemSprite.Glowing = ItemSprite.Glowing(0x88eeff) // teal
}