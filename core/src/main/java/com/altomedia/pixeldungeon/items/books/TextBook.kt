package com.altomedia.pixeldungeon.items.books

import com.altomedia.pixeldungeon.actors.hero.Hero
import com.altomedia.pixeldungeon.messages.Messages
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.windows.WndTextBook

import java.util.ArrayList

/**
 * Created by 93942 on 10/14/2018.
 */

open class TextBook : Book() {
    private var pageSize = -1

    fun pageSize(): Int {
        if (pageSize < 0)
            pageSize = Integer.parseInt(Messages.get(this, "pagesize"))
        return pageSize
    }

    fun page(i: Int): String = Messages.get(this, "page$i")

    override fun doRead(hero: Hero) {
        identify()
        GameScene.show(WndTextBook(this))
    }

    override fun price(): Int = quantity() * pageSize() * 5
}
