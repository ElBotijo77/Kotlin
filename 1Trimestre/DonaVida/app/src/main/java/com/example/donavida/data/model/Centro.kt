package com.example.donavida.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Centro oficial donde informarse e inscribirse como donante.
 *
 * @Serializable hace que kotlinx.serialization genere el código para convertir JSON en este
 * objeto. Los nombres de las propiedades coinciden con las claves del JSON.
 *
 * Los campos que no tienen todos los centros llevan valor por defecto: así un JSON sin esa
 * clave sigue siendo válido, y en la ficha esa sección simplemente no se muestra.
 */
@Serializable
data class Centro(
    val id: String,
    val nombre: String,
    val tipo: TipoCentro,
    val comunidad: String,
    val direccion: String,
    val ciudad: String = "",
    val provincia: String = "",
    /** 5 cifras; las dos primeras son la provincia (28 = Madrid). */
    val codigoPostal: String = "",
    /** Qué se puede hacer en el centro; sirve para los filtros de la lista. */
    val servicios: Set<ServicioCentro> = emptySet(),
    val latitud: Double,
    val longitud: Double,
    /** Solo dígitos o con espacios; vacío si el centro no tiene teléfono público. */
    val telefono: String,
    val web: String? = null,
    /** Resumen del horario en texto libre (lo usará la lista de centros). */
    val horario: String,
    /** Si hace falta cita previa para la toma de muestra. */
    val requiereCita: Boolean,
    val queLlevar: List<String> = emptyList(),
    /** De dónde sale la información del centro (página oficial). */
    val urlFuente: String,
    /** Fecha en que se comprobaron los datos ("AAAA-MM-DD" en el JSON), o null si aún no. */
    @Serializable(with = LocalDateSerializer::class)
    val fechaVerificacion: LocalDate? = null,
    /** Centro regional de referencia para el registro REDMO. */
    val esReferenciaRedmo: Boolean = false,
    /** Cómo llegar en transporte público, una línea por opción. */
    val transportes: List<String> = emptyList(),
    /** Franjas de apertura detalladas, para la ficha y para saber si está abierto ahora. */
    val horarios: List<FranjaHorario> = emptyList(),
    /** Atiende sin cerrar a mediodía. */
    val atencionContinua: Boolean = false,
)

/**
 * Una franja de apertura: "Lunes a sábado, de 08:30 a 21:00".
 * Los festivos se tratan como domingo: una franja "Domingos y festivos" lleva SUNDAY.
 */
@Serializable
data class FranjaHorario(
    /** Texto que se muestra: "Lunes a sábado". */
    val dias: String,
    @Serializable(with = LocalTimeSerializer::class)
    val apertura: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val cierre: LocalTime,
    /** En el JSON, en inglés y mayúsculas: ["MONDAY", "TUESDAY"]. */
    val diasSemana: Set<@Serializable(with = DayOfWeekSerializer::class) DayOfWeek>,
)

@Serializable
enum class TipoCentro {
    @SerialName("centro_transfusion") CENTRO_TRANSFUSION,
    @SerialName("hospital_publico") HOSPITAL_PUBLICO,
    /** Punto fijo donde inscribirse en REDMO, sin ser centro de transfusión. */
    @SerialName("punto_inscripcion") PUNTO_INSCRIPCION,
    /** Maternidad autorizada para la donación de sangre de cordón umbilical. */
    @SerialName("maternidad") MATERNIDAD,
}

@Serializable
enum class ServicioCentro {
    @SerialName("inscripcion_medula") INSCRIPCION_MEDULA,
    @SerialName("sangre_cordon") SANGRE_CORDON,
    @SerialName("puntos_temporales") PUNTOS_TEMPORALES,
}
