﻿package com.salkcoding.tunalandsBC.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

object ComponentFactory {

    fun areEqual(a: Component, b: Component): Boolean {
        return PlainTextComponentSerializer.plainText().serialize(a) == PlainTextComponentSerializer.plainText().serialize(b)
    }

    fun plain(c: Component): Component {
        return c.decoration(TextDecoration.ITALIC, false)
    }

    fun plain(s: String): Component {
        return Component.text(s, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
    }
    fun plain(s: String, c: NamedTextColor): Component {
        return Component.text(s, c).decoration(TextDecoration.ITALIC, false)
    }
}