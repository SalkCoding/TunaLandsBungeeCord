package com.salkcoding.tunalandsBC.util

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

operator fun Material.times(i: Int): ItemStack {
    return ItemStack(this, i)
}

//Back button
val backButton = (Material.FEATHER * 1).apply {
    val meta = this.itemMeta
    meta.displayName(ComponentFactory.plain("뒤로가기", NamedTextColor.WHITE))
    this.itemMeta = meta
}

//Background decoration
val blackPane = (Material.BLACK_STAINED_GLASS_PANE * 1).apply {
    val meta = this.itemMeta
    meta.displayName(ComponentFactory.plain(" ", NamedTextColor.WHITE))
    this.itemMeta = meta
}

//Paging button
val nextPageButton = (Material.LIME_STAINED_GLASS_PANE * 1).apply {
    val meta = this.itemMeta
    meta.displayName(ComponentFactory.plain("다음 페이지", NamedTextColor.WHITE))
    this.itemMeta = meta
}

//Paging button
val previousPageButton = (Material.LIME_STAINED_GLASS_PANE * 1).apply {
    val meta = this.itemMeta
    meta.displayName(ComponentFactory.plain("이전 페이지", NamedTextColor.WHITE))
    this.itemMeta = meta
}