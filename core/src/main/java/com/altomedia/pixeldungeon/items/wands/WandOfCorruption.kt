/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.altomedia.pixeldungeon.items.wands

import com.altomedia.pixeldungeon.*
import com.altomedia.pixeldungeon.actors.Actor
import com.altomedia.pixeldungeon.actors.Char
import com.altomedia.pixeldungeon.actors.Damage
import com.altomedia.pixeldungeon.actors.buffs.Amok
import com.altomedia.pixeldungeon.actors.buffs.Blindness
import com.altomedia.pixeldungeon.actors.buffs.Buff
import com.altomedia.pixeldungeon.actors.buffs.Charm
import com.altomedia.pixeldungeon.actors.buffs.Chill
import com.altomedia.pixeldungeon.actors.buffs.Corruption
import com.altomedia.pixeldungeon.actors.buffs.Cripple
import com.altomedia.pixeldungeon.actors.buffs.FlavourBuff
import com.altomedia.pixeldungeon.actors.buffs.Frost
import com.altomedia.pixeldungeon.actors.buffs.Paralysis
import com.altomedia.pixeldungeon.actors.buffs.PinCushion
import com.altomedia.pixeldungeon.actors.buffs.Roots
import com.altomedia.pixeldungeon.actors.buffs.Slow
import com.altomedia.pixeldungeon.actors.buffs.SoulMark
import com.altomedia.pixeldungeon.actors.buffs.Terror
import com.altomedia.pixeldungeon.actors.buffs.Vertigo
import com.altomedia.pixeldungeon.actors.buffs.Vulnerable
import com.altomedia.pixeldungeon.actors.buffs.Weakness
import com.altomedia.pixeldungeon.actors.mobs.Bee
import com.altomedia.pixeldungeon.actors.mobs.King
import com.altomedia.pixeldungeon.actors.mobs.Mimic
import com.altomedia.pixeldungeon.actors.mobs.Mob
import com.altomedia.pixeldungeon.actors.mobs.Piranha
import com.altomedia.pixeldungeon.actors.mobs.Statue
import com.altomedia.pixeldungeon.actors.mobs.Swarm
import com.altomedia.pixeldungeon.actors.mobs.Wraith
import com.altomedia.pixeldungeon.actors.mobs.Yog
import com.altomedia.pixeldungeon.effects.MagicMissile
import com.altomedia.pixeldungeon.items.Item
import com.altomedia.pixeldungeon.items.weapon.melee.MagesStaff
import com.altomedia.pixeldungeon.mechanics.Ballistica
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.utils.GLog
import com.watabou.noosa.audio.Sample
import com.watabou.utils.Callback
import com.watabou.utils.Random

import java.util.HashMap

class WandOfCorruption : Wand() {
    init {
        image = ItemSpriteSheet.WAND_CORRUPTION
    }

    override fun onZap(bolt: Ballistica) {
        val mob = Actor.findChar(bolt.collisionPos)
        if (mob != null && mob is Mob) {
            val corruptingPower = 2f + level()
            // todo: clean
            var enemyResist = when {
                mob is Mimic || mob is Statue -> 1f + Dungeon.depth
                mob is Piranha || mob is Bee -> 1f + Dungeon.depth / 2f
                mob is Wraith -> 0.5f + Dungeon.depth / 8f
                mob is Yog.Larva || mob is King.Undead -> 1f + 30f
                mob is Swarm -> 1f + 3f
                else -> 1f + mob.EXP
            }

            //100% health: 3x resist   75%: 2.1x resist   50%: 1.5x resist   25%: 1.1
            enemyResist *= 1f + 2f * Math.pow(mob.HP / mob.HT.toDouble(), 2.0).toFloat()

            // placed debuffs reduce their resistance
            for (buff in mob.buffs()) {
                var r = 1f
                if (buff.type == Buff.buffType.NEGATIVE) r = MINOR_DEBUFF_WEAKEN
                if (buff is FlavourBuff) {
                    if (buff.javaClass in MAJOR_DEBUFFS) r = MAJOR_DEBUFF_WEAKEN
                    else if (buff.javaClass in MINOR_DEBUFFS) r = MINOR_DEBUFF_WEAKEN
                }

                enemyResist *= r
            }

            //cannot re-corrupt or doom an enemy, so give them a major debuff instead
            if (mob.buff(Corruption::class.java) != null) enemyResist = corruptingPower * .99f

            if (corruptingPower > enemyResist) corruptEnemy(mob)
            else {
                val debuffChance = corruptingPower / enemyResist
                if (Random.Float() < debuffChance) debuffEnemy(mob, MAJOR_DEBUFFS)
                else debuffEnemy(mob, MINOR_DEBUFFS)
            }
        }
    }

    private fun debuffEnemy(enemy: Mob, category: HashMap<Class<out FlavourBuff>, Float>) {
        val candidates = category.filter { enemy.buff(it.key) == null && !enemy.immunizedBuffs().contains(it.key) }
        val cls = KRandom.Chances(candidates)
        if (cls != null)
            Buff.append(enemy, cls, 6f + level() * 3f)
        else
            if (category === MINOR_DEBUFFS) debuffEnemy(enemy, MAJOR_DEBUFFS)
            else if (category === MAJOR_DEBUFFS) corruptEnemy(enemy)
    }

    private fun corruptEnemy(enemy: Mob) {
        //cannot re-corrupt or doom an enemy, so give them a major debuff instead
        if (enemy.buff(Corruption::class.java) != null || enemy.buff(Vulnerable::class.java) != null) {
            GLog.w(Messages.get(this, "already_corrupted"))
            return
        }

        val canBeCorruptted = (!enemy.immunizedBuffs().contains(Corruption::class.java) &&
                !enemy.properties().contains(Char.Property.BOSS) &&
                !enemy.properties().contains(Char.Property.MINIBOSS) &&
                !enemy.properties().contains(Char.Property.MACHINE))


        if (canBeCorruptted) {
            enemy.HP = enemy.HT
            for (buff in enemy.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE && buff !is SoulMark)
                    buff.detach()
                else if (buff is PinCushion)
                    buff.detach() // drop ranged weapon
            }
            Buff.affect(enemy, Corruption::class.java)

            // in dpd, enemy would not dead directly...
//            Statistics.enemiesSlain++;
//            Badges.validateMonstersSlain();
//            Statistics.qualifiedForNoKilling = false;
//            if (enemy.EXP > 0 && curUser.lvl <= enemy.maxLvl) {
//                curUser.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy,
//                        "exp", enemy.EXP));
//                curUser.earnExp(enemy.EXP);
//            }

        } else {
            // in dpd, i give the vulnerable
            Buff.prolong(enemy, Vulnerable::class.java, Vulnerable.DURATION * 2).ratio = 1.5f
        }
    }

    override fun onHit(staff: MagesStaff, damage: Damage) {
        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        if (Random.Int(level() + 4) >= 3) {
            Buff.prolong(damage.to as Char, Amok::class.java, (3 + level()).toFloat())
        }
    }

    override fun fx(bolt: Ballistica, callback: Callback) {
        MagicMissile.shadow(Item.curUser.sprite.parent, bolt.sourcePos, bolt
                .collisionPos, callback)
        Sample.INSTANCE.play(Assets.SND_ZAP)
    }

    override fun staffFx(particle: MagesStaff.StaffParticle) {
        particle.color(0)
        particle.am = 0.6f
        particle.setLifespan(0.8f)
        particle.acc.set(0f, 20f)
        particle.setSize(0f, 3f)
        particle.shuffleXY(2f)
    }

    companion object {

        private val MINOR_DEBUFF_WEAKEN = .8f
        private val MINOR_DEBUFFS = HashMap<Class<out FlavourBuff>, Float>()

        init {
            MINOR_DEBUFFS[Weakness::class.java] = 0f  // in dpd, weakness can only attach hero
            MINOR_DEBUFFS[Cripple::class.java] = 1f
            MINOR_DEBUFFS[Blindness::class.java] = 1f
            MINOR_DEBUFFS[Terror::class.java] = 1f

            MINOR_DEBUFFS[Chill::class.java] = 0f
            MINOR_DEBUFFS[Roots::class.java] = 0f
            MINOR_DEBUFFS[Vertigo::class.java] = 0f
            // MINOR_DEBUFFS[Bleeding::class.java] = 0f
            // MINOR_DEBUFFS[Burning::class.java] = 0f
            // MINOR_DEBUFFS[Poison::class.java] = 0f
        }

        private val MAJOR_DEBUFF_WEAKEN = .667f
        private val MAJOR_DEBUFFS = HashMap<Class<out FlavourBuff>, Float>()

        init {
            MAJOR_DEBUFFS[Amok::class.java] = 3f
            MAJOR_DEBUFFS[Slow::class.java] = 2f
            MAJOR_DEBUFFS[Paralysis::class.java] = 1f
            MAJOR_DEBUFFS[SoulMark::class.java] = 1f

            MAJOR_DEBUFFS[Charm::class.java] = 0f
            // MAJOR_DEBUFFS[MagicalSleep::class.java] = 0f
            MAJOR_DEBUFFS[SoulMark::class.java] = 0f
            MAJOR_DEBUFFS[Frost::class.java] = 0f
        }
    }

}
