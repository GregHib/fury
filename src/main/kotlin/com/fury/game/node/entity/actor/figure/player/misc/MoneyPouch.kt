package com.fury.game.node.entity.actor.figure.player.misc

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.game.entity.character.player.actions.ItemMorph
import com.fury.game.entity.character.player.content.BankPin
import com.fury.core.model.item.Item
import com.fury.game.network.packet.out.MoneyPouchFlash

/**
 * Handles the money pouch
 *
 * @author Goml
 * Perfected by Gabbe
 * Converted and improved by Greg
 */
class MoneyPouch(private val player: Player) {

    var total: Long = 0
        set(moneyInPouch) {
            if (player.started)
                player.send(MoneyPouchFlash(moneyInPouch > this.total))
            field = moneyInPouch
        }

    val amountAsInt: Int
        get() = if (total > Integer.MAX_VALUE) Integer.MAX_VALUE else total.toInt()

    /**
     * Deposits money into the money pouch
     *
     * @param amount How many coins to deposit
     * @return false Returns false if transaction was unsuccessful
     */
    fun deposit(amount: Int): Boolean {
        if (amount <= 0)
            return false
        if (player.interfaceId > 0) {
            player.message("Please close the interface you have open before opening another one.")
            return false
        }
        if (player.health.hitpoints <= 0) {
            player.message("You cannot do this while dying.")
            return false
        }
        if (player.isInWilderness) {
            player.message("You cannot do this here.")
            return false
        }
        if (validateAmount(amount)) {
            val addedMoney = player.moneyPouch.total + amount
            if (addedMoney > java.lang.Long.MAX_VALUE) {
                val canStore = java.lang.Long.MAX_VALUE - player.moneyPouch.total
                player.inventory.delete(Item(995, canStore.toInt()))
                player.moneyPouch.total = player.moneyPouch.total + canStore
                player.packetSender.sendString(8135, "" + player.moneyPouch.total)
                player.message("$canStore coins have been added to your money pouch.")
                return true
            } else {
                if (!player.controllerManager.canAddMoneyToPouch(player, amount))
                    return false
                player.inventory.delete(Item(995, amount))
                player.moneyPouch.total = player.moneyPouch.total + amount
                player.packetSender.sendString(8135, "" + player.moneyPouch.total)
                Achievements.finishAchievement(player, Achievements.AchievementData.ADD_COINS_TO_POUCH)
                player.message("$amount coins have been added to your money pouch.")
                return true
            }
        } else {
            player.message("You do not seem to have $amount coins in your inventory.")
            return false
        }
    }

    /**
     * @param amount How many coins to withdraw
     * @return false Returns false if the transaction was unsuccessful
     */
    fun withdraw(amount: Long): Boolean {
        if (amount <= 0)
            return false
        if (player.dungManager.isInside) {
            player.message("You cannot do this here.")
            return false
        }

        if (player.moneyPouch.total <= 0) {
            player.message("Your money pouch is empty.")
            return false
        }

        if (player.bankPinAttributes.hasBankPin() && !player.bankPinAttributes.hasEnteredBankPin()) {
            BankPin.init(player) { withdraw(amount) }
            return false
        }

        if (player.inventory.spaces <= 0 && !player.inventory.contains(Item(995))) {
            player.inventory.full()
            return false
        }

        if (player.actionManager.action is ItemMorph) {
            player.message("You cannot do this at the moment.")
            return false
        }

        val allowWithdraw = player.trade.inTrade() || player.dueling.inDuelScreen
        if (!allowWithdraw) {
            if (player.interfaceId > 0) {
                player.message("Please close the interface you have open before opening another one.")
                return false
            }
            player.packetSender.sendInterfaceRemoval()
        }
        var toWithdraw = amount
        if (toWithdraw > player.moneyPouch.total)
            toWithdraw = player.moneyPouch.total
        if (player.inventory.getAmount(Item(995)) + toWithdraw < Integer.MAX_VALUE) {
            player.moneyPouch.total = player.moneyPouch.total - toWithdraw
            player.inventory.add(Item(995, toWithdraw.toInt()))
            player.packetSender.sendString(8135, "" + player.moneyPouch.total)
            if (toWithdraw == 10000L)
                Achievements.finishAchievement(player, Achievements.AchievementData.WITHDRAW_10K)
            player.message("You withdraw $toWithdraw coins from your pouch.")
            if (allowWithdraw)
                player.packetSender.sendItemContainer(player.inventory, 3322)
            return true
        } else if (player.inventory.getAmount(Item(995)) + toWithdraw > Integer.MAX_VALUE) {
            val canWithdraw = Integer.MAX_VALUE - player.inventory.getAmount(Item(995))
            if (canWithdraw == 0) {
                player.message("You cannot withdraw more money into your inventory.")
                return false
            }
            player.moneyPouch.total = player.moneyPouch.total - canWithdraw
            player.inventory.add(Item(995, canWithdraw))
            player.packetSender.sendString(8135, "" + player.moneyPouch.total)
            player.message("You could only withdraw $canWithdraw coins.")
            if (allowWithdraw)
                player.packetSender.sendItemContainer(player.inventory, 3322)
            return true
        }
        return false
    }


    fun bank() {
        if (!player.bank.banking())
            return

        if (player.moneyPouch.total == 0L) {
            player.message("You money pouch is empty.")
            return
        }

        val amount = player.moneyPouch.amountAsInt
        val coins = Item(995, 0)
        val currentAmount = if (player.bank.contains(coins)) player.bank.get(coins)!!.amount else 0
        val totalAmount = (currentAmount + amount).toLong()
        if (totalAmount > Integer.MAX_VALUE || totalAmount < 0) {
            val canWithdraw = Integer.MAX_VALUE - currentAmount
            if (canWithdraw <= 0) {
                player.message("You can't deposit anymore money into your bank.")
                return
            }
            val add: Boolean
            if (currentAmount != 0)
                add = player.bank.getTab(coins).add(Item(coins, canWithdraw))
            else
                add = player.bank.tab()!!.add(Item(coins, canWithdraw))
            player.bank.refresh()
            if (add)
                player.moneyPouch.total = player.moneyPouch.total - canWithdraw
            player.packetSender.sendString(8135, player.moneyPouch.total.toString())
        } else {
            val add = player.bank.tab()!!.add(Item(coins, amount))
            player.bank.refresh()
            if (add)
                player.moneyPouch.total = player.moneyPouch.total - amount
            player.packetSender.sendString(8135, player.moneyPouch.total.toString())
        }
    }

    /**
     * Validates that the player has the coins in their inventory
     *
     * @param amount The amount the player wishes to insert
     * @return false Returns false if the player does not have the coins in their inventory
     */
    private fun validateAmount(amount: Int): Boolean {
        return player.inventory.getAmount(Item(995)) >= amount
    }

}