package com.altomedia.pixeldungeon.actors.mobs.npcs

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.items.helmets.CrownOfDwarf
import com.altomedia.pixeldungeon.items.unclassified.GreatBlueprint
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.CharSprite
import com.altomedia.pixeldungeon.sprites.MobSprite
import com.altomedia.pixeldungeon.utils.GLog
import com.altomedia.pixeldungeon.windows.WndOptions
import com.watabou.noosa.TextureFilm
import com.watabou.utils.Bundle

class KingStatuary : NPC() {
    init {
        spriteClass = Sprite::class.java

        properties.add(Property.IMMOVABLE)
    }

    private var hasCrown = false

    override fun description(): String = M.L(this, "desc") +
            if (hasCrown) M.L(this, "desc_wear") else M.L(this, "desc_unwear")

    override fun interact(): Boolean {
        if (hasCrown) return false

        GameScene.show(object : WndOptions(Sprite(), name, description(),
                M.L(this, "wear"), M.L(this, "unworthy")) {
            override fun onSelect(index: Int) {
                if (index == 0) onWear()
            }
        })

        return false
    }

    private fun onWear() {
        val crown = Dungeon.hero.belongings.getItem(CrownOfDwarf::class.java)
        if (crown == null) {
            GLog.w(M.L(this, "no_crown"))
            return
        }

        if (crown.isEquipped(Dungeon.hero)) crown.doUnequip(Dungeon.hero, false)
        crown.detach(Dungeon.hero.belongings.backpack)
        GLog.w(M.L(this, "on_wear"))

        hasCrown = true
        (sprite as Sprite).wear()
        Dungeon.level.drop(GreatBlueprint(), Dungeon.hero.pos)
    }

    override fun sprite(): CharSprite = Sprite().apply { if (hasCrown) wear() }

    class Sprite : MobSprite() {
        private val idleCrown: Animation

        init {
            texture(Assets.KING_STATUARY)

            val film = TextureFilm(texture, 16, 16)
            idle = Animation(10, true).apply { frames(film, 0) }

            run = Animation(10, true).apply { frames(film, 0) }

            die = Animation(10, true).apply { frames(film, 0) }

            play(idle)

            idleCrown = Animation(10, true).apply { frames(film, 1) }
        }

        fun wear() {
            play(idleCrown)
        }
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(HAS_CROWN, hasCrown)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        hasCrown = bundle.getBoolean(HAS_CROWN)
    }

    // unbreakable
    override fun act(): Boolean {
        throwItem()
        return super.act()
    }

    override fun defenseSkill(enemy: Char): Float = 1000f

    override fun takeDamage(dmg: Damage): Int = 0

    override fun add(buff: Buff) {}

    companion object {
        private const val HAS_CROWN = "has-crown"
    }
}