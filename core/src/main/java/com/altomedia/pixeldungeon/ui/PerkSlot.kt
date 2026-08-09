package com.altomedia.pixeldungeon.ui

import com.altomedia.pixeldungeon.Assets
import com.altomedia.pixeldungeon.actors.hero.perks.Perk
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.scenes.PixelScene
import com.altomedia.pixeldungeon.windows.WndMessage
import com.watabou.gltextures.TextureCache
import com.watabou.noosa.BitmapText
import com.watabou.noosa.Image
import com.watabou.noosa.TextureFilm
import com.watabou.noosa.ui.Button

open class PerkSlot(protected val perk: Perk) : Button() {
    private lateinit var icon: Image
    private lateinit var topRight: BitmapText

    init {
        setPerk()
    }

    private fun setPerk() {
        val index = perk.image()
        icon.frame(film.get(index))

        if (perk.level > 1 || perk.upgradable()) {
            topRight.text("${perk.level}")
            topRight.hardlight(0xf1ca3e)
            topRight.measure()
        } else topRight.visible = false
    }

    override fun createChildren() {
        super.createChildren()

        icon = Image(icons)
        add(icon)

        topRight = BitmapText(PixelScene.pixelFont)
        add(topRight)
    }

    override fun layout() {
        super.layout()

        icon.x = x + (width - icon.width) / 2
        icon.y = y + (height - icon.height) / 2

        topRight.x = x + (width - topRight.width())
        topRight.y = y // - (height - topRight.height())
    }

    override fun onClick() {
        GameScene.show(WndMessage(perk.description()))
    }

    companion object {
        private val icons = TextureCache.get(Assets.PERKS)
        private val film = TextureFilm(icons, 16, 16)
    }

}