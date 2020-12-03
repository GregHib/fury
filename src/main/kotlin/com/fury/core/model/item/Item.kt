package com.fury.core.model.item

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.item.ItemDefinition
import com.fury.game.system.files.loaders.item.ItemConstants
import com.fury.game.system.files.loaders.item.ItemDefinitions


/**
 * Represents an item which is owned by a player.
 *
 * @author relex lawl
 * @author Greg
 */

open class Item @JvmOverloads constructor(var id: Int, var amount: Int, var revision: Revision = getRevision(id)) {

    constructor(id: Int) : this(id, 1, getRevision(id))

    constructor(id: Int, toCopy: Item) : this(id, toCopy.amount, toCopy.revision)

    constructor(toCopy: Item, amount: Int) : this(toCopy.id, amount, toCopy.revision)

    constructor(id: Int, revision: Revision) : this(id, 1, revision)


    val definitions: ItemDefinitions
        get() = ItemDefinitions.forId(id)
    val name: String
        get() = getDefinition().getName()

    private var definition: ItemDefinition? = null

    fun getDefinition(): ItemDefinition {
        return if (definition != null) definition!! else Loader.getItem(id, revision)
    }

    fun setDefinition(item: Item) {
        definition = item.getDefinition()
    }

    fun tradeable(): Boolean {
        return ItemConstants.isTradeable(this)
    }

    fun copy(): Item {
        return Item(id, amount, revision)
    }

    fun incrementAmount() {
        if (amount + 1 > Integer.MAX_VALUE) {
            return
        }
        amount++
    }

    fun decrementAmount() {
        if (amount - 1 < 0) {
            return
        }
        amount--
    }

    fun incrementAmountBy(amount: Int) {
        if (this.amount + amount > Integer.MAX_VALUE) {
            this.amount = Integer.MAX_VALUE
        } else {
            this.amount += amount
        }
    }

    fun decrementAmountBy(amount: Int) {
        if (this.amount - amount < 1) {
            this.amount = 0
        } else {
            this.amount -= amount
        }
    }

    fun createAndDecrement(removeAmount: Int): Item {
        if (removeAmount < 0) { // Same effect as incrementing.
            return createAndIncrement(-removeAmount)
        }

        var newAmount = amount - removeAmount

        // Value too low, or an overflow.
        if (newAmount < 1 || newAmount > amount) {
            newAmount = 1
        }

        val clone = copy()
        clone.amount = newAmount
        return clone
    }

    fun createAndIncrement(addAmount: Int): Item {
        if (addAmount < 0) { // Same effect as decrementing.
            return createAndDecrement(Math.abs(addAmount))
        }

        var newAmount = amount + addAmount

        if (newAmount < amount) { // An overflow.
            newAmount = Integer.MAX_VALUE
        }

        val item = copy()
        item.amount = newAmount
        return item
    }

    fun isEqual(item: Item?): Boolean {
        return item != null && isEqual(item.id, item.revision)
    }

    @JvmOverloads
    fun isEqual(id: Int, revision: Revision = Revision.RS2): Boolean {
        return this.id == id && this.revision == revision
    }

    override fun toString(): String {
        return "$id $amount $revision"
    }

    companion object {

        fun getRevision(id: Int): Revision {
            if (id in 22346..22348 || id == 23400 || id == 23351)
                return Revision.PRE_RS3
            return if (id >= Loader.getTotalItems(Revision.RS2)) if (id >= Loader.getTotalItems(Revision.PRE_RS3)) Revision.RS3 else Revision.PRE_RS3 else Revision.RS2
        }

        fun getNoted(item: Item): Item {
            return if (item.id != -1 && !item.getDefinition().isNoted && item.getDefinition().noteId != -1)
                Item(item.getDefinition().noteId, item)
            else
                item
        }

        fun getUnNoted(item: Item): Item {
            return if (item.id != -1 && item.getDefinition().isNoted)
                Item(item.getDefinition().noteId, item)
            else
                item
        }
    }
}