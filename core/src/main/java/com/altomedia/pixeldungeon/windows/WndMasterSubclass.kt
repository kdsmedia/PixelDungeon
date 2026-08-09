package com.altomedia.pixeldungeon.windows

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.hero.HeroClass
import com.altomedia.pixeldungeon.actors.hero.HeroSubClass
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.scenes.PixelScene
import com.altomedia.pixeldungeon.ui.RedButton
import com.altomedia.pixeldungeon.ui.Window

class WndMasterSubclass(way1: HeroSubClass, way2: HeroSubClass) : Window() {
    init {
        val rtm = PixelScene.renderMultiline(PixelScene.FONT_SIZE_BODY)
        rtm.text(M.L(this, "message") + "\n\n" + way1.desc() + "\n\n" + way2.desc(), WIDTH.toInt())
        rtm.setPos(0f, 0f)
        add(rtm)

        val btnWay1 = object : RedButton(way1.title().toUpperCase()) {
            override fun onClick() {
                hide()
                HeroSubClass.Choose(Dungeon.hero!!, way1)
            }
        }
        btnWay1.setRect(0f, rtm.bottom() + GAP, (WIDTH - GAP) / 2f, BTN_HEIGHT)
        add(btnWay1)

        val btnWay2 = object : RedButton(way2.title().toUpperCase()) {
            override fun onClick() {
                hide()
                HeroSubClass.Choose(Dungeon.hero!!, way2)
            }
        }
        btnWay2.setRect(btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT)
        add(btnWay2)

        val btnCancel = object : RedButton(M.L(this, "cancel")) {
            override fun onClick() {
                hide()
                GameScene.show(WndMessage(M.L(WndMasterSubclass::class.java, "tip")))
            }
        }
        btnCancel.setRect(0f, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT)
        add(btnCancel)

        resize(WIDTH.toInt(), btnCancel.bottom().toInt())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        GameScene.show(WndMessage(M.L(WndMasterSubclass::class.java, "tip")))
    }

    companion object {
        private const val WIDTH = 120f
        private const val BTN_HEIGHT = 18f
        private const val GAP = 2f

        fun Show(hero: Hero) {
            val pr = when (hero.heroClass) {
                HeroClass.WARRIOR -> Pair(HeroSubClass.GLADIATOR, HeroSubClass.BERSERKER)
                HeroClass.MAGE -> Pair(HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK)
                HeroClass.ROGUE -> Pair(HeroSubClass.FREERUNNER, HeroSubClass.ASSASSIN)
                HeroClass.HUNTRESS -> Pair(HeroSubClass.SNIPER, HeroSubClass.WARDEN)
                HeroClass.SORCERESS -> Pair(HeroSubClass.STARGAZER, HeroSubClass.WITCH)
            }

            GameScene.show(WndMasterSubclass(pr.first, pr.second))
        }
    }
}