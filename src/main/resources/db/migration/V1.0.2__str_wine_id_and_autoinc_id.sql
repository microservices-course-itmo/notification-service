ALTER TABLE notification ALTER COLUMN wine_id TYPE TEXT;
ALTER TABLE notification ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY;