package com.salkcoding.tunalandsBC.commands.sub

import com.salkcoding.tunalandsBC.util.infoFormat
import com.salkcoding.tunalandsBC.util.warnFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Help : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("명령어 목록".infoFormat())
        sender.sendMessage("/tl accept: 초대를 수락합니다.".infoFormat())
        sender.sendMessage("/tl alba (name): 해당 유저를 알바로 채용하기 위한 초대를 보냅니다.".warnFormat())
        sender.sendMessage("/tl ban (name): 해당 유저를 밴합니다.".infoFormat())
        sender.sendMessage("/tl banlist: 땅에서 밴된 유저 목록을 보여줍니다.".infoFormat())
        sender.sendMessage("/tl cancel (name): 보낸 초대를 취소합니다.".infoFormat())
        sender.sendMessage("/tl delete: 자신 소유의 땅을 삭제합니다.".infoFormat())
        sender.sendMessage("/tl demote (name): 해당 유저를 강등시킵니다.".infoFormat())
        sender.sendMessage("/tl deny: 초대를 거절합니다.".infoFormat())
        sender.sendMessage("/tl hego (name): 해당 유저를 해고합니다.".warnFormat())
        sender.sendMessage("/tl help: 땅 보호 관련 명령어를 보여줍니다.".warnFormat())
        sender.sendMessage("/tl invite (name): 해당 유저를 멤버로 초대합니다.".infoFormat())
        sender.sendMessage("/tl kick (name): 땅에서 해당 유저를 쫓아냅니다.".infoFormat())
        sender.sendMessage("/tl leave: 땅을 떠납니다.".infoFormat())
        sender.sendMessage("/tl promote (name): 해당 유저를 승급시킵니다.".infoFormat())
        sender.sendMessage("/tl recommend: 다른 사람의 땅을 추천할 수 있는 GUI를 엽니다.".infoFormat())
        sender.sendMessage("/tl rename (name): 땅의 이름을 변경합니다.".infoFormat())
        sender.sendMessage("/tl setspawn (main/visitor): 땅의 멤버들의 스폰과, 알바와 방문자의 스폰 지점을 설정합니다.".infoFormat())
        sender.sendMessage("/tl spawn: 땅의 스폰 지점으로 이동합니다.".infoFormat())
        sender.sendMessage("/tl unban (name): 해당 유저의 밴을 해제합니다.".infoFormat())
        sender.sendMessage("/tl visit: 방문할 수 있는 땅을 GUI로 보여줍니다.".infoFormat())
        sender.sendMessage("/tl setleader (name): 해당 유저를 땅의 소유주로 만듭니다.".infoFormat())
        if (sender.isOp) {
            sender.sendMessage("관리자 전용 디버깅 명령어 목록".infoFormat())
            sender.sendMessage("/tl debug api show: Metamorphosis로 Synced된 데이터를 출력합니다.".warnFormat())
            sender.sendMessage("/tl debug api rank (name): Metamorphosis로 Synced된 데이터를 기준으로 player rank를 출력합니다.".warnFormat())
        }
        return true
    }
}