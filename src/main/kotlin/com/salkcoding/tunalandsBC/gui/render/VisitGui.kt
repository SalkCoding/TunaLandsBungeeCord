package com.salkcoding.tunalandsbc.gui.render

import br.com.devsrsouza.kotlinbukkitapi.extensions.item.displayName
import com.salkcoding.tunalandsbc.bungee.visitReceiverMap
import com.salkcoding.tunalandsbc.bungeeApi
import com.salkcoding.tunalandsbc.currentServerName
import com.salkcoding.tunalandsbc.gui.GuiInterface
import com.salkcoding.tunalandsbc.guiManager
import com.salkcoding.tunalandsbc.tunaLands
import com.salkcoding.tunalandsbc.util.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.SkullMeta
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.min

class VisitGui(private val player: Player) : GuiInterface {
    private val landMap = visitReceiverMap[player.uniqueId]!!
    private lateinit var landList: MutableList<UUID>

    private val sortButton = (Material.HOPPER * 1).apply {
        this.displayName("정렬 방법 선택")
    }

    private val statisticsInfo = (Material.PAINTING * 1).apply {
        this.displayName("지역 통계")
    }

    private var sortWay = 0
    private var currentPage = 0
    override fun render(inv: Inventory) {
        statisticsInfo.apply {
            var publicCount = 0
            var privateCount = 0
            landMap.forEach { (_, lands) ->
                if (lands.open) publicCount++
                else privateCount++
            }
            this.lore = listOf(
                "지역: ${landMap.size}개",
                "공개된 지역: ${publicCount}개",
                "비공개된 지역: ${privateCount}개"
            )
        }

        inv.setItem(0, backButton)
        inv.setItem(3, sortButton)
        inv.setItem(4, statisticsInfo)
        inv.setItem(5, sortButton)
        inv.setItem(8, backButton)

        for (i in 10..16)
            inv.setItem(i, blackPane)

        pageRender(inv)
    }

    private fun pageRender(inv: Inventory) {
        val sortLore = when (sortWay) {
            0 -> {
                //Default sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.createdMillisecond
                }.toMutableList()
                "기본"
            }
            1 -> {
                //Public sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.createdMillisecond
                }.toMutableList()
                "공개 지역"
            }
            2 -> {
                //Private sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.createdMillisecond
                }.toMutableList()
                "비공개 지역"
            }
            3 -> {
                //Solo sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.createdMillisecond
                }.filter {
                    landMap[it]!!.memberSize == 1
                }.toMutableList()
                "혼자"
            }
            4 -> {
                //Member count sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.memberSize
                }.toMutableList()
                "멤버 수"
            }
            5 -> {
                //Visitor count sorting
                landList = landMap.keys.sortedByDescending {
                    landMap[it]!!.visitorCount
                }.toMutableList()
                "방문자 수"
            }
            else -> ""
        }

        sortButton.apply {
            this.lore = listOf(
                "현재 보기 상태: $sortLore",
                "기본: 생성된 시간이 오래된 순서로 모든 지역의 목록을 봅니다.",
                "공개 지역: 공개로 설정된 지역의 목록을 생성된 시간이 오래된 순서로 봅니다.",
                "비공개 지역: 비공개로 설정된 지역의 목록을 생성된 시간이 오래된 순서로 봅니다.",
                "혼자:  혼자 살아가는 지역들을, 생성된 시간이 오래된 순서로 봅니다.",
                "멤버 수: 멤버가 많은 순으로 지역의 목록을 봅니다.",
                "방문자 수: 방문자 수가 많은 순으로 지역의 목록을 봅니다.",
                "",
                "클릭하여 정렬 방법을 변경할 수 있습니다."
            )
        }
        inv.setItem(3, sortButton)
        inv.setItem(5, sortButton)

        val start = currentPage * 36
        val length = min(landList.size - start, 36)

        for (i in start until length) {
            val uuid = landList[start + i]
            val entry = Bukkit.getOfflinePlayer(uuid)
            val lands = landMap[uuid]!!

            val head = (Material.PLAYER_HEAD * 1).apply {
                val meta = this.itemMeta as SkullMeta
                val created = Calendar.getInstance()
                created.timeInMillis = lands.createdMillisecond
                meta.owningPlayer = entry
                meta.setDisplayName(entry.name)
                val lore = mutableListOf(
                    "공개 여부: ${
                        when (lands.open) {
                            true -> "공개"
                            false -> "비공개"
                        }
                    }",
                    "멤버 수: ${lands.memberSize}",
                    "방문자 수: ${lands.visitorCount}",
                    "생성일: ${created.get(Calendar.YEAR)}/${created.get(Calendar.MONTH) + 1}/${created.get(Calendar.DATE)}",
                )
                (0 until lands.lore.size).forEach { i ->
                    lore.add(i, lands.lore[i])
                }
                if (lands.open) {
                    lore.add("")
                    lore.add("클릭하여 이동할 수 있습니다.")
                }
                meta.lore = lore
                this.itemMeta = meta
            }
            //Start index is 18 because of decorations
            inv.setItem(i + 18, head)
        }

        if (currentPage < 1)
            inv.setItem(9, blackPane)
        else
            inv.setItem(9, previousPageButton)

        if ((landList.size - start) > 36)
            inv.setItem(17, nextPageButton)
        else
            inv.setItem(17, blackPane)
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true
        when (event.rawSlot) {
            //Back button
            0, 8 -> {
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
                player.closeInventory()
            }
            //Hopper(Sorting way change)
            3, 5 -> {
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
                sortWay++
                if (sortWay > 5)
                    sortWay = 0
                pageRender(event.inventory)
            }
            //Previous
            9 -> {
                if (currentPage > 0) {
                    currentPage--
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
                    pageRender(event.inventory)
                }
            }
            //Next
            17 -> {
                val start = currentPage * 36
                if ((landList.size - start) > 36) {
                    currentPage++
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
                    pageRender(event.inventory)
                }
            }
            in 18..53 -> {
                val index = (currentPage * 36) + (event.rawSlot - 18)
                if (index >= landList.size) return

                val lands = landMap[landList[index]] ?: return
                if (!lands.open && !player.isOp) {
                    player.sendMessage("땅이 비공개 상태라 방문할 수 없습니다!".errorFormat())
                    return
                }

                val uuid = player.uniqueId
                Bukkit.getScheduler().runTaskAsynchronously(tunaLands, Runnable {
                    val messageBytes = ByteArrayOutputStream()
                    val messageOut = DataOutputStream(messageBytes)
                    try {
                        messageOut.writeUTF(uuid.toString())
                        messageOut.writeUTF(player.name)
                        messageOut.writeUTF(currentServerName)
                        messageOut.writeUTF(lands.ownerUUID.toString())
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    }

                    bungeeApi.forward("ALL", "tunalands-visit-connect", messageBytes.toByteArray())
                })
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        visitReceiverMap.remove(player.uniqueId)
        guiManager.guiMap.remove(event.view)
    }
}

fun Player.openVisitGui() {
    val inventory = Bukkit.createInventory(null, 54, "User list GUI")
    val gui = VisitGui(this)
    gui.render(inventory)

    val view = this.openInventory(inventory)!!
    guiManager.guiMap[view] = gui
}