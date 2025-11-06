INSERT INTO users (username, accepted) VALUES
                                           ('david', TRUE),
                                           ('ana', FALSE),
                                           ('juan', TRUE);

INSERT INTO sessions (id, user_id, ip, disconnection_time, status) VALUES
                                                                       ('111e4567-e89b-12d3-a456-426614174000', 1, '192.168.0.2', NULL, 'CONNECTED'),
                                                                       ('222e4567-e89b-12d3-a456-426614174001', 3, '192.168.0.5', '2025-11-01 12:30:00', 'DISCONNECTED');

INSERT INTO files (name, size, owner_id, data, upload_date)
VALUES
    ('documento.txt', 512, 1, NULL, CURRENT_TIMESTAMP),
    ('imagen.png', 2048, 3, NULL, CURRENT_TIMESTAMP);

INSERT INTO messages (sender_id, receiver_id, content_type, content_text, content_file_id, date_sent)
VALUES
    (1, 3, 'TEXT', 'Hola Juan, ¿cómo estás?', NULL, CURRENT_TIMESTAMP),
    (3, 1, 'TEXT', 'Bien, gracias. Te envío el archivo.', NULL, CURRENT_TIMESTAMP),
    (3, 1, 'FILE', NULL, 2, CURRENT_TIMESTAMP);
