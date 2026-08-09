package com.altomedia.pixeldungeon.levels.traps

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.hero.HeroLines
import com.altomedia.pixeldungeon.sprites.TrapSprite
import com.watabou.utils.Random

class FreakingTrap : Trap() {
    init {
        color = TrapSprite.RED
        shape = TrapSprite.GRILL
    }

    override fun activate() {
        val ch = Actor.findChar(pos)
        if (ch === Dungeon.hero && ch.isAlive) {
            ch.takeDamage(Damage(Random.NormalIntRange(3, 9), this, ch).type(Damage.Type.MENTAL))

            ch.say(HeroLines.Line(HeroLines.BAD_NOISE))
        }
    }
}