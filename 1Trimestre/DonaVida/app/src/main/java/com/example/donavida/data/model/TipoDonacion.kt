package com.example.donavida.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Las tres fuentes de progenitores hematopoyéticos. Lo usan Historias y Mi donación. */
@Serializable
enum class TipoDonacion {
    @SerialName("medula_osea") MEDULA_OSEA,
    @SerialName("sangre_periferica") SANGRE_PERIFERICA,
    @SerialName("cordon_umbilical") CORDON_UMBILICAL,
}
