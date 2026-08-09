package com.altomedia.pixeldungeon.items.weapon.missiles

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.blobs.Blob
import com.altomedia.pixeldungeon.actors.blobs.SmokeOfBlindness
import com.altomedia.pixeldungeon.actors.buffs.Blindness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Random

class 
SmokeSparks : MissileWeapon(1) {
    init {
        image = ItemSpriteSheet.SMOKE_SPARKS
    }
    
    override fun onThrow(cell: Int) {
        Sample.INSTANCE.play(Assets.SND_BLAST)
        GameScene.add(Blob.seed(cell, 80, SmokeOfBlindness::class.java))
    }

    override fun proc(dmg: Damage): Damage {
        Buff.affect(dmg.to as Char, Blindness::class.java, 3f)
        dmg.addFeature(Damage.Feature.ACCURATE) // cannot miss
        return super.proc(dmg)
    }

    override fun random(): Item {
        quantity = Random.Int(3, 5)
        return this
    }
}