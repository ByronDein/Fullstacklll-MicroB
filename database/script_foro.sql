-- Script de base de datos del microservicio de FORO (Oracle)
-- Ejecutar con el usuario "foro" (el que crea el docker-compose.yml)
-- OJO: si ya levantaste el microservicio, Hibernate crea las tablas solo (ddl-auto=update)
-- y el DataInitializer inserta estos mismos datos, asi que este script es para tenerlo documentado
-- o para crear la base a mano.
-- autor_id guarda el id de un usuario del microservicio de usuarios (1 = Byron, 2 = Camila, 3 = Matias)

-- ==========================
-- TABLAS
-- ==========================
CREATE TABLE categorias (
    id          NUMBER GENERATED AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR2(255) NOT NULL,
    descripcion VARCHAR2(255),
    CONSTRAINT uk_categorias_nombre UNIQUE (nombre)
);

CREATE TABLE temas (
    id             NUMBER GENERATED AS IDENTITY PRIMARY KEY,
    titulo         VARCHAR2(255) NOT NULL,
    contenido      VARCHAR2(2000) NOT NULL,
    autor_id       NUMBER NOT NULL,
    fecha_creacion TIMESTAMP,
    categoria_id   NUMBER NOT NULL,
    CONSTRAINT fk_temas_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);

CREATE TABLE comentarios (
    id             NUMBER GENERATED AS IDENTITY PRIMARY KEY,
    contenido      VARCHAR2(1000) NOT NULL,
    autor_id       NUMBER NOT NULL,
    fecha_creacion TIMESTAMP,
    tema_id        NUMBER NOT NULL,
    CONSTRAINT fk_comentarios_tema FOREIGN KEY (tema_id) REFERENCES temas (id)
);

-- ==========================
-- DATOS DE PRUEBA
-- ==========================
INSERT INTO categorias (nombre, descripcion) VALUES ('Tecnologia', 'Noticias, programacion y gadgets');
INSERT INTO categorias (nombre, descripcion) VALUES ('Videojuegos', 'Todo sobre consolas y juegos de PC');
INSERT INTO categorias (nombre, descripcion) VALUES ('Deportes', 'Futbol, tenis y mas');
INSERT INTO categorias (nombre, descripcion) VALUES ('Musica', 'Artistas, discos y conciertos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Cine y Series', 'Estrenos, criticas y recomendaciones');

INSERT INTO temas (titulo, contenido, autor_id, fecha_creacion, categoria_id)
VALUES ('Cual es el mejor lenguaje para aprender backend?',
        'Estoy partiendo en programacion y no se si irme por Java, Python o Node.',
        3, SYSTIMESTAMP, (SELECT id FROM categorias WHERE nombre = 'Tecnologia'));

INSERT INTO temas (titulo, contenido, autor_id, fecha_creacion, categoria_id)
VALUES ('Que juegos recomiendan para este fin de semana?',
        'Busco algo para jugar con amigos, de preferencia cooperativo.',
        2, SYSTIMESTAMP, (SELECT id FROM categorias WHERE nombre = 'Videojuegos'));

INSERT INTO temas (titulo, contenido, autor_id, fecha_creacion, categoria_id)
VALUES ('Prediccion para la fecha del campeonato',
        'Quien creen que va a ganar el clasico este fin de semana?',
        1, SYSTIMESTAMP, (SELECT id FROM categorias WHERE nombre = 'Deportes'));

INSERT INTO comentarios (contenido, autor_id, fecha_creacion, tema_id)
VALUES ('Yo te recomiendo Java con Spring Boot, es lo que se usa harto en empresas.',
        1, SYSTIMESTAMP, (SELECT id FROM temas WHERE titulo LIKE 'Cual es el mejor lenguaje%'));

INSERT INTO comentarios (contenido, autor_id, fecha_creacion, tema_id)
VALUES ('Prueba It Takes Two, es muy bueno para jugar de a dos.',
        3, SYSTIMESTAMP, (SELECT id FROM temas WHERE titulo LIKE 'Que juegos recomiendan%'));

INSERT INTO comentarios (contenido, autor_id, fecha_creacion, tema_id)
VALUES ('Yo creo que gana el local, juega mucho mejor en casa.',
        2, SYSTIMESTAMP, (SELECT id FROM temas WHERE titulo LIKE 'Prediccion para la fecha%'));

COMMIT;

-- ==========================
-- CONSULTAS PARA REVISAR
-- ==========================
-- SELECT * FROM categorias;
-- SELECT t.id, t.titulo, c.nombre AS categoria, t.autor_id FROM temas t JOIN categorias c ON t.categoria_id = c.id;
-- SELECT * FROM comentarios;
