CREATE TABLE hibernate_sequence (
                                    next_val BIGINT
);

INSERT INTO hibernate_sequence (next_val) VALUES (1);
INSERT INTO hibernate_sequence (next_val) VALUES (1);

CREATE TABLE daily_calories (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT,
                                daily_norm DOUBLE PRECISION,
                                consumed_calories DOUBLE PRECISION NOT NULL,
                                remaining_calorie DOUBLE PRECISION,
                                protein_norm DOUBLE PRECISION,
                                consumed_proteins DOUBLE PRECISION,
                                remaining_proteins DOUBLE PRECISION,
                                carbs_norm DOUBLE PRECISION,
                                consumed_carbs DOUBLE PRECISION,
                                remaining_carbs DOUBLE PRECISION,
                                fat_norm DOUBLE PRECISION,
                                consumed_fats DOUBLE PRECISION,
                                remaining_fats DOUBLE PRECISION,
                                carbs_calories DOUBLE PRECISION,
                                protein_calories DOUBLE PRECISION,
                                fat_calories DOUBLE PRECISION,
                                created_at DATE NOT NULL,
                                date DATE,
                                updated_at DATE
);

CREATE TABLE food (
                      id BIGSERIAL PRIMARY KEY,
                      nameFood VARCHAR NOT NULL,
                      protein DECIMAL NOT NULL,
                      carb DECIMAL NOT NULL,
                      fat DECIMAL NOT NULL
);

CREATE TABLE kafka_message_log (
                                   id BIGSERIAL PRIMARY KEY,
                                   payload TEXT NOT NULL,
                                   status VARCHAR NOT NULL,
                                   sent_at TIMESTAMP NOT NULL,
                                   updated_at TIMESTAMP NOT NULL
);