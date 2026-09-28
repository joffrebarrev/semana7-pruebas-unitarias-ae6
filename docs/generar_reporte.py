import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

def set_cell_background(cell, fill_hex):
    tcPr = cell._element.get_or_add_tcPr()
    shd = OxmlElement('w:shd')
    shd.set(qn('w:val'), 'clear')
    shd.set(qn('w:color'), 'auto')
    shd.set(qn('w:fill'), fill_hex)
    tcPr.append(shd)

def create_report():
    doc = docx.Document()

    # Configuración de márgenes
    sections = doc.sections
    for section in sections:
        section.top_margin = Inches(1)
        section.bottom_margin = Inches(1)
        section.left_margin = Inches(1)
        section.right_margin = Inches(1)

    # Estilos globales
    style_normal = doc.styles['Normal']
    font = style_normal.font
    font.name = 'Calibri'
    font.size = Pt(11)
    font.color.rgb = RGBColor(0x33, 0x33, 0x33)

    # Portada / Encabezado
    p_title = doc.add_paragraph()
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run_univ = p_title.add_run("UNIVERSIDAD ESPÍRITU SANTO\nFACULTAD DE INGENIERÍA Y CIENCIAS APLICADAS\n")
    run_univ.bold = True
    run_univ.font.size = Pt(14)
    run_univ.font.color.rgb = RGBColor(0x00, 0x33, 0x66)

    run_sub = p_title.add_run("DISEÑO DE SOFTWARE | UCOM0310\n\n")
    run_sub.bold = True
    run_sub.font.size = Pt(12)

    run_act = p_title.add_run("REPORTE TÉCNICO BRIEF - ACTIVIDAD EVALUADA Ae6\n")
    run_act.bold = True
    run_act.font.size = Pt(16)
    run_act.font.color.rgb = RGBColor(0x00, 0x33, 0x66)

    p_meta = doc.add_paragraph()
    p_meta.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_meta.add_run("Estudiante: Joffre Barre\nCarrera: Ingeniería en Computación\nPeríodo: PEL 4 – 2026 | Fecha: 28 de septiembre de 2026\n\n")

    doc.add_paragraph("─" * 55).alignment = WD_ALIGN_PARAGRAPH.CENTER

    # Helper para Títulos
    def add_h1(text):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(14)
        p.paragraph_format.space_after = Pt(4)
        run = p.add_run(text)
        run.bold = True
        run.font.size = Pt(13)
        run.font.color.rgb = RGBColor(0x00, 0x33, 0x66)

    # 1. OBJETIVO
    add_h1("1. OBJETIVO DE LA ACTIVIDAD Ae6")
    doc.add_paragraph(
        "El objetivo principal de esta actividad integradora es diseñar, implementar y documentar una suite de pruebas "
        "unitarias automatizada para el servicio principal de reservas (ReservaService), aplicando pruebas de estado, "
        "de frontera/límites y de interacción mediante dobles de prueba (Stubs y Mocks) en JUnit 5 y Mockito. Además, "
        "se evalúa el porcentaje de cobertura de código alcanzado a través del plugin JaCoCo, estableciendo la trazabilidad "
        "técnica mediante un flujo riguroso de Git y la documentación de un Pull Request profesional."
    )

    # 2. REGLAS DE NEGOCIO Y MATRIZ
    add_h1("2. REGLAS DE NEGOCIO Y MATRIZ DE CASOS DE PRUEBA")
    doc.add_paragraph(
        "1. Regla de Cancelación: Permite cancelar únicamente si la anticipación es mayor o igual a 2 horas.\n"
        "2. Regla de Descuentos: Aplica 15% para 'VIP', 10% para 'ESTUDIANTE' y 0% para 'NORMAL'. Valores menores o iguales a cero lanzan IllegalArgumentException.\n"
        "3. Regla de Confirmación: Consulta disponibilidad externa (DisponibilidadClient). Si está disponible, la confirma, la guarda en ReservaRepository y notifica vía Notificador. De lo contrario, lanza IllegalStateException."
    )

    # Tabla Matriz
    headers = ["ID", "Método", "Escenario", "Entradas", "Resultado Esperado", "Tipo", "Doble de Prueba"]
    data = [
        ["CP-01", "puedeCancelar", "Cancelación normal", "horas = 5", "true", "Normal", "N/A"],
        ["CP-02", "puedeCancelar", "Límite superior", "horas = 2", "true", "Límite", "N/A"],
        ["CP-03", "puedeCancelar", "Debajo del límite", "horas = 1", "false", "Límite", "N/A"],
        ["CP-04", "puedeCancelar", "Sin anticipación", "horas = 0", "false", "Inválido", "N/A"],
        ["CP-05", "calcularTotal", "Cliente NORMAL", "NORMAL, 100.0", "100.0", "Normal", "N/A"],
        ["CP-06", "calcularTotal", "Cliente VIP", "VIP, 100.0", "85.0", "Alternativo", "N/A"],
        ["CP-07", "calcularTotal", "Cliente ESTUDIANTE", "ESTUDIANTE, 100.0", "90.0", "Alternativo", "N/A"],
        ["CP-08", "calcularTotal", "Total cero", "VIP, 0.0", "0.0", "Límite", "N/A"],
        ["CP-08b", "calcularTotal", "Total negativo", "NORMAL, -1.0", "IllegalArgumentException", "Inválido", "N/A"],
        ["CP-09", "confirmar", "Disponible", "Reserva('R-001'), true", "Confirmada, Guardada, Notificada", "Normal", "Stub (Dispo), Mock (Repo/Notif)"],
        ["CP-10", "confirmar", "No disponible", "Reserva('R-002'), false", "IllegalStateException", "Alternativo", "Stub (Dispo), Mock (Repo/Notif)"],
        ["CP-11", "confirmar", "Reserva nula", "null", "IllegalArgumentException", "Inválido", "Mock (Sin interacción)"]
    ]

    table = doc.add_table(rows=len(data) + 1, cols=7)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.style = 'Table Grid'

    # Estilar Encabezados Tabla
    hdr_cells = table.rows[0].cells
    for idx, header_text in enumerate(headers):
        hdr_cells[idx].text = header_text
        hdr_cells[idx].paragraphs[0].runs[0].font.bold = True
        hdr_cells[idx].paragraphs[0].runs[0].font.size = Pt(9)
        set_cell_background(hdr_cells[idx], "003366")
        hdr_cells[idx].paragraphs[0].runs[0].font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

    # Estilar Filas
    for row_idx, row_data in enumerate(data):
        row_cells = table.rows[row_idx + 1].cells
        for col_idx, cell_value in enumerate(row_data):
            row_cells[col_idx].text = cell_value
            row_cells[col_idx].paragraphs[0].runs[0].font.size = Pt(8.5)
            if row_idx % 2 == 1:
                set_cell_background(row_cells[col_idx], "F2F2F2")

    # 3. AAA
    add_h1("3. IMPLEMENTACIÓN JUNIT 5 Y PATRÓN AAA")
    doc.add_paragraph(
        "Todas las pruebas unitarias fueron estructuradas siguiendo el patrón Arrange-Act-Assert (AAA):\n"
        "• Arrange: Inicialización de Mocks y definición de datos/comportamientos esperados.\n"
        "• Act: Ejecución del método bajo prueba en ReservaService.\n"
        "• Assert: Verificación de retornos, cambios de estado y excepciones esperadas.\n"
        "Se utilizó @BeforeEach para garantizar el aislamiento entre ejecuciones reiniciando el servicio y sus dependencias."
    )

    # 4. DOBLES
    add_h1("4. DOBLES DE PRUEBA UTILIZADOS Y JUSTIFICACIÓN")
    doc.add_paragraph(
        "1. DisponibilidadClient (Stub): Configurado con when(...).thenReturn(...) para simular disponibilidad positiva o negativa sin peticiones HTTP/red.\n"
        "2. ReservaRepository y Notificador (Mocks): Verificados mediante verify(...) y verify(..., never()) para comprobar que las acciones de persistencia y envío de correo ocurran únicamente en flujos válidos."
    )

    # 5. RESULTADOS
    add_h1("5. RESULTADO DE EJECUCIÓN (mvn clean test)")
    doc.add_paragraph(
        "Ejecución del comando en la terminal integrada de VS Code:\n"
        "• Comando: mvn clean test\n"
        "• Resultado del Build: BUILD SUCCESS\n"
        "• Pruebas ejecutadas: 12 | Fallos: 0 | Errores: 0 | Omitidas: 0"
    )

    # 6. COBERTURA
    add_h1("6. COBERTURA JACOCO E INTERPRETACIÓN TÉCNICA")
    doc.add_paragraph(
        "Métricas alcanzadas en ReservaService (target/site/jacoco/index.html):\n"
        "• Cobertura de Instrucciones: 100%\n"
        "• Cobertura de Ramas (Branches): 100%\n\n"
        "Reflexión Técnica: Un porcentaje del 100% únicamente asegura que cada línea y condición fueron ejecutadas al menos una vez. "
        "No garantiza la ausencia de errores, dado que no detecta reglas de negocio omitidas en el diseño ni fallos por concurrencia en entornos reales."
    )

    # 7. GIT Y PR
    add_h1("7. FLUJO GIT, PULL REQUEST Y DECLARACIÓN DE IA")
    doc.add_paragraph(
        "• Rama de trabajo: ae6/suite-pruebas\n"
        "• Commits atómicos: 'test: corregir constructor ReservaService...' y 'docs: completar matriz y reporte'\n"
        "• Documentación del PR: Registrada en docs/03_PULL_REQUEST_PLANTILLA.md\n"
        "• Declaración de Uso de IA: Se utilizó un asistente de IA como apoyo en la estructuración de Mocks, corrección del constructor y redacción del reporte técnico."
    )

    doc.save("UCOM0310_Barre_Veliz_Semana7_Ae6_Reporte.docx")
    print("¡Documento 'UCOM0310_Barre_Veliz_Semana7_Ae6_Reporte.docx' generado exitosamente!")

if __name__ == "__main__":
    create_report()