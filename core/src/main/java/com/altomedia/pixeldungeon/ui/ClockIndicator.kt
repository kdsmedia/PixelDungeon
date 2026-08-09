package com.altomedia.pixeldungeon.ui

import com.altomedia.pixeldungeon.Dungeon
import com.altomedia.pixeldungeon.Statistics
import com.altomedia.pixeldungeon.scenes.PixelScene
import com.watabou.noosa.BitmapText

class ClockIndicator : Tag(0xff4c4c) {
    init {
        setSize(24f, 16f)
        flash()
        visible = false

        Instance = this
    }

    private lateinit var timestr: BitmapText

    override fun createChildren() {
        super.createChildren()

        timestr = BitmapText("00:00", PixelScene.pixelFont)
        add(timestr)
    }

    override fun layout() {
        super.layout()

        timestr.measure()
        timestr.x = x + (width - timestr.width()) / 2f
        timestr.y = y + (height - timestr.baseLine()) / 2f
        PixelScene.align(timestr)
    }

    override fun update() {
        visible = Statistics.Clock.special
        if(visible) timestr.text(Statistics.Clock.timestr)

        super.update()
    }

    companion object {
        lateinit var Instance: ClockIndicator
    }
}