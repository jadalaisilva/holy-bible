import sqlite3
import os

os.makedirs(r"app/src/main/assets/databases", exist_ok=True)
db_path = r"app/src/main/assets/databases/rvr1960.db"

if os.path.exists(db_path):
    os.remove(db_path)

conn = sqlite3.connect(db_path)
cur = conn.cursor()

# Create tables matching Room entities
cur.execute('''
CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    testament TEXT NOT NULL,
    order_index INTEGER NOT NULL,
    chapters_count INTEGER NOT NULL
)
''')

cur.execute('''
CREATE TABLE IF NOT EXISTS chapters (
    id INTEGER PRIMARY KEY NOT NULL,
    book_id INTEGER NOT NULL,
    chapter_number INTEGER NOT NULL,
    verses_count INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE
)
''')

cur.execute('''
CREATE TABLE IF NOT EXISTS verses (
    id INTEGER PRIMARY KEY NOT NULL,
    chapter_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    verse_number INTEGER NOT NULL,
    content_text TEXT NOT NULL,
    audio_url TEXT,
    FOREIGN KEY(chapter_id) REFERENCES chapters(id) ON DELETE CASCADE
)
''')

cur.execute('''
CREATE TABLE IF NOT EXISTS bookmarks (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    verse_id INTEGER NOT NULL,
    book_name TEXT NOT NULL,
    chapter_number INTEGER NOT NULL,
    verse_number INTEGER NOT NULL,
    verse_text TEXT NOT NULL,
    custom_note TEXT,
    timestamp_added INTEGER NOT NULL,
    FOREIGN KEY(verse_id) REFERENCES verses(id) ON DELETE CASCADE
)
''')

cur.execute('''
CREATE TABLE IF NOT EXISTS highlights (
    verse_id INTEGER PRIMARY KEY NOT NULL,
    color_hex TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    FOREIGN KEY(verse_id) REFERENCES verses(id) ON DELETE CASCADE
)
''')

# Populate Books (66 books of RVR1960)
books = [
    (1, "Génesis", "OT", 1, 50), (2, "Éxodo", "OT", 2, 40), (3, "Levítico", "OT", 3, 27),
    (4, "Números", "OT", 4, 36), (5, "Deuteronomio", "OT", 5, 34), (6, "Josué", "OT", 6, 24),
    (7, "Jueces", "OT", 7, 21), (8, "Rut", "OT", 8, 4), (9, "1 Samuel", "OT", 9, 31),
    (10, "2 Samuel", "OT", 10, 24), (11, "1 Reyes", "OT", 11, 22), (12, "2 Reyes", "OT", 12, 25),
    (13, "1 Crónicas", "OT", 13, 29), (14, "2 Crónicas", "OT", 14, 36), (15, "Esdras", "OT", 15, 10),
    (16, "Nehemías", "OT", 16, 13), (17, "Ester", "OT", 17, 10), (18, "Job", "OT", 18, 42),
    (19, "Salmos", "OT", 19, 150), (20, "Proverbios", "OT", 20, 31), (21, "Eclesiastés", "OT", 21, 12),
    (22, "Cantares", "OT", 22, 8), (23, "Isaías", "OT", 23, 66), (24, "Jeremías", "OT", 24, 52),
    (25, "Lamentaciones", "OT", 25, 5), (26, "Ezequiel", "OT", 26, 48), (27, "Daniel", "OT", 27, 12),
    (28, "Oseas", "OT", 28, 14), (29, "Joel", "OT", 29, 3), (30, "Amós", "OT", 30, 9),
    (31, "Abdías", "OT", 31, 1), (32, "Jonás", "OT", 32, 4), (33, "Miqueas", "OT", 33, 7),
    (34, "Nahúm", "OT", 34, 3), (35, "Habacuc", "OT", 35, 3), (36, "Sofonías", "OT", 36, 3),
    (37, "Hageo", "OT", 37, 2), (38, "Zacarías", "OT", 38, 14), (39, "Malaquías", "OT", 39, 4),
    (40, "Mateo", "NT", 40, 28), (41, "Marcos", "NT", 41, 16), (42, "Lucas", "NT", 42, 24),
    (43, "Juan", "NT", 43, 21), (44, "Hechos", "NT", 44, 28), (45, "Romanos", "NT", 45, 16),
    (46, "1 Corintios", "NT", 46, 16), (47, "2 Corintios", "NT", 47, 13), (48, "Gálatas", "NT", 48, 6),
    (49, "Efesios", "NT", 49, 6), (50, "Filipenses", "NT", 50, 4), (51, "Colosenses", "NT", 51, 4),
    (52, "1 Tesalonicenses", "NT", 52, 5), (53, "2 Tesalonicenses", "NT", 53, 3),
    (54, "1 Timoteo", "NT", 54, 6), (55, "2 Timoteo", "NT", 55, 4), (56, "Tito", "NT", 56, 3),
    (57, "Filemón", "NT", 57, 1), (58, "Hebreos", "NT", 58, 13), (59, "Santiago", "NT", 59, 5),
    (60, "1 Pedro", "NT", 60, 5), (61, "2 Pedro", "NT", 61, 3), (62, "1 Juan", "NT", 62, 5),
    (63, "2 Juan", "NT", 63, 1), (64, "3 Juan", "NT", 64, 1), (65, "Judas", "NT", 65, 1),
    (66, "Apocalipsis", "NT", 66, 22)
]

for b in books:
    cur.execute("INSERT INTO books VALUES (?, ?, ?, ?, ?)", b)
    # create chapters
    for ch in range(1, b[4] + 1):
        ch_id = b[0] * 1000 + ch
        cur.execute("INSERT INTO chapters (id, book_id, chapter_number, verses_count) VALUES (?, ?, ?, ?)", (ch_id, b[0], ch, 0))

# Sample verses population
sample_verses = [
    # Genesis 1
    (1001001, 1001, 1, 1, "En el principio creó Dios los cielos y la tierra.", None),
    (1001002, 1001, 1, 2, "Y la tierra estaba desordenada y vacía, y las tinieblas estaban sobre la faz del abismo, y el Espíritu de Dios se movía sobre la faz de las aguas.", None),
    (1001003, 1001, 1, 3, "Y dijo Dios: Sea la luz; y fue la luz.", None),
    (1001004, 1001, 1, 4, "Y vio Dios que la luz era buena; y separó Dios la luz de las tinieblas.", None),
    (1001005, 1001, 1, 5, "Y llamó Dios a la luz Día, y a las tinieblas llamó Noche. Y fue la tarde y la mañana un día.", None),
    # Salmos 23
    (19023001, 19023, 19, 1, "Jehová es mi pastor; nada me faltará.", None),
    (19023002, 19023, 19, 2, "En lugares de delicados pastos me hará descansar; Junto a aguas de reposo me pastoreará.", None),
    (19023003, 19023, 19, 3, "Confortará mi alma; Me guiará por sendas de justicia por amor de su nombre.", None),
    (19023004, 19023, 19, 4, "Aunque ande en valle de sombra de muerte, No temeré mal alguno, porque tú estarás conmigo; Tu vara y tu cayado me infundirán aliento.", None),
    (19023005, 19023, 19, 5, "Aderezas mesa delante de mí en presencia de mis angustiadores; Unges mi cabeza con aceite; mi copa está rebosando.", None),
    (19023006, 19023, 19, 6, "Ciertamente el bien y la misericordia me seguirán todos los días de mi vida, Y en la casa de Jehová moraré por largos días.", None),
    # Juan 1
    (43001001, 43001, 43, 1, "En el principio era el Verbo, y el Verbo era con Dios, y el Verbo era Dios.", None),
    (43001002, 43001, 43, 2, "Este era en el principio con Dios.", None),
    (43001003, 43001, 43, 3, "Todas las cosas por él fueron hechas, y sin él nada de lo que ha sido hecho, fue hecho.", None),
    # Juan 3
    (43003016, 43003, 43, 16, "Porque de tal manera amó Dios al mundo, que ha dado a su Hijo unigénito, para que todo aquel que en él cree, no se pierda, mas tenga vida eterna.", None),
    # Romanos 8
    (45008028, 45008, 45, 28, "Y sabemos que a los que aman a Dios, todas las cosas les ayudan a bien, esto es, a los que conforme a su propósito son llamados.", None),
    (45008031, 45008, 45, 31, "¿Qué, pues, diremos a esto? Si Dios es por nosotros, ¿quién contra nosotros?", None),
    (45008038, 45008, 45, 38, "Por lo cual estoy seguro de que ni la muerte, ni la vida, ni ángeles, ni principados, ni potestades, ni lo presente, ni lo por venir,", None),
    (45008039, 45008, 45, 39, "ni lo alto, ni lo profundo, ni ninguna otra cosa creada nos podrá separar del amor de Dios, que es en Cristo Jesús Señor nuestro.", None),
    # Filipenses 4
    (50004013, 50004, 50, 13, "Todo lo puedo en Cristo que me fortalece.", None)
]

for v in sample_verses:
    cur.execute("INSERT INTO verses VALUES (?, ?, ?, ?, ?, ?)", v)

conn.commit()
conn.close()
print("SQLite database rvr1960.db created successfully.")
