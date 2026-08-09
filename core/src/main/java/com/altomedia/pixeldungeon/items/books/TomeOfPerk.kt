package com.altomedia.pixeldungeon.items.books

import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.actors.hero.perks.ExtraPerkChoice
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.sprites.ItemSpriteSheet
import com.altomedia.pixeldungeon.windows.WndSelectPerk

class TomeOfPerk : Book() {
    init {
        image = ItemSpriteSheet.MASTERY
    }

    override fun isIdentified(): Boolean = true

    override fun price(): Int = 0

    override fun doRead(hero: Hero) {
        detach(hero.belongings.backpack)

        val cnt = if (hero.heroPerk.get(ExtraPerkChoice::class.java) == null) 3 else 5
        GameScene.show(WndSelectPerk.CreateWithRandomPositives(
                M.L(WndSelectPerk::class.java, "select"), cnt))
    }
}