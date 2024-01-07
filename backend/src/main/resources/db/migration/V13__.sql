ALTER TABLE tournament
    ADD has_group_stage BOOLEAN;

UPDATE tournament
SET has_group_stage = TRUE
WHERE has_group_stage IS NULL;

ALTER TABLE tournament
    ALTER COLUMN has_group_stage SET NOT NULL;