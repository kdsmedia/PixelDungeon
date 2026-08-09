package com.altomedia.pixeldungeon.windows

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.Journal
import com.altomedia.pixeldungeon.items.keys.GoldenKey
import com.altomedia.pixeldungeon.items.keys.IronKey
import com.altomedia.pixeldungeon.items.keys.SkeletonKey
import com.altomedia.pixeldungeon.messages.M
import com.altomedia.pixeldungeon.scenes.GameScene
import com.altomedia.pixeldungeon.scenes.PixelScene
import com.altomedia.pixeldungeon.ui.*
import com.watabou.noosa.BitmapText
import com.watabou.noosa.ColorBlock
import com.watabou.noosa.Image
import com.watabou.noosa.ui.Component

class WndJournal : Window() {
    private val btnTitle: RedButton
    private val btnCatalogues: RedButton
    private val list: ScrollPane

    init {
        resize(WIDTH.toInt(), HEIGHT.toInt())

        btnTitle = RedButton(M.L(this, "title"), 9).apply {
            textColor(TITLE_COLOR)
            setRect(0f, 0f, WIDTH / 2f - 1, reqHeight())
        }
        PixelScene.align(btnTitle)
        add(btnTitle)

        btnCatalogues = object : RedButton(M.L(WndCatalogs::class.java, "title"), 9) {
            override fun onClick() {
                hide()
                GameScene.show(WndCatalogs())
            }
        }.apply {
            setRect(WIDTH / 2f + 1, 0f, WIDTH / 2f - 1, reqHeight())
        }
        PixelScene.align(btnCatalogues)
        add(btnCatalogues)

//        list = ScrollPane(Component())
        val content = Component()
//        val content = list.content()

        Journal.records.sortBy { -it.depth }

        var pos = 0f
        // keys
        val belongings = Dungeon.hero.belongings
        for (i in belongings.ironKeys.size - 1 downTo 0) {
            if (belongings.specialKeys[i] > 0) {
                var text = if (i % 5 == 0) M.CL(SkeletonKey::class.java, "name")
                else M.CL(GoldenKey::class.java, "name")

                if (belongings.specialKeys[i] > 1)
                    text += "x${belongings.specialKeys[i]}"

                val item = ListItem(M.T(text), i).apply {
                    setRect(0f, pos, WIDTH, ITEM_HEIGHT)
                }

                content.add(item)
                pos += item.height()
            }
            if (belongings.ironKeys[i] > 0) {
                var text = M.TL(IronKey::class.java, "name")
                if (belongings.ironKeys[i] > 1)
                    text += "x${belongings.ironKeys[i]}"

                val item = ListItem(text, i).apply {
                    setRect(0f, pos, WIDTH, ITEM_HEIGHT)
                }

                content.add(item)
                pos += item.height()
            }
        }

        // journal
        for (rec in Journal.records) {
            val item = ListItem(rec.desc, rec.depth).apply {
                setRect(0f, pos, WIDTH, ITEM_HEIGHT)
            }
            content.add(item)

            pos += item.height()
        }

        content.setSize(width.toFloat(), pos);
        list = ScrollPane(content)
        add(list)
        list.setRect(0f, btnTitle.height() + 1f, WIDTH, height - btnTitle.height() - 1)
        // list.setSize(list.width(), list.height())
    }


    class ListItem(text: String, depth: Int) : Component() {
        private lateinit var feature: RenderedTextMultiline
        private lateinit var depth: BitmapText
        private lateinit var line: ColorBlock
        private lateinit var icon: Image

        init {
            feature.text(text)

            this.depth.text(depth.toString())
            this.depth.measure()

            if (depth == Dungeon.depth) {
                feature.hardlight(TITLE_COLOR)
                this.depth.hardlight(TITLE_COLOR)
            }
        }

        override fun createChildren() {
            feature = PixelScene.renderMultiline(7)
            add(feature)

            depth = BitmapText(PixelScene.pixelFont)
            add(depth)

            line = ColorBlock(1f, 1f, 0x222222)
            add(line)

            icon = Icons.get(Icons.DEPTH)
            add(icon)
        }

        override fun layout() {
            depth.x = (8 - depth.width()) / 2f
            depth.y = y + 1.5f + (height() - 1f - depth.height()) / 2f
            PixelScene.align(depth)

            icon.x = 8f
            icon.y = y + 1f + (height() - 1f - icon.height()) / 2f
            PixelScene.align(icon)

            line.size(width, 1f)
            line.x = 0f
            line.y = y

            feature.maxWidth((width - icon.width() - 8f - 1f).toInt())
            feature.setPos(icon.x + icon.width() + 1f, y + 1f + (height() - 1f - feature.height()) / 2f)
            PixelScene.align(feature)
        }
    }

    companion object {
        private const val WIDTH = 112f
        private const val HEIGHT = 160f

        private const val ITEM_HEIGHT = 17f
    }
}