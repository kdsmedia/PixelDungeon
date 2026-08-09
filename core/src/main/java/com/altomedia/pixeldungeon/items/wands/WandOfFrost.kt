package com.altomedia.pixeldungeon.items.wands

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Chill
import com.altomedia.pixeldungeon.actors.buffs.FlavourBuff
import com.altomedia.pixeldungeon.actors.buffs.Frost
import com.altomedia.pixeldungeon.effects.MagicMissile
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.levels.Level
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Callback
import com.watabou.utils.PointF
import com.watabou.utils.Random
import kotlin.math.pow
import kotlin.math.round

class WandOfFrost : DamageWand() {
    init {
        image = ItemSpriteSheet.WAND_FROST
    }

    override fun min(lvl: Int): Int = 2 + lvl

    override fun max(lvl: Int): Int = 8 + 5 * lvl

    override fun giveDamage(enemy: Char): Damage {
        return super.giveDamage(enemy).addElement(Damage.Element.ICE)
    }

    override fun onZap(attack: Ballistica) {
        Dungeon.level.heaps.get(attack.collisionPos)?.freeze()

        Actor.findChar(attack.collisionPos)?.let { ch ->
            val dmg = giveDamage(ch)

            // nothing to do wit a frozen target
            if (ch.buff(Frost::class.java) != null) return

            if (ch.buff(Chill::class.java) != null) {
                val chill = ch.buff(Chill::class.java).cooldown()
                dmg.value = round(dmg.value * 0.95f.pow(chill)).toInt()
            } else
                ch.sprite.burst(0xff99ccff.toInt(), level() / 2 + 2)

            onMissileHit(ch, Dungeon.hero, dmg)
            ch.takeDamage(dmg)

            if (ch.isAlive) {
                val duration = if (Level.water[ch.pos]) 4 + level() else 2 + level()
                Buff.prolong(ch, Chill::class.java, duration.toFloat())
            }
        }
    }

    override fun fx(bolt: Ballistica, callback: Callback) {
        MagicMissile.blueLight(Item.curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback)
        Sample.INSTANCE.play(Assets.SND_ZAP)
    }

    override fun onHit(staff: MagesStaff, damage: Damage) {
        val defender = damage.to as Char
        val chill = defender.buff(Chill::class.java)
        if (chill != null && Random.IntRange(2, 10) > chill.cooldown()) {
            // delay to no broken by the current staff attack
            object : FlavourBuff() {
                init {
                    actPriority = Int.MIN_VALUE
                }

                override fun act(): Boolean {
                    affect(target, Frost::class.java, Frost.duration(target) * Random.Float(1f, 2f))
                    return super.act()
                }
            }.attachTo(defender)
        }
    }

    override fun particleColor(): Int = 0x88CCFF

    override fun staffFx(particle: MagesStaff.StaffParticle) {
        particle.color(particleColor())
        particle.am = 0.6f
        particle.setLifespan(1.5f)
        val angle = Random.Float(PointF.PI2)
        particle.speed.polar(angle, 2f)
        particle.acc.set(0f, 1f)
        particle.setSize(0f, 1.5f)
        particle.radiateXY(Random.Float(2f))
    }
}