package com.possible_triangle.flightlib.fabric

import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.init.CommonSources

object FabricSources {

    private fun Any.asJetpack(): IJetpack? {
        return if(this is IJetpack) this else null
    }

    fun register() {
        CommonSources.addEntitySources { it.asJetpack() }
        CommonSources.addEquipmentSources { it.item.asJetpack() }
    }

}