ALTER TABLE tournament
    ADD number_of_players_in_knockout_bracket SMALLINT;

ALTER TABLE tournament
    ALTER COLUMN number_of_players_in_knockout_bracket SET NOT NULL;

ALTER TABLE tournament
    ALTER COLUMN number_of_groups TYPE SMALLINT USING (number_of_groups::SMALLINT);

ALTER TABLE tournament
    ALTER COLUMN number_of_players TYPE SMALLINT USING (number_of_players::SMALLINT);