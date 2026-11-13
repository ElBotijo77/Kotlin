# Cambios de los clientes · Prompts para Claude Code

## Cómo usarlos
1. Copia `contenido.md` en el repo como `docs/contenido.md`. Todas las tareas sacan los textos de ahí.
2. Lanza las tareas **en orden y de una en una**. Prueba cada una en el emulador antes de pasar a la siguiente.
3. La tarea 5 (Google Maps) tiene pasos previos que tienes que hacer tú.

## Nueva estructura de la app

| Orden del cliente | Dónde va |
|---|---|
| 1. Qué son / por qué donar | Inicio (arriba del todo) |
| 2. Mapa de Google Maps | Centros |
| 3. Métodos de donación | Cómo funciona |
| 4. Problemas que solucionan | Saber más → Problemas |
| 5. Casos reales | Saber más → Casos reales |
| 6. Preguntas frecuentes y mitos | Saber más → Preguntas frecuentes / Mitos |

"Mi donación" sale de la barra inferior, porque los clientes no la piden. Sus archivos se conservan.

---

## Tarea 1 · Navegación y pestaña "Saber más"

```
Tarea: reorganizar la navegación de Dona Vida según lo que piden los clientes.

IMPORTANTE: esto es un prototipo. Implementa solo lo necesario para que funcione,
sin tests ni refactorizaciones. Usa nombres en español. Si necesitas un componente
privado de otra pantalla, cópialo y anótalo en el resumen.

1. Barra inferior
- Las 4 pestañas pasan a ser: Inicio, Centros, Cómo funciona y Saber más.
- "Saber más" sustituye a "Mi donación" en la última posición, con el icono
  menu_book (descárgalo de Material Symbols Outlined, como los demás).
- Quita Mi donación de la navegación, pero NO borres sus archivos.

2. Quitar los accesos a Mi donación
- Inicio: elimina la tarjeta "¿Ya eres donante o has donado?" (RegisteredDonorCard).
- Avatar de la barra superior: quítale la acción; queda solo como imagen.

3. Pantalla nueva PantallaSaberMas (pestaña)
- Usa el mismo estilo que el resto: LazyColumn, margen lateral de 20dp, 24dp entre bloques.
- Cabecera:
  - StatusBadge "Aprende" (fondo surfaceContainerHigh, texto primary)
  - título "Saber más" en headlineLarge
  - texto "Todo lo que necesitas saber sobre la donación de células madre." en
    bodyMedium onSurfaceVariant
- 4 tarjetas pulsables (SectionCard, esquinas de 16dp, padding de 16dp, 12dp entre
  ellas). Cada una: IconCircle de 44dp, título en headlineSmall, una línea en
  bodySmall y chevron_right al final.
  1. "Qué problemas pueden solucionar" · icono healing · "Enfermedades que se tratan y lo que se investiga"
  2. "Casos reales" · icono favorite · "Historias de pacientes y donantes"
  3. "Preguntas frecuentes" · icono ic_help_outline · "Las dudas más habituales antes de donar"
  4. "Mitos" · icono lightbulb · "Lo que se dice y lo que es verdad"
  Colores de los círculos, en orden: primaryFixed, secondaryFixed, tertiaryFixed y
  surfaceContainerHigh.
- Al final, un enlace de texto "Privacidad" en labelMedium primary, que lleva a la
  pantalla de Privacidad que ya existe.

4. Destinos de las tarjetas (pantallas secundarias con SecondaryTopBar)
- Casos reales → la pantalla Historias que ya existe (título de la barra: "Casos reales").
- Mitos → la pantalla Mitos que ya existe.
- Problemas y Preguntas frecuentes → crea PantallaProblemas y PantallaPreguntas
  vacías, solo con la SecondaryTopBar y el título. El contenido llega en la tarea 2.

No toques nada más.
Resumen final corto: qué has hecho, en qué te has apartado y qué tengo que hacer yo.
```

---

## Tarea 2 · Contenido de "Saber más"

```
Tarea: rellenar las 4 pantallas de "Saber más" con los textos de docs/contenido.md.

IMPORTANTE: esto es un prototipo. Implementa solo lo necesario para que funcione,
sin tests ni refactorizaciones. Usa nombres en español.

Reglas para los textos:
- Copia los textos de docs/contenido.md al pie de la letra en strings.xml.
- No uses NADA de la sección "Notas para el equipo".
- Las marcas [N1], [N2]… no se muestran.
- Las partes en **negrita** van en negrita (usa textoConDestacado).

1. PantallaProblemas (sección 4 de contenido.md)
- Dos bloques, cada uno con su título en headlineSmall y su introducción en bodyMedium:
  - "Tratamientos actuales", con un StatusBadge "En uso" (fondo tertiaryFixed).
  - "En investigación", con un StatusBadge "En investigación" (fondo secondaryFixed).
- Cada punto es una SectionCard (esquinas de 12dp, padding de 16dp) con un
  IconCircle de 40dp, el título del punto en labelLarge y el texto en bodySmall.
- Iconos: bloodtype, healing y autorenew para los actuales; psychology,
  monitor_heart, favorite y healing para los de investigación. Descarga los que falten.

2. Casos reales, en la pantalla Historias que ya existe (sección 5)
- Reescribe su interfaz. Si tiene datos propios (JSON o lista), sustitúyelos por
  los 2 casos de contenido.md.
- Cada caso es una SectionCard con:
  - el nombre en headlineSmall
  - el subtítulo en caption, color primary
  - el texto en bodyMedium
  - si hay URL de fuente, un botón de texto "Ver fuente" con el icono open_in_new,
    que abre el navegador
- Al final, una nota en caption onSurfaceVariant: "Casos publicados en medios de
  comunicación. La evolución de cada paciente puede haber cambiado desde entonces."

3. PantallaPreguntas (sección 6)
- Una tarjeta desplegable por pregunta. Copia el desplegable de Mi donación (fila
  con la pregunta en labelLarge, expand_more que gira y AnimatedVisibility).
- Por defecto, todas cerradas.
- La respuesta va en bodyMedium. La pregunta 6.2 tiene 3 puntos: muéstralos como
  filas con un punto de 6dp en primary.

4. Mitos, en la pantalla que ya existe (sección 7)
- Reescribe su interfaz. Si tiene datos propios, sustitúyelos por los 5 mitos de
  contenido.md.
- Cada mito es una SectionCard con:
  - StatusBadge "MITO" (fondo secondaryFixed, texto onSecondaryFixed) y el mito en
    labelLarge onSurface
  - un separador de 12dp
  - StatusBadge "REALIDAD" (fondo tertiaryFixed, texto onTertiaryFixed) y la
    realidad en bodyMedium

No toques nada más.
Resumen final corto: qué has hecho, en qué te has apartado y qué tengo que hacer yo.
```

---

## Tarea 3 · Introducción en Inicio

```
Tarea: añadir la introducción en la pantalla Inicio.

IMPORTANTE: esto es un prototipo. Implementa solo lo necesario para que funcione,
sin tests ni refactorizaciones.

- Justo después del saludo (GreetingHeader) y antes de la tarjeta "¿Quieres hacerte
  donante?", añade 2 tarjetas con los textos de la sección 1 de docs/contenido.md
  (sin las marcas [N1]):
  1. "¿Qué son las células madre?" · IconCircle con biotech (fondo primaryFixed).
     Son 2 párrafos; el segundo va en bodySmall color primary.
  2. "¿Por qué es importante donarlas?" · IconCircle con favorite (fondo secondaryFixed).
- Estilo: SectionCard (esquinas de 16dp, padding de 24dp) con una fila de
  IconCircle de 40dp + título en headlineSmall, y debajo el texto en bodyMedium
  onSurfaceVariant.
- El resto de Inicio se queda igual.

Resumen final corto.
```

---

## Tarea 4 · Métodos de donación en "Cómo funciona"

```
Tarea: sustituir la sección de pasos de "Cómo funciona" por los métodos de donación.

IMPORTANTE: esto es un prototipo. Implementa solo lo necesario para que funcione,
sin tests ni refactorizaciones.

- Elimina la sección "El camino de la donación" (los 6 pasos).
- En su lugar, añade la sección "Métodos de donación", con:
  - título en headlineSmall y StatusBadge "3 métodos" a la derecha (fondo
    surfaceContainer, texto primary)
  - debajo, 3 tarjetas con los textos de la sección 3 de docs/contenido.md
    (sin las marcas [N…])
- Cada tarjeta es una SectionCard (esquinas de 12dp, padding de 16dp):
  - Cabecera: IconCircle de 48dp + título en headlineSmall.
  - Debajo, cada apartado ("Procedimiento", "Recuperación", "Preparación",
    "Coordinación", "Dato") con la etiqueta en labelSmall primary en mayúsculas y
    el texto en bodySmall onSurfaceVariant, separados 8dp.
  - Iconos y círculos:
    - Médula ósea: medical_services (primaryFixed)
    - Aféresis: bloodtype (secondaryFixed); debajo del título, InfoChip
      "La más habitual" con check_circle
    - Cordón umbilical: child_care (tertiaryFixed)
- Se quedan igual: la cabecera, la imagen principal, la imagen de las manos, los
  requisitos y el botón final.

Resumen final corto.
```

---

## Tarea 5 · Mapa de Google Maps en Centros

### Antes de lanzarla (lo haces tú)
1. Entra en Google Cloud Console y crea un proyecto. Te pedirá activar la facturación; el SDK de Maps para Android normalmente no tiene coste, pero la cuenta tiene que tenerla activa.
2. Activa la API **Maps SDK for Android**.
3. Crea una **clave de API** y restríngela a apps Android, con el nombre del paquete de la app y la huella **SHA-1** de depuración. La SHA-1 se obtiene en Android Studio con la tarea de Gradle `signingReport`. Si tus compañeros generan el APK desde sus PC, añade también sus SHA-1.
4. En el archivo `local.properties` del proyecto, que no se sube a Git, añade la línea `MAPS_API_KEY=tu_clave`.

```
Tarea: sustituir los mapas provisionales por mapas reales de Google Maps.

IMPORTANTE: esto es un prototipo. Implementa solo lo necesario para que funcione,
sin tests ni refactorizaciones.

La clave está en local.properties como MAPS_API_KEY. Léela con el
secrets-gradle-plugin de Google y pásala al manifest. Nunca la escribas en el código.

1. Lista de Centros (MapaCentrosPreview)
- Sustituye la imagen map_centros_placeholder por un GoogleMap de maps-compose,
  en el slot mapContent que ya existe. Mantén la altura animada (lista o mapa),
  las esquinas y la píldora "{n} centros".
- Un marcador por cada centro filtrado. Al pulsarlo, ventana con el nombre; al
  pulsar la ventana, abre la ficha del centro.
- Cámara: si hay ubicación del usuario, centrada en ella con zoom 11 y el punto
  azul de "mi ubicación" activado (solo si hay permiso). Si no, ajustada para
  mostrar todos los centros filtrados. Si no hay ninguno, España (40.4, -3.7, zoom 5.5).
- Quita el degradado oscuro de encima del mapa.

2. Ficha de centro (CenterMapPreview)
- Sustituye map_placeholder por un GoogleMap en modo lite (liteMode = true),
  centrado en el centro con zoom 15 y un marcador.
- Mantén el pin animado solo si no tapa el marcador; si no, quítalo.
- Activa la píldora "Plano satélite" (ic_layers): alterna entre MapType.NORMAL y
  MapType.HYBRID.
- Pulsar el mapa sigue abriendo "Cómo llegar".

3. Borra map_placeholder.xml y map_centros_placeholder.xml si ya no se usan.

Si el mapa sale gris, la clave o la SHA-1 están mal: dilo en el resumen.
Resumen final corto.
```
