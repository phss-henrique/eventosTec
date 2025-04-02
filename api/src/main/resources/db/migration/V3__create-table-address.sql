CREATE TABLE address (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city VARCHAR(100),
    uf VARCHAR(100),
    event_id UUID,
    FOREIGN KEY(event_id) REFERENCES event(id) ON DELETE CASCADE
);