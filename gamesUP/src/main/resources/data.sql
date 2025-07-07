-- Données initiales pour la base de données H2

-- Insertion des catégories
INSERT INTO categories (id, type, description) VALUES 
(1, 'Stratégie', 'Jeux de stratégie et de réflexion'),
(2, 'Famille', 'Jeux pour toute la famille'),
(3, 'Aventure', 'Jeux d''aventure et d''exploration'),
(4, 'Éducatif', 'Jeux éducatifs et d''apprentissage');

-- Insertion des éditeurs
INSERT INTO publishers (id, name, description) VALUES 
(1, 'Asmodee', 'Éditeur de jeux de société français'),
(2, 'Days of Wonder', 'Éditeur spécialisé dans les jeux familiaux'),
(3, 'Fantasy Flight Games', 'Éditeur de jeux d''aventure'),
(4, 'Ravensburger', 'Éditeur allemand de jeux éducatifs');

-- Insertion des auteurs
INSERT INTO authors (id, name, biography, country) VALUES 
(1, 'Klaus Teuber', 'Créateur de Catan', 'Allemagne'),
(2, 'Reiner Knizia', 'Auteur de nombreux jeux de stratégie', 'Allemagne'),
(3, 'Alan R. Moon', 'Créateur de Ticket to Ride', 'États-Unis'),
(4, 'Uwe Rosenberg', 'Auteur de jeux agricoles', 'Allemagne');

-- Insertion des jeux
INSERT INTO games (id, nom, description, prix, num_edition, age_minimum, nombre_joueurs_min, nombre_joueurs_max, duree_partie_minutes, disponible, category_id, publisher_id) VALUES 
(1, 'Catan', 'Jeu de colonisation et de commerce', 45.00, 1, 10, 3, 4, 90, true, 1, 1),
(2, 'Ticket to Ride', 'Jeu de construction de chemins de fer', 35.00, 1, 8, 2, 5, 60, true, 2, 2),
(3, 'Pandemic', 'Jeu de coopération pour sauver le monde', 40.00, 1, 8, 2, 4, 45, true, 1, 3),
(4, 'Agricola', 'Jeu de gestion agricole', 50.00, 1, 12, 1, 4, 120, true, 1, 4);

-- Liaison jeux-auteurs
INSERT INTO game_authors (game_id, author_id) VALUES 
(1, 1),
(2, 3),
(3, 2),
(4, 4);

-- Insertion des inventaires
INSERT INTO inventory (id, game_id, stock, stock_minimum, disponible) VALUES 
(1, 1, 10, 5, true),
(2, 2, 8, 3, true),
(3, 3, 5, 2, true),
(4, 4, 3, 1, true);

-- Insertion des utilisateurs
INSERT INTO users (id, nom, prenom, email, password, role, active) VALUES 
(1, 'Admin', 'Admin', 'admin@gamesup.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true),
(2, 'Dupont', 'Jean', 'jean.dupont@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CLIENT', true),
(3, 'Martin', 'Marie', 'marie.martin@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CLIENT', true),
(4, 'Bernard', 'Pierre', 'pierre.bernard@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'CLIENT', true);

-- Insertion d'avis de test
INSERT INTO avis (id, user_id, game_id, commentaire, note, date_creation, approuve) VALUES 
(1, 2, 1, 'Excellent jeu de stratégie !', 5, CURRENT_TIMESTAMP, true),
(2, 3, 1, 'Très bon jeu pour la famille', 4, CURRENT_TIMESTAMP, true),
(3, 4, 2, 'Jeu amusant et accessible', 5, CURRENT_TIMESTAMP, true),
(4, 2, 3, 'Jeu coopératif passionnant', 4, CURRENT_TIMESTAMP, true);

-- Insertion de commandes de test
INSERT INTO purchases (id, user_id, date, total_amount, status, paid, delivered, archived) VALUES 
(1, 2, CURRENT_TIMESTAMP, 45.00, 'CONFIRMED', true, true, false),
(2, 3, CURRENT_TIMESTAMP, 75.00, 'DELIVERED', true, true, false),
(3, 4, CURRENT_TIMESTAMP, 40.00, 'PENDING', false, false, false);

-- Insertion de lignes de commande
INSERT INTO purchase_lines (id, purchase_id, game_id, quantite, prix, prix_remise) VALUES 
(1, 1, 1, 1, 45.00, NULL),
(2, 2, 1, 1, 45.00, NULL),
(3, 2, 2, 1, 35.00, NULL),
(4, 3, 3, 1, 40.00, NULL);

-- Insertion de liste de souhaits
INSERT INTO wishlist (id, user_id, game_id, date_ajout, priorite, note) VALUES 
(1, 2, 4, CURRENT_TIMESTAMP, 1, 'Jeu agricole intéressant'),
(2, 3, 2, CURRENT_TIMESTAMP, 2, 'Pour jouer en famille'),
(3, 4, 1, CURRENT_TIMESTAMP, 1, 'Classique à avoir');